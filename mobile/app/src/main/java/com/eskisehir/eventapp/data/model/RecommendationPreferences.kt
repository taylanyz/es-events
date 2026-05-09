package com.eskisehir.eventapp.data.model

data class RecommendationPreferences(
    val time: TimePreference = TimePreference.ANYTIME,
    val budget: BudgetPreference = BudgetPreference.ANY,
    val crowd: CrowdPreference = CrowdPreference.ANY,
    val selectedCategories: List<Category> = emptyList(),
    val transport: TransportPreference = TransportPreference.ANY,
    val parking: Boolean? = null, // null means indifferent
    val isIndoor: Boolean? = null,
    val companion: CompanionPreference = CompanionPreference.ANY
)

enum class TimePreference(val label: String) {
    TODAY("Bugün"),
    TOMORROW("Yarın"),
    THIS_WEEK("Bu Hafta"),
    WEEKEND("Bu Hafta Sonu"),
    ANYTIME("Fark Etmez")
}

enum class BudgetPreference(val label: String) {
    FREE("Ücretsiz"),
    LOW("Düşük Bütçe"),
    MEDIUM("Orta Bütçe"),
    ANY("Bütçe Önemli Değil")
}

enum class CrowdPreference(val label: String) {
    QUIET("Sakin"),
    MEDIUM("Orta Kalabalık"),
    SOCIAL("Hareketli & Sosyal"),
    ANY("Fark Etmez")
}

enum class TransportPreference(val label: String) {
    WALKABLE("Yürüyerek Ulaşım"),
    PUBLIC("Toplu Taşıma"),
    CAR("Araba"),
    ANY("Fark Etmez")
}

enum class CompanionPreference(val label: String) {
    ALONE("Tek Başıma"),
    FRIENDS("Arkadaşlarla"),
    FAMILY("Aileyle"),
    COUPLE("Çift Olarak"),
    ANY("Fark Etmez")
}

data class EventRecommendation(
    val eventId: Long,
    val score: Int,
    val reason: String,
    val matchedPreferences: List<String>
)
