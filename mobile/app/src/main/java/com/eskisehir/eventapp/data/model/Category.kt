package com.eskisehir.eventapp.data.model

/**
 * Event categories matching the backend Category enum.
 */
enum class Category(val displayNameTr: String) {
    CONCERT("Konser"),
    THEATER("Tiyatro"),
    EXHIBITION("Sergi"),
    FESTIVAL("Festival"),
    WORKSHOP("Workshop"),
    SPORTS("Spor"),
    STAND_UP("Stand-up"),
    CINEMA("Sinema"),
    CONFERENCE("Konferans"),
    MUSEUM("Müze"),
    PARK("Park"),
    WALKING_ROUTE("Yürüyüş Rotası"),
    FOOD("Yeme İçme"),
    UNIVERSITY("Üniversite"),
    CULTURE("Kültür"),
    FAMILY("Aile"),
    TECHNOLOGY("Teknoloji"),
    OUTDOOR("Açık Hava"),
    OTHER("Diğer")
}
