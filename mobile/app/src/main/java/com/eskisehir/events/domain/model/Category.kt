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
    WORKSHOP("Workshop", "🔧"),
    SPORTS("Spor", "⚽"),
    STAND_UP("Stand-up", "😂"),
    CINEMA("Sinema", "🎬"),
    CONFERENCE("Konferans", "💡"),
    MUSEUM("Müze", "🏛️"),
    PARK("Park", "🌳"),
    WALKING_ROUTE("Yürüyüş Rotası", "🚶"),
    FOOD("Yeme İçme", "☕"),
    UNIVERSITY("Üniversite", "🎓"),
    CULTURE("Kültür", "🌍"),
    FAMILY("Aile", "👨‍👩‍👧‍👦"),
    TECHNOLOGY("Teknoloji", "💻"),
    OUTDOOR("Açık Hava", "🏞️"),
    OTHER("Diğer", "📌")
}
