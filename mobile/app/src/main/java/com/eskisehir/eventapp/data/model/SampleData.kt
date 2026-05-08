package com.eskisehir.eventapp.data.model

/**
 * Hardcoded sample events with high-quality reliable images.
 * Optimized URLs for fast and consistent loading across all devices.
 */
object SampleData {

    val events = listOf(
        Event(
            id = 1,
            name = "Eskişehir Balon Festivali",
            description = "Gökyüzü renk renk balonlarla dolacak. Odunpazarı'nın eşsiz manzarasında unutulmaz bir deneyim.",
            category = Category.FESTIVAL,
            latitude = 39.7667,
            longitude = 30.5256,
            venue = "Odunpazarı",
            date = "11 Mayıs, 10:00",
            price = 50.0,
            imageUrl = "https://images.unsplash.com/photo-1534067783941-51c9c23ecefd?q=80&w=1000&auto=format&fit=crop",
            tags = listOf("festival", "açık-hava", "aile")
        ),
        Event(
            id = 2,
            name = "Sanat Galerisi - Modern İzlenimler",
            description = "Çağdaş sanat eserleri sergisi ve sanatçı söyleşileri.",
            category = Category.EXHIBITION,
            latitude = 39.7800,
            longitude = 30.5100,
            venue = "Ankara Caddesi Sanat Merkezi",
            date = "13 Mayıs, 14:00",
            price = 25.0,
            imageUrl = "https://images.unsplash.com/photo-1460661419201-fd4cecdf8a8b?q=80&w=1000&auto=format&fit=crop",
            tags = listOf("sanat", "sergi", "kültür")
        ),
        Event(
            id = 3,
            name = "Rock Konseri - Local Bandlar",
            description = "Eskişehir'in yerel rock gruplarından yüksek enerjili konser.",
            category = Category.CONCERT,
            latitude = 39.7900,
            longitude = 30.5300,
            venue = "Live Club",
            date = "15 Mayıs, 21:00",
            price = 75.0,
            imageUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?q=80&w=1000&auto=format&fit=crop",
            tags = listOf("müzik", "rock", "live")
        ),
        Event(
            id = 4,
            name = "Workshop - Seramik Sanatı",
            description = "Kendi seramik eserinizi yaratmayı öğrenin.",
            category = Category.WORKSHOP,
            latitude = 39.7500,
            longitude = 30.5500,
            venue = "Sanat Atölyesi",
            date = "10 Mayıs, 11:00",
            price = 40.0,
            imageUrl = "https://images.unsplash.com/photo-1565191999001-551c187427bb?q=80&w=1000&auto=format&fit=crop",
            tags = listOf("workshop", "sanat", "eğitim")
        ),
        Event(
            id = 5,
            name = "Yoga ve Meditasyon Sınıfı",
            description = "Sabahın erken saatlerinde huzur dolu bir başlangıç.",
            category = Category.SPORTS,
            latitude = 39.7600,
            longitude = 30.5000,
            venue = "Wellness Center",
            date = "09 Mayıs, 08:30",
            price = 0.0,
            imageUrl = "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?q=80&w=1000&auto=format&fit=crop",
            tags = listOf("spor", "wellness", "yoga")
        )
    )
}
