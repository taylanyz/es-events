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

        if (apiKey.isBlank()) {
            Log.e(TAG, "GEMINI_API_KEY is empty")
            return Result.success(getLocalFallback(preferences))
        }

        val candidateEvents = getCandidateEvents(preferences)
        val prompt = buildPrompt(preferences, candidateEvents)

        return try {
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
            val recommendationResponse = gson.fromJson(jsonText, AiRecommendationResponse::class.java)
            
            val validRecommendations = recommendationResponse.recommendations.filter { rec ->
                SampleData.events.any { it.id == rec.eventId }
            }.map { rec ->
                val event = SampleData.events.find { it.id == rec.eventId }!!
                
                // If AI reason is weak, augment it
                val displayReason = if (rec.reason.isNullOrBlank() || rec.reason.length < 30) {
                    buildDetailedReason(event, preferences)
                } else {
                    rec.reason
                }

                EventRecommendation(
                    eventId = rec.eventId,
                    score = calculateLogicalScore(event, preferences),
                    reason = displayReason,
                    matchedPreferences = if (rec.matchedPreferences.isNullOrEmpty()) buildMatchedList(event, preferences) else rec.matchedPreferences
                )
            }

            if (validRecommendations.isEmpty()) {
                Result.success(getLocalFallback(preferences))
            } else {
                Result.success(validRecommendations.sortedByDescending { it.score })
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error during AI call, using fallback: ${e.message}", e)
            Result.success(getLocalFallback(preferences))
        }
    }

    private fun extractJsonObject(raw: String): String {
        val start = raw.indexOf("{")
        val end = raw.lastIndexOf("}")
        if (start == -1 || end == -1 || end <= start) return "{}"
        return raw.substring(start, end + 1)
    }

    private fun getLocalFallback(prefs: RecommendationPreferences): List<EventRecommendation> {
        return SampleData.events.map { event ->
            EventRecommendation(
                eventId = event.id,
                score = calculateLogicalScore(event, prefs),
                reason = buildDetailedReason(event, prefs),
                matchedPreferences = buildMatchedList(event, prefs)
            )
        }.sortedByDescending { it.score }.take(5)
    }

    private fun calculateLogicalScore(event: Event, prefs: RecommendationPreferences): Int {
        var score = 10
        
        // Category match (+25)
        if (prefs.selectedCategories.contains(event.category)) score += 25
        
        // Crowd level match (+15)
        val prefCrowdLevel = when(prefs.crowd) {
            CrowdPreference.QUIET -> 1
            CrowdPreference.MEDIUM -> 2
            else -> null
        }
        if (prefCrowdLevel != null && event.crowdLevel == prefCrowdLevel) score += 15
        
        // Companion match (+15)
        val companionTag = when(prefs.companion) {
            CompanionPreference.ALONE -> "alone"
            CompanionPreference.FRIENDS -> "friends"
            CompanionPreference.FAMILY -> "family"
            CompanionPreference.PARTNER -> "couple"
            CompanionPreference.GROUP -> "group"
            else -> null
        }
        if (companionTag != null && event.suitableFor.contains(companionTag)) score += 15
        
        // Indoor/Outdoor match (+10)
        if (prefs.isIndoor != null && event.isIndoor == prefs.isIndoor) score += 10
        
        // Time of day match (+10)
        val prefTimeStr = when(prefs.timeOfDay) {
            TimeOfDayPreference.MORNING -> "morning"
            TimeOfDayPreference.AFTERNOON -> "afternoon"
            TimeOfDayPreference.EVENING -> "evening"
            TimeOfDayPreference.NIGHT -> "night"
            else -> null
        }
        if (prefTimeStr != null && event.preferredTimeOfDay == prefTimeStr) score += 10
        
        // Budget match (+10)
        if (prefs.budget == BudgetPreference.FREE && event.price == 0.0) score += 15
        else if (prefs.budget == BudgetPreference.AFFORDABLE && event.priceLevel <= 2) score += 10
        
        // Transport match (+10)
        if (prefs.transport == TransportPreference.PUBLIC && event.publicTransportFriendly) score += 10
        
        return score.coerceIn(0, 100)
    }

    private fun buildDetailedReason(event: Event, prefs: RecommendationPreferences): String {
        val reasons = mutableListOf<String>()
        
        if (prefs.selectedCategories.contains(event.category)) {
            reasons.add("Seçtiğin ${event.category.displayNameTr.lowercase()} kategorisine tam uyum sağlıyor.")
        }
        
        if (prefs.isIndoor != null) {
            if (prefs.isIndoor == event.isIndoor) {
                reasons.add(if (event.isIndoor) "Kapalı mekan tercihinle örtüşüyor." else "Açık hava atmosferi sunuyor.")
            }
        }
        
        if (prefs.timeOfDay != TimeOfDayPreference.ANY) {
            reasons.add("${prefs.timeOfDay.label.lowercase()} saatleri için ideal bir plan.")
        }
        
        if (prefs.budget == BudgetPreference.FREE && event.price == 0.0) {
            reasons.add("Bütçe dostu, tamamen ücretsiz bir seçenek.")
        }
        
        if (reasons.isEmpty()) {
            return "${event.name} etkinliği, Eskişehir'deki merkezi konumu ve popülerliği nedeniyle tercihlerine yakın görünüyor."
        }
        
        return reasons.joinToString(" ") + " Ayrıca ${event.venue} gibi harika bir konumda yer alması avantaj sağlıyor."
    }

    private fun buildMatchedList(event: Event, prefs: RecommendationPreferences): List<String> {
        val list = mutableListOf<String>()
        list.add(event.category.displayNameTr)
        if (prefs.isIndoor == event.isIndoor) list.add(if (event.isIndoor) "Kapalı Mekan" else "Açık Hava")
        if (event.price == 0.0) list.add("Ücretsiz")
        if (event.publicTransportFriendly) list.add("Toplu Taşıma")
        return list
    }

    private fun getCandidateEvents(preferences: RecommendationPreferences): List<Event> {
        return SampleData.events.shuffled().take(20)
    }

    private fun buildPrompt(prefs: RecommendationPreferences, events: List<Event>): String {
        val eventsData = events.joinToString("\n") { 
            "ID: ${it.id}, Ad: ${it.name}, Cat: ${it.category.displayNameTr}, Price: ${it.price} TL, Indoor: ${it.isIndoor}, PT: ${it.publicTransportFriendly}, Crowd: ${it.crowdLevel}, Time: ${it.preferredTimeOfDay}"
        }

        return """
            Sen bir Eskişehir etkinlik öneri uzmanısın. 
            Kullanıcının tercihleri:
            - Kategori: ${prefs.selectedCategories.joinToString { it.displayNameTr }}
            - Kalabalık: ${prefs.crowd.label}
            - Kiminle: ${prefs.companion.label}
            - Mekan: ${if (prefs.isIndoor == true) "Kapalı" else if (prefs.isIndoor == false) "Açık" else "Fark Etmez"}
            - Zaman: ${prefs.timeOfDay.label}
            - Bütçe: ${prefs.budget.label}
            - Ulaşım: ${prefs.transport.label}
            - Süre: ${prefs.duration.label}
            - Sosyal Ortam: ${prefs.socialMood.label}

            Aşağıdaki etkinlik listesini incele ve kullanıcıya en uygun TOP 5 etkinliği seç.
            Etkinlikler:
            $eventsData

            KRİTİK TALİMATLAR:
            1. "reason" alanı mutlaka kullanıcının tercihlerine (Örn: bütçe, zaman, kalabalık) değinen KİŞİSEL bir açıklama olmalı.
            2. Cevabı SADECE geçerli bir JSON objesi olarak döndür. Markdown KULLANMA.

            FORMAT:
            {
              "recommendations": [
                {
                  "eventId": Long,
                  "score": Int(0-100),
                  "reason": "Bu etkinlik bütçenize uygun ve sakin bir ortam sunduğu için seçildi...",
                  "matchedPreferences": ["Kategori", "Zaman", "Bütçe"]
                }
              ]
            }
        """.trimIndent()
    }
}
