package com.eskisehir.events.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object EventDetail : Screen("eventDetail/{eventId}") {
        fun createRoute(eventId: Long) = "eventDetail/$eventId"
    }
    object Profile : Screen("profile")
    object Roadmap : Screen("roadmap")
    object Settings : Screen("settings")
}
