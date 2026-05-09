package com.eskisehir.eventapp.navigation

/**
 * Sealed class defining all navigation routes in the app.
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Explore : Screen("explore")
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
    object Preferences : Screen("preferences")
    object Login : Screen("login")
    object Register : Screen("register")
    object EventDetail : Screen("event_detail/{eventId}") {
        fun createRoute(eventId: Long) = "event_detail/$eventId"
    }
    object Map : Screen("map?eventId={eventId}") {
        fun createRoute(eventId: Long? = null) = if (eventId != null) "map?eventId=$eventId" else "map"
    }
    object EditProfile : Screen("edit_profile")
    object Roadmap : Screen("roadmap")
}
