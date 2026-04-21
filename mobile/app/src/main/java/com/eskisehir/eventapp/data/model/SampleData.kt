package com.eskisehir.eventapp.data.model

/**
 * Hardcoded sample events for initial development.
 * These will be replaced with API calls to the Spring Boot backend.
 */
object SampleData {

    val events = listOf(
        Event(1, "Eskişehir Caz Festivali", "Uluslararası caz müzik festivali.", Category.CONCERT, 39.7667, 30.5256, "Haller Gençlik Merkezi", "2026-05-15T20:00", 150.0, "https://picsum.photos/seed/event1/400/300", listOf("jazz", "live")),
        Event(2, "Porsuk Çayı Işık Gösterisi", "Porsuk üzerinde ışık ve ses gösterisi.", Category.FESTIVAL, 39.7712, 30.5153, "Porsuk Çayı Köprüsü", "2026-05-20T21:00", 0.0, "https://picsum.photos/seed/event2/400/300", listOf("outdoor", "free")),
        Event(3, "Hamlet - Tiyatro", "Shakespeare'in ölümsüz eseri.", Category.THEATER, 39.7620, 30.5280, "Şehir Tiyatrosu", "2026-05-22T19:30", 80.0, "https://picsum.photos/seed/event3/400/300", listOf("drama", "classical")),
        Event(4, "Modern Sanat Sergisi", "Yerli ve yabancı sanatçıların eserleri.", Category.EXHIBITION, 39.7753, 30.5205, "Modern Sanat Müzesi", "2026-05-25T10:00", 30.0, "https://picsum.photos/seed/event4/400/300", listOf("modern", "visual-art")),
        Event(5, "Eskişehirspor Maçı", "Eskişehirspor ev sahibi.", Category.SPORTS, 39.7489, 30.5006, "Yeni Atatürk Stadyumu", "2026-05-28T19:00", 60.0, "https://picsum.photos/seed/event5/400/300", listOf("football", "local")),
        Event(6, "Seramik Atölyesi", "Lületaşı ve seramik yapımı.", Category.WORKSHOP, 39.7730, 30.5190, "Odunpazarı Kültür Merkezi", "2026-06-01T14:00", 120.0, "https://picsum.photos/seed/event6/400/300", listOf("handcraft", "traditional")),
        Event(7, "Stand-up Gecesi", "Komedi gösterisi.", Category.STANDUP, 39.7650, 30.5300, "Kongre Merkezi", "2026-06-05T21:00", 300.0, "https://picsum.photos/seed/event7/400/300", listOf("comedy", "popular")),
        Event(8, "Anadolu Rock Konseri", "Türk rock canlı performans.", Category.CONCERT, 39.7667, 30.5256, "Haller Gençlik Merkezi", "2026-06-08T20:30", 100.0, "https://picsum.photos/seed/event8/400/300", listOf("rock", "turkish")),
        Event(9, "Teknoloji Konferansı", "AI ve yazılım konuşmaları.", Category.CONFERENCE, 39.7500, 30.4800, "ESTÜ", "2026-06-18T09:00", 0.0, "https://picsum.photos/seed/event13/400/300", listOf("technology", "ai", "free")),
        Event(10, "Gastronomi Festivali", "Eskişehir yerel lezzetleri.", Category.FESTIVAL, 39.7740, 30.5170, "Odunpazarı Meydanı", "2026-07-01T11:00", 0.0, "https://picsum.photos/seed/event19/400/300", listOf("food", "local", "free"))
    )
}
