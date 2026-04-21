package com.eskisehir.events.domain.model

/**
 * Enum representing event categories.
 * Mirrors the backend Category enum for API compatibility.
 */
enum class Category(val displayName: String, val emoji: String) {
    CONCERT("Concert", "🎵"),
    THEATER("Theater", "🎭"),
    EXHIBITION("Exhibition", "🎨"),
    FESTIVAL("Festival", "🎪"),
    WORKSHOP("Workshop", "🔧"),
    SPORTS("Sports", "⚽"),
    STANDUP("Stand-up", "😂"),
    CINEMA("Cinema", "🎬"),
    CONFERENCE("Conference", "💡"),
    OTHER("Other", "📌")
}
