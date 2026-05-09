package com.eskisehir.eventapp.data.model

/**
 * Event categories matching the backend Category enum.
 */
enum class Category(val displayNameTr: String) {
    CONCERT("Konser"),
    THEATER("Tiyatro"),
    EXHIBITION("Sergi"),
    FESTIVAL("Festival"),
    WORKSHOP("Atölye"),
    SPORTS("Spor"),
    STANDUP("Stand-up"),
    CINEMA("Sinema"),
    CONFERENCE("Konferans"),
    MUSEUM("Müze"),
    PARK("Park"),
    WALKING_ROUTE("Yürüyüş Rotası"),
    FOOD("Yemek & Kahve"),
    UNIVERSITY("Üniversite"),
    CULTURE("Kültür"),
    FAMILY("Aile & Çocuk"),
    TECHNOLOGY("Teknoloji"),
    OTHER("Diğer")
}
