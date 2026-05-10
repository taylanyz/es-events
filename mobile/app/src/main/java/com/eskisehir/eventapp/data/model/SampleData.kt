package com.eskisehir.eventapp.data.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Rich set of sample events for Eskişehir.
 * Contains ~65 events across various categories with high-quality reliable images.
 * Metadata updated for enhanced AI Discovery.
 */
object SampleData {

    private val now = LocalDate.now()
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    private fun d(days: Long, hour: Int, minute: Int): String {
        return LocalDateTime.of(now.plusDays(days), java.time.LocalTime.of(hour, minute)).format(formatter)
    }

    val events = listOf(
        // ================= TODAY (Day 0) =================
        Event(
            id = 1,
            name = "Porsuk Senfoni Konseri",
            description = "Eskişehir Büyükşehir Belediyesi Senfoni Orkestrası'nın Atatürk Kültür Sanat ve Kongre Merkezi'ndeki büyüleyici akşamına davetlisiniz.",
            category = Category.CONCERT,
            latitude = 39.7892, longitude = 30.5123,
            venue = "Atatürk Kültür Sanat ve Kongre Merkezi",
            address = "Mamure, 26120 Tepebaşı/Eskişehir",
            date = d(0, 20, 0),
            price = 150.0,
            imageUrl = "https://images.unsplash.com/photo-1465847899034-d174fc932ce9?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("klasik", "senfoni", "kültür"),
            isFeatured = true,
            priceLevel = 2, crowdLevel = 2, isIndoor = true, hasParking = true, publicTransportFriendly = true,
            durationMinutes = 120, preferredTimeOfDay = "evening", socialMood = "quiet",
            suitableFor = listOf("alone", "couple", "family")
        ),
        Event(
            id = 2,
            name = "OMM: Modern Sanat Gezisi",
            description = "Odunpazarı Modern Müze'nin dikkat çekici mimarisini ve güncel sergilerini rehber eşliğinde keşfedin.",
            category = Category.MUSEUM,
            latitude = 39.7651, longitude = 30.5245,
            venue = "Odunpazarı Modern Müze (OMM)",
            address = "Şarkiye, 26030 Odunpazarı/Eskişehir",
            date = d(0, 11, 0),
            price = 100.0,
            imageUrl = "https://images.unsplash.com/photo-1554907984-15263bfd63bd?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("sanat", "mimari", "kültür"),
            isFeatured = true,
            priceLevel = 1, crowdLevel = 1, isIndoor = true, hasParking = false, publicTransportFriendly = true,
            durationMinutes = 90, preferredTimeOfDay = "morning", socialMood = "discovery",
            suitableFor = listOf("alone", "friends")
        ),
        Event(
            id = 3,
            name = "Seramik Atölyesi: Başlangıç",
            description = "Kendi kupanızı tasarlamaya ne dersiniz? Odunpazarı'nın tarihi atmosferinde seramik sanatıyla tanışın.",
            category = Category.WORKSHOP,
            latitude = 39.7645, longitude = 30.5250,
            venue = "Atlıhan El Sanatları Çarşısı",
            address = "Paşa, Pazaroğlu Sk. No:7, 26030 Odunpazarı/Eskişehir",
            date = d(0, 14, 0),
            price = 250.0,
            imageUrl = "https://images.unsplash.com/photo-1565191999001-551c187427bb?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("sanat", "atölye", "odunpazarı"),
            priceLevel = 2, crowdLevel = 1, isIndoor = true, hasParking = false, publicTransportFriendly = true,
            durationMinutes = 180, preferredTimeOfDay = "afternoon", socialMood = "discovery",
            suitableFor = listOf("alone", "friends", "couple")
        ),
        Event(
            id = 4,
            name = "Porsuk Boyu Akşam Yürüyüşü",
            description = "Adalar bölgesinden başlayan, nehir kıyısı boyunca huzurlu bir akşam yürüyüşü rotası.",
            category = Category.WALKING_ROUTE,
            latitude = 39.7770, longitude = 30.5180,
            venue = "Adalar / Köprübaşı",
            address = "İstiklal, Odunpazarı/Eskişehir",
            date = d(0, 19, 0),
            price = 0.0,
            imageUrl = "https://images.unsplash.com/photo-1506126613408-eca07ce68773?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("yürüyüş", "şehir", "gece"),
            priceLevel = 0, crowdLevel = 3, isIndoor = false, hasParking = false, publicTransportFriendly = true,
            durationMinutes = 60, preferredTimeOfDay = "evening", socialMood = "romantic",
            suitableFor = listOf("alone", "couple", "friends", "family")
        ),
        Event(
            id = 5,
            name = "Kahve Tadımı Atölyesi",
            description = "Nitelikli kahve dünyasına yolculuk yapın. Farklı demleme yöntemlerini ve çekirdekleri deneyimleyin.",
            category = Category.FOOD,
            latitude = 39.7785, longitude = 30.5130,
            venue = "Third Wave Coffee Lab",
            address = "Hoşnudiye, Cassaba Modern, 26130 Tepebaşı/Eskişehir",
            date = d(0, 16, 30),
            price = 300.0,
            imageUrl = "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("kahve", "gurme", "eğitim"),
            priceLevel = 2, crowdLevel = 1, isIndoor = true, hasParking = true, publicTransportFriendly = true,
            durationMinutes = 90, preferredTimeOfDay = "afternoon", socialMood = "quiet",
            suitableFor = listOf("alone", "friends")
        ),
        Event(
            id = 6,
            name = "Açık Mikrofon Komedi",
            description = "Kahkahaya hazır olun! Eskişehir'in yetenekli komedyenleri sahne alıyor.",
            category = Category.STAND_UP,
            latitude = 39.7755, longitude = 30.5135,
            venue = "Haller Underground Sahne",
            address = "Eskibağlar, Tepebaşı/Eskişehir",
            date = d(0, 21, 30),
            price = 200.0,
            imageUrl = "https://images.unsplash.com/photo-1585699324551-f6c309eedeca?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("komedi", "eğlence", "gece"),
            priceLevel = 2, crowdLevel = 3, isIndoor = true, hasParking = true, publicTransportFriendly = true,
            durationMinutes = 150, preferredTimeOfDay = "night", socialMood = "fun",
            suitableFor = listOf("friends", "couple", "group")
        ),

        // ================= TOMORROW (Day 1) =================
        Event(
            id = 7,
            name = "Rock Gecesi: Yerel Gruplar",
            description = "Eskişehir'in enerjik rock sahnesini keşfedin! Üç farklı yerel grup sahne alacak.",
            category = Category.CONCERT,
            latitude = 39.7758, longitude = 30.5145,
            venue = "Haller Gençlik Merkezi",
            address = "Eskibağlar, 26170 Tepebaşı/Eskişehir",
            date = d(1, 21, 0),
            price = 120.0,
            imageUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("rock", "canlı-müzik", "gençlik"),
            priceLevel = 1, crowdLevel = 3, isIndoor = true, hasParking = true, publicTransportFriendly = true,
            durationMinutes = 240, preferredTimeOfDay = "night", socialMood = "social",
            suitableFor = listOf("friends", "group")
        ),
        Event(
            id = 8,
            name = "Ebru Sanatı Deneyimi",
            description = "Suyun üzerine resim yapmanın büyülü dünyasına girin. Geleneksel Ebru sanatını öğrenin.",
            category = Category.WORKSHOP,
            latitude = 39.7650, longitude = 30.5280,
            venue = "Kurşunlu Külliyesi Atölyeleri",
            address = "Paşa, 26030 Odunpazarı/Eskişehir",
            date = d(1, 11, 0),
            price = 150.0,
            imageUrl = "https://images.unsplash.com/photo-1513364235450-736868841793?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("geleneksel", "ebru", "sanat"),
            priceLevel = 1, crowdLevel = 1, isIndoor = true, hasParking = false, publicTransportFriendly = true,
            durationMinutes = 120, preferredTimeOfDay = "morning", socialMood = "quiet",
            suitableFor = listOf("alone", "family")
        ),
        Event(
            id = 9,
            name = "Bir Delinin Hatıra Defteri",
            description = "Eskişehir Şehir Tiyatroları'nın klasikleşmiş oyunu EBB Sanat ve Kültür Sarayı'nda.",
            category = Category.THEATER,
            latitude = 39.7892, longitude = 30.5123,
            venue = "EBB Sanat ve Kültür Sarayı",
            address = "Mamure, Tepebaşı/Eskişehir",
            date = d(1, 20, 0),
            price = 80.0,
            imageUrl = "https://images.unsplash.com/photo-1507924538820-ede94a04019d?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("tiyatro", "klasik", "drama"),
            priceLevel = 1, crowdLevel = 2, isIndoor = true, hasParking = true, publicTransportFriendly = true,
            durationMinutes = 130, preferredTimeOfDay = "evening", socialMood = "quiet",
            suitableFor = listOf("alone", "friends", "couple", "family")
        ),
        Event(
            id = 10,
            name = "Sazova Parkı: Masal Şatosu",
            description = "Eskişehir'in Disneyland'i Sazova Parkı'nda büyülü bir gün. Çocuklar ve aileler için ideal.",
            category = Category.FAMILY,
            latitude = 39.7675, longitude = 30.4785,
            venue = "Sazova Bilim Kültür ve Sanat Parkı",
            address = "Sazova, 26150 Tepebaşı/Eskişehir",
            date = d(1, 13, 0),
            price = 0.0,
            imageUrl = "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("aile", "çocuk", "eğlence"),
            isFeatured = true,
            priceLevel = 0, crowdLevel = 3, isIndoor = false, hasParking = true, publicTransportFriendly = true,
            durationMinutes = 300, preferredTimeOfDay = "afternoon", socialMood = "fun",
            suitableFor = listOf("family", "group")
        ),

        // ================= DAY 2 =================
        Event(
            id = 11,
            name = "Yoga: Şelale Park",
            description = "Güne merhaba yogası. Şehrin en güzel manzarasında ruhunuzu dinlendirin.",
            category = Category.SPORTS,
            latitude = 39.7562, longitude = 30.5218,
            venue = "Şelale Park",
            address = "Çankaya, Eskişehir",
            date = d(2, 8, 30),
            price = 0.0,
            imageUrl = "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("yoga", "sağlık"),
            priceLevel = 0, crowdLevel = 1, isIndoor = false,
            durationMinutes = 90, preferredTimeOfDay = "morning", socialMood = "quiet",
            suitableFor = listOf("alone", "friends")
        ),
        Event(
            id = 12,
            name = "Mutfak Sanatları: Mantı",
            description = "Geleneksel lezzetleri profesyonellerden öğrenin. Uygulamalı yemek atölyesi.",
            category = Category.FOOD,
            latitude = 39.7745, longitude = 30.5210,
            venue = "Zübeyde Hanım Kültür Merkezi",
            address = "Akarbaşı, Eskişehir",
            date = d(2, 14, 0),
            price = 400.0,
            imageUrl = "https://images.unsplash.com/photo-1507048331197-7d4ac70811cf?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("yemek", "atölye"),
            priceLevel = 2, crowdLevel = 2, isIndoor = true,
            durationMinutes = 180, preferredTimeOfDay = "afternoon", socialMood = "social",
            suitableFor = listOf("friends", "family", "group")
        ),
        Event(
            id = 13,
            name = "Jazz Quartet Live",
            description = "Sıcak bir akşamda kaliteli caz tınıları. Akustik performans.",
            category = Category.CONCERT,
            latitude = 39.7785, longitude = 30.5130,
            venue = "Jazz Loft",
            address = "Hoşnudiye, Eskişehir",
            date = d(2, 21, 30),
            price = 250.0,
            imageUrl = "https://images.unsplash.com/photo-1511192336575-5a79af67a629?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("jazz", "müzik"),
            priceLevel = 2, crowdLevel = 2, isIndoor = true,
            durationMinutes = 150, preferredTimeOfDay = "night", socialMood = "romantic",
            suitableFor = listOf("couple", "alone")
        )
    )
}
