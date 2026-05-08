package com.eskisehir.eventapp.data.model

/**
 * Hardcoded sample events for initial development.
 * These will be replaced with API calls to the Spring Boot backend.
 */
object SampleData {

    val events = listOf(
        Event(1, "Eskişehir Balon Festivali", "Gökyüzü renk renk balonlarla dolacak", Category.FESTIVAL, 39.7667, 30.5256, "Odunpazarı", "2026-05-11T10:08", 50.0, "", listOf("festival", "açık-hava", "aile")),
        Event(2, "Sanat Galerisi - Modern İzlenimler", "Çağdaş sanat eserleri sergisi", Category.EXHIBITION, 39.7800, 30.5100, "Ankara Caddesi Sanat Merkezi", "2026-05-13T10:08", 25.0, "", listOf("sanat", "sergi", "kültür")),
        Event(3, "Rock Konseri - Local Bandlar", "Eskişehir'in yerel rock gruplarından konser", Category.CONCERT, 39.7900, 30.5300, "Live Club", "2026-05-15T10:08", 75.0, "", listOf("müzik", "rock", "live")),
        Event(4, "Workshop - Seramik Sanatı", "Seramik sanatı öğrenme atölyesi", Category.WORKSHOP, 39.7500, 30.5500, "Sanat Atölyesi", "2026-05-10T10:08", 40.0, "", listOf("workshop", "sanat", "eğitim")),
        Event(5, "Yoga ve Meditasyon Sınıfı", "Sabah yoga oturumu ve meditasyon", Category.SPORTS, 39.7600, 30.5000, "Wellness Center", "2026-05-09T10:08", 30.0, "", listOf("spor", "wellness", "yoga"))
    )
}
