package com.eskisehir.eventapp.navigation

/**
 * Sealed class defining all navigation routes in the app.
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Explore : Screen("explore")
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
    object EventDetail : Screen("event_detail/{eventId}") {
        fun createRoute(eventId: Long) = "event_detail/$eventId"
    }
}
