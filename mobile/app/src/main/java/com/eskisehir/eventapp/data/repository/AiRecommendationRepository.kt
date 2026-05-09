package com.eskisehir.eventapp.data.repository

import android.util.Log
import com.eskisehir.eventapp.BuildConfig
import com.eskisehir.eventapp.data.model.Event
import com.eskisehir.eventapp.data.model.EventRecommendation
import com.eskisehir.eventapp.data.model.RecommendationPreferences
import com.eskisehir.eventapp.data.model.SampleData
import com.eskisehir.eventapp.data.model.TimePreference
import com.eskisehir.eventapp.data.remote.ai.dto.AIRecommendationList
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.generationConfig
import com.google.gson.Gson
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiRecommendationRepository @Inject constructor() {
    private val gson = Gson()

    suspend fun getRecommendations(preferences: RecommendationPreferences): Result<List<EventRecommendation>> {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank()) return Result.failure(Exception("Gemini API key is missing."))

        // 1. Resmi Google AI SDK Modeli Kurulumu (0.9.0 uyumlu)
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash", // Hata devam ederse "gemini-pro" denenebilir
            apiKey = apiKey,
            generationConfig = generationConfig {
                temperature = 0.7f
                topK = 32
                topP = 1f
                maxOutputTokens = 2048
                // 0.9.0'da responseMimeType desteği daha kararlı
                responseMimeType = "application/json" 
            },
            safetySettings = listOf(
                SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.MEDIUM_AND_ABOVE)
            )
        )

        val candidateEvents = getCandidateEvents(preferences)
        val prompt = buildPrompt(preferences, candidateEvents)

        return try {
            Log.d("AiRecommendation", "Gemini 1.5 Flash SDK 0.9.0 isteği gönderiliyor...")
            
            val response = generativeModel.generateContent(prompt)
            val jsonText = response.text ?: return Result.failure(Exception("AI boş yanıt döndü."))

            Log.d("AiRecommendation", "SDK Yanıtı Başarılı")

            // Markdown temizliği
            val cleanedJson = jsonText.trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()

            val recommendationList = gson.fromJson(cleanedJson, AIRecommendationList::class.java)
            
            val result = recommendationList.recommendations.map {
                EventRecommendation(
                    eventId = it.eventId,
                    score = it.score,
                    reason = it.reason,
                    matchedPreferences = it.matchedPreferences
                )
            }
            Result.success(result)
        } catch (e: Exception) {
            Log.e("AiRecommendation", "Hata Detayı: ${e.message}", e)
            
            // Eğer model bulunamadı hatası devam ederse otomatik olarak gemini-pro (1.0) dene
            if (e.message?.contains("not found") == true) {
                return getFallbackRecommendations(apiKey, prompt)
            }
            
            Result.failure(e)
        }
    }

    // Yedek model (Flash çalışmazsa kararlı Pro modeline geç)
    private suspend fun getFallbackRecommendations(apiKey: String, prompt: String): Result<List<EventRecommendation>> {
        Log.w("AiRecommendation", "Flash modeli bulunamadı, Pro modeline geçiliyor...")
        val fallbackModel = GenerativeModel(modelName = "gemini-pro", apiKey = apiKey)
        return try {
            val response = fallbackModel.generateContent(prompt)
            val jsonText = response.text ?: return Result.failure(Exception("AI yanıt vermedi."))
            val cleanedJson = jsonText.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
            val recommendationList = gson.fromJson(cleanedJson, AIRecommendationList::class.java)
            Result.success(recommendationList.recommendations.map {
                EventRecommendation(it.eventId, it.score, it.reason, it.matchedPreferences)
            })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getCandidateEvents(preferences: RecommendationPreferences): List<Event> {
        val allEvents = SampleData.events
        val today = LocalDate.now()
        
        val timeFiltered = when(preferences.time) {
            TimePreference.TODAY -> allEvents.filter { it.date.startsWith(today.toString()) }
            TimePreference.TOMORROW -> allEvents.filter { it.date.startsWith(today.plusDays(1).toString()) }
            else -> allEvents
        }

        val budgetFiltered = when(preferences.budget) {
            com.eskisehir.eventapp.data.model.BudgetPreference.FREE -> timeFiltered.filter { it.price == 0.0 }
            else -> timeFiltered
        }

        return budgetFiltered.shuffled().take(15) // Daha az event göndererek token hatasını önle
    }

    private fun buildPrompt(prefs: RecommendationPreferences, events: List<Event>): String {
        val eventsData = events.joinToString("\n") { 
            "ID: ${it.id}, Title: ${it.name}, Cat: ${it.category.name}, Price: ${it.price} TL, Crowd: ${it.crowdLevel}, Indoor: ${it.isIndoor}"
        }

        return """
            Sen bir Eskişehir etkinlik öneri asistanısın. 
            Aşağıdaki etkinlik listesini incele ve kullanıcı tercihlerine göre TOP 5 seçimi yap.
            
            Tercihler: ${prefs.time.label}, ${prefs.budget.label}, ${prefs.crowd.label}.
            Etkinlikler:
            $eventsData

            Sadece yukarıdaki ID'leri kullan. JSON formatında cevap ver.
            {
              "recommendations": [
                {
                  "eventId": Long,
                  "score": Int,
                  "reason": "Türkçe açıklama",
                  "matchedPreferences": ["Etiket"]
                }
              ]
            }
        """.trimIndent()
    }
}
