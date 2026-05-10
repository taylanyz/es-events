package com.eskisehir.eventapp.data.model

data class RecommendationPreferences(
    val selectedCategories: List<Category> = emptyList(),
    val crowd: CrowdPreference = CrowdPreference.ANY,
    val companion: CompanionPreference = CompanionPreference.ANY,
    val isIndoor: Boolean? = null,
    val timeOfDay: TimeOfDayPreference = TimeOfDayPreference.ANY,
    val budget: BudgetPreference = BudgetPreference.ANY,
    val transport: TransportPreference = TransportPreference.ANY,
    val duration: DurationPreference = DurationPreference.ANY,
    val socialMood: SocialMoodPreference = SocialMoodPreference.ANY
)

enum class CrowdPreference(val label: String) {
    QUIET("Sakin"),
    MEDIUM("Orta"),
    ANY("Fark Etmez")
}

enum class CompanionPreference(val label: String) {
    ALONE("Tek Başıma"),
    FRIENDS("Arkadaşımla"),
    FAMILY("Ailemle"),
    PARTNER("Partnerimle"),
    GROUP("Grup Halinde"),
    ANY("Fark Etmez")
}

enum class TimeOfDayPreference(val label: String) {
    MORNING("Sabah"),
    AFTERNOON("Öğleden Sonra"),
    EVENING("Akşam"),
    NIGHT("Gece"),
    ANY("Fark Etmez")
}

enum class BudgetPreference(val label: String) {
    FREE("Ücretsiz"),
    AFFORDABLE("Uygun Fiyatlı"),
    ANY("Fark Etmez")
}

enum class TransportPreference(val label: String) {
    WALKABLE("Yürüyerek"),
    PUBLIC("Toplu Taşıma"),
    CAR("Araba"),
    ANY("Fark Etmez")
}

enum class DurationPreference(val label: String) {
    SHORT("Kısa"),
    MEDIUM("Orta"),
    LONG("Uzun"),
    ANY("Fark Etmez")
}

enum class SocialMoodPreference(val label: String) {
    QUIET("Sessiz / Sakin"),
    SOCIAL("Sosyal / Hareketli"),
    ROMANTIC("Romantik"),
    DISCOVERY("Keşif Odaklı"),
    FUN("Eğlenceli"),
    ANY("Fark Etmez")
}

data class EventRecommendation(
    val eventId: Long,
    val score: Int,
    val reason: String,
    val matchedPreferences: List<String>
)
