package com.eskisehir.eventapp.data.repository

import android.util.Log
import com.eskisehir.eventapp.BuildConfig
import com.eskisehir.eventapp.data.model.*
import com.eskisehir.eventapp.data.remote.ai.dto.AiRecommendationResponse
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiRecommendationRepository @Inject constructor() {
    private val gson = Gson()
    private val TAG = "GeminiRecommendation"
    private val GEMINI_MODEL = "gemini-2.0-flash"

    suspend fun getRecommendations(preferences: RecommendationPreferences): Result<List<EventRecommendation>> {
        val apiKey = BuildConfig.GEMINI_API_KEY
        
        Log.d(TAG, "Gemini request started")
        Log.d(TAG, "API key empty: ${apiKey.isBlank()}")

        if (apiKey.isBlank()) {
            Log.e(TAG, "GEMINI_API_KEY is empty")
            Log.d(TAG, "Recommendation source: LOCAL_FALLBACK (API Key missing)")
            return Result.success(getLocalFallback(preferences))
        }

        val candidateEvents = getCandidateEvents(preferences)
        val prompt = buildPrompt(preferences, candidateEvents)

        return try {
            Log.d(TAG, "Model: $GEMINI_MODEL")
            
            val generativeModel = GenerativeModel(
                modelName = GEMINI_MODEL,
                apiKey = apiKey,
                generationConfig = generationConfig {
                    temperature = 0.6f
                    topK = 32
                    topP = 1f
                    maxOutputTokens = 2048
                    responseMimeType = "application/json"
                }
            )

            val response = generativeModel.generateContent(prompt)
            val rawText = response.text ?: ""
            Log.d(TAG, "Raw response text: $rawText")

            val jsonText = extractJsonObject(rawText)
            Log.d(TAG, "Extracted JSON: $jsonText")

            val recommendationResponse = gson.fromJson(jsonText, AiRecommendationResponse::class.java)
            
            Log.d(TAG, "Parse edilen recommendation listesi size: ${recommendationResponse.recommendations.size}")

            // Mevcut olmayan eventId'leri filtrele
            val validRecommendations = recommendationResponse.recommendations.filter { rec ->
                SampleData.events.any { it.id == rec.eventId }
            }.map { rec ->
                val event = SampleData.events.find { it.id == rec.eventId }!!
                Log.d(TAG, "Recommendation: ID=${rec.eventId}, Title=${event.name}, Score=${rec.score}, Reason=${rec.reason}")
                
                // Eğer AI tarafından gelen reason çok kısaysa veya varsayılan değerse fallback üret
                val displayReason = if (rec.reason.isNullOrBlank() || rec.reason.length < 30) {
                    buildFallbackReason(event, preferences)
                } else {
                    rec.reason
                }

                EventRecommendation(
                    eventId = rec.eventId,
                    score = rec.score,
                    reason = displayReason,
                    matchedPreferences = if (rec.matchedPreferences.isNullOrEmpty()) buildMatchedPreferences(event, preferences) else rec.matchedPreferences
                )
            }

            if (validRecommendations.isEmpty()) {
                Log.w(TAG, "AI geçerli öneri bulamadı veya tümü filtrelendi. Yerel Öneri'ye geçiliyor.")
                Log.d(TAG, "Recommendation source: LOCAL_FALLBACK (Empty AI results)")
                Result.success(getLocalFallback(preferences))
            } else {
                Log.d(TAG, "Recommendation source: GEMINI")
                Result.success(validRecommendations)
            }

        } catch (e: Exception) {
            Log.e(TAG, "Parse error stacktrace: ${e.message}", e)
            Log.d(TAG, "Recommendation source: LOCAL_FALLBACK (Error during AI call)")
            
            // Kritik hata durumunda Yerel Önerileri (Fallback) döndür
            Result.success(getLocalFallback(preferences))
        }
    }

    private fun extractJsonObject(raw: String): String {
        val start = raw.indexOf("{")
        val end = raw.lastIndexOf("}")
        if (start == -1 || end == -1 || end <= start) {
            throw IllegalArgumentException("Gemini response içinde geçerli JSON object bulunamadı.")
        }
        return raw.substring(start, end + 1)
    }

    private fun getLocalFallback(preferences: RecommendationPreferences): List<EventRecommendation> {
        Log.i(TAG, "Yerel öneri sistemi (Fallback) çalışıyor...")
        return SampleData.events.map { event ->
            val matches = mutableListOf<String>()
            var totalScore = 15 

            if (preferences.selectedCategories.contains(event.category)) {
                totalScore += 25
                matches.add(event.category.displayNameTr)
            }
            
            if (preferences.budget == BudgetPreference.FREE && event.price == 0.0) {
                totalScore += 15
                matches.add("Ücretsiz")
            } else if (preferences.budget != BudgetPreference.ANY && event.price > 0 && event.price <= 200) {
                totalScore += 10
                matches.add("Ekonomik")
            }

            if (preferences.transport == TransportPreference.PUBLIC && event.publicTransportFriendly) {
                totalScore += 15
                matches.add("Toplu Taşıma")
            }

            if (preferences.parking == true && event.hasParking) {
                totalScore += 10
                matches.add("Otopark")
            }

            if (preferences.isIndoor != null && event.isIndoor == preferences.isIndoor) {
                totalScore += 10
                matches.add(if (event.isIndoor) "Kapalı Mekan" else "Açık Hava")
            }

            if (preferences.crowd != CrowdPreference.ANY && event.crowdLevel == getCrowdLevelFromPref(preferences.crowd)) {
                totalScore += 15
                matches.add(preferences.crowd.label)
            }

            EventRecommendation(
                eventId = event.id,
                score = totalScore.coerceAtMost(100),
                reason = buildFallbackReason(event, preferences),
                matchedPreferences = matches.ifEmpty { listOf(event.category.displayNameTr) }
            )
        }
        .sortedByDescending { it.score }
        .take(5)
    }

    private fun buildFallbackReason(event: Event, prefs: RecommendationPreferences): String {
        val sentences = mutableListOf<String>()
        
        sentences.add("${event.name} etkinliği, seçtiğin ${event.category.displayNameTr.lowercase()} kategorisine uygunluğu ve ${event.venue} konumundaki yapısıyla öne çıkıyor.")

        val transportText = if (event.publicTransportFriendly) "toplu taşımayla ulaşımı kolay" else "ulaşımı pratik"
        val indoorText = if (event.isIndoor) "kapalı bir alanda" else "açık hava atmosferinde"
        
        val prefMatch = if (prefs.transport == TransportPreference.PUBLIC && event.publicTransportFriendly) {
            "tercih ettiğin toplu taşıma seçeneğine tam uyum sağlıyor"
        } else {
            "planlarını kolaylaştıracak bir konumda yer alıyor"
        }

        sentences.add("Bu aktivite $indoorText gerçekleşeceği için konforlu bir deneyim sunarken, $transportText olması $prefMatch.")

        if (event.price == 0.0) {
            sentences.add("Ayrıca bütçe dostu, tamamen ücretsiz bir program olması Eskişehir'de değerlendirmen gereken harika bir fırsat sunuyor.")
        } else if (event.crowdLevel == 1) {
            sentences.add("Sakin ve huzurlu bir atmosfer arayışındaysan, bu etkinliğin sunduğu dingin ortam beklentilerini fazlasıyla karşılayacaktır.")
        } else {
            sentences.add("Şehrin sosyal dokusuna karışmak ve hareketli bir gün geçirmek isteyen kullanıcılar için ideal bir alternatif.")
        }

        return sentences.take(3).joinToString(" ")
    }

    private fun buildMatchedPreferences(event: Event, prefs: RecommendationPreferences): List<String> {
        val list = mutableListOf<String>()
        list.add(event.category.displayNameTr)
        if (event.price == 0.0) list.add("Ücretsiz")
        if (event.isIndoor) list.add("Kapalı Mekan") else list.add("Açık Hava")
        if (event.publicTransportFriendly) list.add("Toplu Taşıma")
        if (event.hasParking) list.add("Otopark Var")
        return list.take(4)
    }

    private fun getCrowdLevelFromPref(pref: CrowdPreference): Int {
        return when(pref) {
            CrowdPreference.QUIET -> 1
            CrowdPreference.MEDIUM -> 2
            CrowdPreference.SOCIAL -> 3
            else -> 2
        }
    }

    private fun getCandidateEvents(preferences: RecommendationPreferences): List<Event> {
        return SampleData.events.filter { event ->
            preferences.selectedCategories.isEmpty() || event.category in preferences.selectedCategories
        }.shuffled().take(20).ifEmpty { SampleData.events.shuffled().take(20) }
    }

    private fun buildPrompt(prefs: RecommendationPreferences, events: List<Event>): String {
        val eventsData = events.joinToString("\n") { 
            "ID: ${it.id}, Ad: ${it.name}, Konum: ${it.venue}, Kategori: ${it.category.displayNameTr}, Fiyat: ${it.price} TL, Mekan: ${if(it.isIndoor) "Kapalı" else "Açık"}, Ulaşım: ${if(it.publicTransportFriendly) "Kolay" else "Zor"}, Kalabalık: ${it.crowdLevel}"
        }

        return """
            Sen bir Eskişehir etkinlik öneri uzmanısın. 
            Kullanıcının tercihleri:
            - Zaman: ${prefs.time.label}
            - Bütçe: ${prefs.budget.label}
            - Ortam: ${prefs.crowd.label}
            - Kategoriler: ${prefs.selectedCategories.joinToString { it.displayNameTr }}
            - Ulaşım: ${prefs.transport.label}
            - Mekan: ${if (prefs.isIndoor == true) "Kapalı" else if (prefs.isIndoor == false) "Açık" else "Fark Etmez"}

            Aşağıdaki Eskişehir etkinlik listesini incele ve en uygun TOP 5 etkinliği seç.
            Etkinlikler:
            $eventsData

            KRİTİK TALİMATLAR:
            1. "reason" alanı mutlaka EN AZ 2-3 CÜMLE ve 35-70 kelime arasında olmalı.
            2. "reason" metni mutlaka etkinliğin adına, konumuna (venue), ulaşım kolaylığına ve bütçe/mekan tercihlerine özel olarak değinmeli.
            3. "Tercihlerine göre önerildi" gibi kısa ve jenerik cümlelerden ASLA kullanma.
            4. "matchedPreferences" listesine en az 3-4 adet eşleşen spesifik kriter yaz (Örn: "Düşük Bütçe", "Sakin Ortam", "Toplu Taşıma", "Açık Hava").
            5. Sadece Türkçe kullan ve samimi, davetkar bir dil benimse.
            6. Yeni etkinlik uydurma, sadece listedeki ID'leri kullan.
            7. Cevabı SADECE geçerli bir saf JSON objesi olarak döndür. Markdown KULLANMA.

            FORMAT:
            {
              "recommendations": [
                {
                  "eventId": Long,
                  "score": Int(0-100),
                  "reason": "Bu etkinlik, seçtiğin spor ve açık hava tercihlerine tam uyum sağladığı için önerildi. Kanlıkavak bölgesi Eskişehir'in en huzurlu rotalarından birini sunduğundan, kalabalıktan uzak ve aktif bir gün geçirmek isteyenler için ideal bir atmosfer vadediyor. Ayrıca bütçe dostu olması ve toplu taşımayla ulaşımının kısalığı planlarını kolaylaştıracaktır.",
                  "matchedPreferences": ["Spor", "Açık Hava", "Sakin", "Ulaşımı Kolay"]
                }
              ]
            }
        """.trimIndent()
    }
}
