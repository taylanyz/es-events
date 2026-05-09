package com.eskisehir.events.domain.model

/**
 * Enum representing event categories.
 * Mirrors the backend Category enum for API compatibility.
 */
enum class Category(val displayName: String, val emoji: String) {
    CONCERT("Konser", "🎵"),
    THEATER("Tiyatro", "🎭"),
    EXHIBITION("Sergi", "🎨"),
    FESTIVAL("Festival", "🎪"),
    WORKSHOP("Atölye", "🔧"),
    SPORTS("Spor", "⚽"),
    STANDUP("Stand-up", "😂"),
    CINEMA("Sinema", "🎬"),
    CONFERENCE("Konferans", "💡"),
    MUSEUM("Müze", "🏛️"),
    PARK("Park", "🌳"),
    WALKING_ROUTE("Yürüyüş Rotası", "🚶"),
    FOOD("Yemek & Kahve", "☕"),
    UNIVERSITY("Üniversite", "🎓"),
    CULTURE("Kültür", "🌍"),
    FAMILY("Aile & Çocuk", "👨‍👩‍👧‍👦"),
    TECHNOLOGY("Teknoloji", "💻"),
    OTHER("Diğer", "📌")
}
