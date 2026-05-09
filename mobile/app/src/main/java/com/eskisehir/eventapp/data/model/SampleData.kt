package com.eskisehir.eventapp.data.model

/**
 * Rich set of sample events for Eskişehir.
 * Contains ~60 events across various categories with high-quality reliable images.
 */
object SampleData {

    val events = listOf(
        // ================= CONCERT =================
        Event(
            id = 1,
            name = "Porsuk Senfoni Konseri",
            description = "Eskişehir Büyükşehir Belediyesi Senfoni Orkestrası'nın Atatürk Kültür Sanat ve Kongre Merkezi'ndeki büyüleyici akşamına davetlisiniz.",
            category = Category.CONCERT,
            latitude = 39.7892, longitude = 30.5123,
            venue = "Atatürk Kültür Sanat ve Kongre Merkezi",
            address = "Mamure, 26120 Tepebaşı/Eskişehir",
            date = "2026-05-15T20:00",
            price = 100.0,
            imageUrl = "https://images.unsplash.com/photo-1465847899034-d174fc932ce9?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("klasik", "senfoni", "kültür"),
            isFeatured = true
        ),
        Event(
            id = 2,
            name = "Rock Gecesi: Yerel Gruplar",
            description = "Eskişehir'in enerjik rock sahnesini keşfedin! Üç farklı yerel grup sahne alacak.",
            category = Category.CONCERT,
            latitude = 39.7758, longitude = 30.5145,
            venue = "Haller Gençlik Merkezi",
            address = "Eskibağlar, 26170 Tepebaşı/Eskişehir",
            date = "2026-05-16T21:30",
            price = 75.0,
            imageUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("rock", "canlı-müzik", "gençlik")
        ),
        Event(
            id = 3,
            name = "Caz Günleri: Adalar'da Bahar",
            description = "Porsuk kıyısında, Adalar'ın huzurlu atmosferinde açık hava caz konseri.",
            category = Category.CONCERT,
            latitude = 39.7772, longitude = 30.5185,
            venue = "Adalar Porsuk Kıyısı",
            address = "İstiklal, 26010 Odunpazarı/Eskişehir",
            date = "2026-05-20T19:00",
            price = 0.0,
            imageUrl = "https://images.unsplash.com/photo-1511192336575-5a79af67a629?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("caz", "açık-hava", "ücretsiz")
        ),
        Event(
            id = 4,
            name = "Anadolu Rock Efsaneleri",
            description = "Anadolu rock müziğinin unutulmaz isimlerinin eserleri yorumlanıyor.",
            category = Category.CONCERT,
            latitude = 39.7825, longitude = 30.5050,
            venue = "Espark Açık Hava Sahnesi",
            address = "Eskibağlar, 26170 Tepebaşı/Eskişehir",
            date = "2026-06-05T20:30",
            price = 150.0,
            imageUrl = "https://images.unsplash.com/photo-1459749411177-042180ce673c?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("rock", "anadolu", "efsane")
        ),

        // ================= WORKSHOP =================
        Event(
            id = 5,
            name = "Seramik Atölyesi: Başlangıç",
            description = "Kendi kupanızı tasarlamaya ne dersiniz? Seramik sanatıyla tanışın.",
            category = Category.WORKSHOP,
            latitude = 39.7645, longitude = 30.5250,
            venue = "Atlıhan El Sanatları Çarşısı",
            address = "Paşa, Pazaroğlu Sk. No:7, 26030 Odunpazarı/Eskişehir",
            date = "2026-05-18T14:00",
            price = 120.0,
            imageUrl = "https://images.unsplash.com/photo-1565191999001-551c187427bb?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("sanat", "atölye", "odunpazarı")
        ),
        Event(
            id = 6,
            name = "Ebru Sanatı Deneyimi",
            description = "Suyun üzerine resim yapmanın büyülü dünyasına girin.",
            category = Category.WORKSHOP,
            latitude = 39.7650, longitude = 30.5280,
            venue = "Kurşunlu Külliyesi Atölyeleri",
            address = "Paşa, 26030 Odunpazarı/Eskişehir",
            date = "2026-05-19T11:00",
            price = 80.0,
            imageUrl = "https://images.unsplash.com/photo-1513364235450-736868841793?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("geleneksel", "ebru", "sanat")
        ),

        // ================= THEATER =================
        Event(
            id = 7,
            name = "Bir Delinin Hatıra Defteri",
            description = "Eskişehir Şehir Tiyatroları'nın klasikleşmiş oyunu.",
            category = Category.THEATER,
            latitude = 39.7892, longitude = 30.5123,
            venue = "EBB Sanat ve Kültür Sarayı",
            address = "Mamure, Tepebaşı/Eskişehir",
            date = "2026-05-22T20:00",
            price = 60.0,
            imageUrl = "https://images.unsplash.com/photo-1507924538820-ede94a04019d?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("tiyatro", "klasik", "drama")
        ),
        Event(
            id = 8,
            name = "Müzikli Komedi: Komşular",
            description = "Eskişehir'in mahalle kültürünü mizahi bir dille anlatan tiyatro.",
            category = Category.THEATER,
            latitude = 39.7745, longitude = 30.5210,
            venue = "Zübeyde Hanım Kültür Merkezi",
            address = "Akarbaşı, 26020 Odunpazarı/Eskişehir",
            date = "2026-05-23T19:00",
            price = 50.0,
            imageUrl = "https://images.unsplash.com/photo-1503095396549-807a8bc3667c?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("komedi", "müzikli", "yerel")
        ),

        // ================= MUSEUM =================
        Event(
            id = 9,
            name = "OMM: Modern Sanat Gezisi",
            description = "Odunpazarı Modern Müze'nin dikkat çekici mimarisini keşfedin.",
            category = Category.MUSEUM,
            latitude = 39.7651, longitude = 30.5245,
            venue = "Odunpazarı Modern Müze (OMM)",
            address = "Şarkiye, 26030 Odunpazarı/Eskişehir",
            date = "2026-05-11T10:00",
            price = 80.0,
            imageUrl = "https://images.unsplash.com/photo-1554907984-15263bfd63bd?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("sanat", "mimari", "kültür"),
            isFeatured = true
        ),
        Event(
            id = 10,
            name = "Balmumu Heykeller Müzesi",
            description = "Dünyaca ünlü isimlerin balmumu heykellerini görün.",
            category = Category.MUSEUM,
            latitude = 39.7635, longitude = 30.5265,
            venue = "Balmumu Heykeller Müzesi",
            address = "Paşa, 26030 Odunpazarı/Eskişehir",
            date = "2026-05-12T09:00",
            price = 50.0,
            imageUrl = "https://images.unsplash.com/photo-1574739782594-db4ead022697?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("tarih", "sanat", "turistik")
        ),

        // ================= PARK & OUTDOOR =================
        Event(
            id = 11,
            name = "Sazova Parkı: Masal Şatosu",
            description = "Eskişehir'in Disneyland'i Sazova Parkı'nda büyülü bir gün.",
            category = Category.PARK,
            latitude = 39.7675, longitude = 30.4785,
            venue = "Sazova Bilim Kültür ve Sanat Parkı",
            address = "Sazova, 26150 Tepebaşı/Eskişehir",
            date = "2026-05-14T13:00",
            price = 0.0,
            imageUrl = "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("aile", "çocuk", "eğlence"),
            isFeatured = true
        ),
        Event(
            id = 12,
            name = "Kanlıkavak Parkı Pikniği",
            description = "Porsuk Çayı'nın en sakin kollarından birinde hafta sonu keyfi.",
            category = Category.PARK,
            latitude = 39.7610, longitude = 30.5055,
            venue = "Kanlıkavak Parkı",
            address = "Osmangazi, 26020 Odunpazarı/Eskişehir",
            date = "2026-05-17T12:00",
            price = 0.0,
            imageUrl = "https://images.unsplash.com/photo-1500673922987-e212871fec22?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("doğa", "huzur", "piknik")
        ),

        // ================= WALKING_ROUTE =================
        Event(
            id = 13,
            name = "Porsuk Boyu Akşam Yürüyüşü",
            description = "Adalar bölgesinden başlayan keyifli bir yürüyüş rotası.",
            category = Category.WALKING_ROUTE,
            latitude = 39.7770, longitude = 30.5180,
            venue = "Adalar / Köprübaşı",
            address = "İstiklal, Odunpazarı/Eskişehir",
            date = "2026-05-18T19:00",
            price = 0.0,
            imageUrl = "https://images.unsplash.com/photo-1506126613408-eca07ce68773?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("yürüyüş", "şehir", "gece")
        ),

        // ================= SPORTS =================
        Event(
            id = 14,
            name = "Kentpark'ta Sabah Koşusu",
            description = "Kentpark'ta güne enerjik başlayın.",
            category = Category.SPORTS,
            latitude = 39.7820, longitude = 30.5560,
            venue = "Kentpark",
            address = "Şeker, 26120 Tepebaşı/Eskişehir",
            date = "2026-05-18T07:30",
            price = 0.0,
            imageUrl = "https://images.unsplash.com/photo-1461896836934-ffe607ba8211?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("koşu", "sağlık", "açık-hava")
        ),

        // ================= FOOD =================
        Event(
            id = 15,
            name = "Kahve Tadımı Atölyesi",
            description = "Nitelikli kahve dünyasına yolculuk yapın.",
            category = Category.FOOD,
            latitude = 39.7785, longitude = 30.5130,
            venue = "Third Wave Coffee Lab",
            address = "Hoşnudiye, Cassaba Modern, 26130 Tepebaşı/Eskişehir",
            date = "2026-05-21T18:00",
            price = 200.0,
            imageUrl = "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("kahve", "gurme", "eğitim")
        ),

        // ================= STAND-UP =================
        Event(
            id = 16,
            name = "Açık Mikrofon Komedi",
            description = "Kahkahaya hazır olun! Stand-up gecesi.",
            category = Category.STANDUP,
            latitude = 39.7755, longitude = 30.5135,
            venue = "Haller Underground Sahne",
            address = "Eskibağlar, Tepebaşı/Eskişehir",
            date = "2026-05-27T20:30",
            price = 120.0,
            imageUrl = "https://images.unsplash.com/photo-1585699324551-f6c309eedeca?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("komedi", "eğlence", "gece")
        ),

        // ================= UNIVERSITY =================
        Event(
            id = 17,
            name = "Anadolu Üni Kariyer Günleri",
            description = "Sektörün dev isimleriyle tanışın.",
            category = Category.UNIVERSITY,
            latitude = 39.7915, longitude = 30.5010,
            venue = "Öğrenci Merkezi",
            address = "Yeşiltepe, 26470 Tepebaşı/Eskişehir",
            date = "2026-05-25T09:30",
            price = 0.0,
            imageUrl = "https://images.unsplash.com/photo-1523050854058-8df90110c9f1?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("kariyer", "öğrenci", "seminer")
        ),

        // ================= FAMILY =================
        Event(
            id = 18,
            name = "Masal Şatosu: Hikaye Saati",
            description = "Çocuklar için interaktif masal anlatımı.",
            category = Category.FAMILY,
            latitude = 39.7675, longitude = 30.4785,
            venue = "Masal Şatosu",
            address = "Sazova Parkı, Eskişehir",
            date = "2026-05-16T14:30",
            price = 40.0,
            imageUrl = "https://images.unsplash.com/photo-1472586662442-3eec04b9dbda?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("çocuk", "masal", "eğitim")
        ),

        // ================= TECHNOLOGY =================
        Event(
            id = 19,
            name = "Mobil Uygulama Atölyesi",
            description = "Kotlin ve Jetpack Compose ile Android giriş.",
            category = Category.TECHNOLOGY,
            latitude = 39.7915, longitude = 30.5010,
            venue = "Teknopark",
            address = "Yunus Emre Kampüsü, Eskişehir",
            date = "2026-06-01T13:00",
            price = 0.0,
            imageUrl = "https://images.unsplash.com/photo-1518770660439-4636190af475?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("yazılım", "android", "teknoloji")
        )
    )
}
