package com.eskisehir.eventapp.data.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Rich set of sample events for Eskişehir.
 * Contains ~65 events across various categories with high-quality reliable images.
 * Dates are dynamically generated based on today to ensure functionality.
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
            priceLevel = 2, crowdLevel = 2, isIndoor = true, hasParking = true, publicTransportFriendly = true
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
            priceLevel = 1, crowdLevel = 2, isIndoor = true, hasParking = false, publicTransportFriendly = true
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
            priceLevel = 2, crowdLevel = 1, isIndoor = true, hasParking = false, publicTransportFriendly = true
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
            priceLevel = 0, crowdLevel = 3, isIndoor = false, hasParking = false, publicTransportFriendly = true
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
            priceLevel = 2, crowdLevel = 1, isIndoor = true, hasParking = true, publicTransportFriendly = true
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
            priceLevel = 2, crowdLevel = 3, isIndoor = true, hasParking = true, publicTransportFriendly = true
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
            priceLevel = 1, crowdLevel = 3, isIndoor = true, hasParking = true, publicTransportFriendly = true
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
            priceLevel = 1, crowdLevel = 1, isIndoor = true, hasParking = false, publicTransportFriendly = true
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
            priceLevel = 1, crowdLevel = 2, isIndoor = true, hasParking = true, publicTransportFriendly = true
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
            priceLevel = 0, crowdLevel = 3, isIndoor = false, hasParking = true, publicTransportFriendly = true
        ),
        Event(
            id = 11,
            name = "Bisiklet Turu: Kanlıkavak",
            description = "Kanlıkavak Parkı boyunca nehir kenarında sabah sporu. Kendi bisikletinizle katılabilirsiniz.",
            category = Category.SPORTS,
            latitude = 39.7610, longitude = 30.5055,
            venue = "Kanlıkavak Parkı",
            address = "Osmangazi, 26020 Odunpazarı/Eskişehir",
            date = d(1, 8, 30),
            price = 0.0,
            imageUrl = "https://images.unsplash.com/photo-1461896836934-ffe607ba8211?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("bisiklet", "spor", "açık-hava"),
            priceLevel = 0, crowdLevel = 2, isIndoor = false, hasParking = false, publicTransportFriendly = true
        ),
        Event(
            id = 12,
            name = "Anadolu Rock Efsaneleri",
            description = "Anadolu rock müziğinin unutulmaz isimlerinin eserleri yorumlanıyor.",
            category = Category.CONCERT,
            latitude = 39.7825, longitude = 30.5050,
            venue = "Espark Açık Hava Sahnesi",
            address = "Eskibağlar, 26170 Tepebaşı/Eskişehir",
            date = d(1, 20, 30),
            price = 300.0,
            imageUrl = "https://images.unsplash.com/photo-1459749411177-042180ce673c?q=80&w=1200&auto=format&fit=crop",
            tags = listOf("rock", "anadolu", "efsane"),
            priceLevel = 2, crowdLevel = 3, isIndoor = false, hasParking = true, publicTransportFriendly = true
        ),

        // ================= DAY 2 =================
        Event(13, "Yoga: Şelale Park", "Güne merhaba yogası. Şehrin en güzel manzarasında ruhunuzu dinlendirin.", Category.SPORTS, 39.7562, 30.5218, "Şelale Park", "Çankaya, Eskişehir", d(2, 8, 30), 0.0, "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?q=80&w=1200&auto=format&fit=crop", listOf("yoga", "sağlık"), priceLevel = 0, crowdLevel = 1, isIndoor = false),
        Event(14, "Mutfak Sanatları: Mantı", "Geleneksel lezzetleri profesyonellerden öğrenin. Uygulamalı yemek atölyesi.", Category.FOOD, 39.7745, 30.5210, "Zübeyde Hanım Kültür Merkezi", "Akarbaşı, Eskişehir", d(2, 14, 0), 400.0, "https://images.unsplash.com/photo-1507048331197-7d4ac70811cf?q=80&w=1200&auto=format&fit=crop", listOf("yemek", "atölye"), priceLevel = 2, crowdLevel = 2, isIndoor = true),
        Event(15, "Fotoğrafçılık Gezisi", "Odunpazarı sokaklarında kadrajınızı geliştirin. Teknik eğitim ve pratik.", Category.TECHNOLOGY, 39.7645, 30.5255, "Odunpazarı Sokakları", "Şarkiye, Eskişehir", d(2, 15, 0), 200.0, "https://images.unsplash.com/photo-1542038784456-1ea8e935640e?q=80&w=1200&auto=format&fit=crop", listOf("fotoğraf", "eğitim"), priceLevel = 2, crowdLevel = 2, isIndoor = false),
        Event(16, "Jazz Quartet Live", "Sıcak bir akşamda kaliteli caz tınıları. Akustik performans.", Category.CONCERT, 39.7785, 30.5130, "Jazz Loft", "Hoşnudiye, Eskişehir", d(2, 21, 30), 250.0, "https://images.unsplash.com/photo-1511192336575-5a79af67a629?q=80&w=1200&auto=format&fit=crop", listOf("jazz", "müzik"), priceLevel = 2, crowdLevel = 2, isIndoor = true),
        Event(17, "Kariyer Günleri", "Sektörün dev isimleriyle tanışın. Networking ve seminerler.", Category.UNIVERSITY, 39.7915, 30.5010, "AU Öğrenci Merkezi", "Tepebaşı, Eskişehir", d(2, 10, 0), 0.0, "https://images.unsplash.com/photo-1523050854058-8df90110c9f1?q=80&w=1200&auto=format&fit=crop", listOf("kariyer", "öğrenci"), priceLevel = 0, crowdLevel = 3, isIndoor = true),
        Event(18, "Pilates: Kentpark", "Kentpark'ın ferah havasında pilates seansı. Matınızı getirmeyi unutmayın.", Category.SPORTS, 39.7820, 30.5560, "Kentpark", "Tepebaşı, Eskişehir", d(2, 7, 30), 0.0, "https://images.unsplash.com/photo-1518611012118-29a8d63a812e?q=80&w=1200&auto=format&fit=crop", listOf("spor", "pilates"), priceLevel = 0, crowdLevel = 2, isIndoor = false),

        // ================= DAY 3 =================
        Event(19, "Mobil Yazılım Atölyesi", "Kotlin ve Jetpack Compose ile modern uygulama geliştirme.", Category.TECHNOLOGY, 39.7915, 30.5010, "AU Teknopark", "Tepebaşı, Eskişehir", d(3, 13, 0), 0.0, "https://images.unsplash.com/photo-1518770660439-4636190af475?q=80&w=1200&auto=format&fit=crop", listOf("yazılım", "android"), priceLevel = 0, crowdLevel = 2, isIndoor = true),
        Event(20, "Tango: İlk Adım", "Arjantin Tangosu'na giriş yapın. Partnerli veya partnersiz katılabilirsiniz.", Category.WORKSHOP, 39.7800, 30.5100, "Dans Akademisi", "Hoşnudiye, Eskişehir", d(3, 19, 30), 100.0, "https://images.unsplash.com/photo-1503095396549-807a8bc3667c?q=80&w=1200&auto=format&fit=crop", listOf("dans", "hobi"), priceLevel = 1, crowdLevel = 2, isIndoor = true),
        Event(21, "Resim Sergisi: Doğa", "Yerel sanatçıların doğa temalı eserleri Haller Sergi Salonu'nda.", Category.EXHIBITION, 39.7750, 30.5145, "Haller Sergi Salonu", "Eskibağlar, Eskişehir", d(3, 11, 0), 0.0, "https://images.unsplash.com/photo-1460661419201-fd4cecdf8a8b?q=80&w=1200&auto=format&fit=crop", listOf("sergi", "sanat"), priceLevel = 0, crowdLevel = 1, isIndoor = true),
        Event(22, "Bilim Deneyleri", "Çocuklar için eğlenceli ve öğretici fen deneyleri.", Category.FAMILY, 39.7675, 30.4785, "Bilim Merkezi", "Sazova Parkı, Eskişehir", d(3, 11, 0), 50.0, "https://images.unsplash.com/photo-1516533075015-a3838414c3cb?q=80&w=1200&auto=format&fit=crop", listOf("bilim", "eğitim"), priceLevel = 1, crowdLevel = 2, isIndoor = true),
        Event(23, "Bando Konseri", "EBB Bando ekibinden görkemli bir akşam konseri.", Category.CONCERT, 39.7892, 30.5123, "Kültür Sarayı", "Mamure, Eskişehir", d(3, 20, 0), 0.0, "https://images.unsplash.com/photo-1465847899034-d174fc932ce9?q=80&w=1200&auto=format&fit=crop", listOf("bando", "klasik"), priceLevel = 0, crowdLevel = 3, isIndoor = true),
        Event(24, "Akustik Gitar Gecesi", "Sakin bir akşamda popüler şarkıların akustik yorumları.", Category.CONCERT, 39.7805, 30.5140, "Old Town Cafe", "Hoşnudiye, Eskişehir", d(3, 21, 0), 80.0, "https://images.unsplash.com/photo-1510915361894-db8b60106cb1?q=80&w=1200&auto=format&fit=crop", listOf("akustik", "gece"), priceLevel = 1, crowdLevel = 2, isIndoor = true),

        // ================= DAY 4 =================
        Event(25, "Film Okumaları", "Seçili kült filmlerin analizi ve tartışma oturumu.", Category.CINEMA, 39.7750, 30.5140, "Haller Sinema", "Tepebaşı, Eskişehir", d(4, 19, 0), 0.0, "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?q=80&w=1200&auto=format&fit=crop", listOf("sinema", "kültür"), priceLevel = 0, crowdLevel = 2, isIndoor = true),
        Event(26, "Pasta Tasarımı", "Temel süsleme teknikleri ve figür yapımı atölyesi.", Category.WORKSHOP, 39.7800, 30.5100, "Mutfağım Atölye", "Hoşnudiye, Eskişehir", d(4, 14, 0), 500.0, "https://images.unsplash.com/photo-1513364235450-736868841793?q=80&w=1200&auto=format&fit=crop", listOf("mutfak", "pasta"), priceLevel = 3, crowdLevel = 1, isIndoor = true),
        Event(27, "Gastronomi Turu", "Eskişehir'in gizli lezzet duraklarına gurme gezisi.", Category.FOOD, 39.7770, 30.5180, "Buluşma Noktası", "Köprübaşı, Eskişehir", d(4, 11, 0), 750.0, "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?q=80&w=1200&auto=format&fit=crop", listOf("yemek", "gezi"), priceLevel = 3, crowdLevel = 2, isIndoor = false),
        Event(28, "Sanat Terapisi", "Renklerle ve formlarla duyguları ifade etme çalışması.", Category.WORKSHOP, 39.7645, 30.5250, "Sanat Evi", "Odunpazarı, Eskişehir", d(4, 14, 0), 300.0, "https://images.unsplash.com/photo-1513364235450-736868841793?q=80&w=1200&auto=format&fit=crop", listOf("sanat", "psikoloji"), priceLevel = 2, crowdLevel = 1, isIndoor = true),
        Event(29, "Sokak Müzisyenleri", "Adalar kıyısında gün boyu devam eden müzik şöleni.", Category.FESTIVAL, 39.7770, 30.5180, "Adalar Kıyısı", "Eskişehir", d(4, 14, 0), 0.0, "https://images.unsplash.com/photo-1534067783941-51c9c23ecefd?q=80&w=1200&auto=format&fit=crop", listOf("müzik", "festival"), priceLevel = 0, crowdLevel = 3, isIndoor = false),
        Event(30, "Mizah Sergisi", "Yerel çizerlerden Eskişehir temalı karikatürler.", Category.EXHIBITION, 39.7750, 30.5145, "Haller Sergi Salonu", "Eskişehir", d(4, 11, 0), 0.0, "https://images.unsplash.com/photo-1460661419201-fd4cecdf8a8b?q=80&w=1200&auto=format&fit=crop", listOf("mizah", "sergi"), priceLevel = 0, crowdLevel = 2, isIndoor = true),

        // ================= DAY 5 =================
        Event(31, "Balon Festivali", "Kentpark üzerinde renkli balonlar ve sabah kahvaltısı.", Category.FESTIVAL, 39.7820, 30.5560, "Kentpark", "Eskişehir", d(5, 7, 0), 100.0, "https://images.unsplash.com/photo-1534067783941-51c9c23ecefd?q=80&w=1200&auto=format&fit=crop", listOf("festival", "eğlence"), priceLevel = 1, crowdLevel = 3, isIndoor = false),
        Event(32, "Kodlama Maratonu", "24 saatlik aralıksız yazılım geliştirme etkinliği.", Category.TECHNOLOGY, 39.7915, 30.5010, "AU Teknopark", "Eskişehir", d(5, 9, 0), 0.0, "https://images.unsplash.com/photo-1518770660439-4636190af475?q=80&w=1200&auto=format&fit=crop", listOf("yazılım", "teknoloji"), priceLevel = 0, crowdLevel = 3, isIndoor = true),
        Event(33, "Salsa Workshop", "Temel Latin dansı teknikleri ve keyifli bir akşam.", Category.WORKSHOP, 39.7800, 30.5100, "Dans Salonu", "Eskişehir", d(5, 19, 0), 150.0, "https://images.unsplash.com/photo-1503095396549-807a8bc3667c?q=80&w=1200&auto=format&fit=crop", listOf("dans", "workshop"), priceLevel = 1, crowdLevel = 2, isIndoor = true),
        Event(34, "Arkeoloji Müzesi", "Eskişehir ve çevresinin antik tarihine yolculuk.", Category.MUSEUM, 39.7700, 30.5100, "Arkeoloji Müzesi", "Eskişehir", d(5, 10, 0), 80.0, "https://images.unsplash.com/photo-1574739782594-db4ead022697?q=80&w=1200&auto=format&fit=crop", listOf("tarih", "müze"), priceLevel = 1, crowdLevel = 2, isIndoor = true),
        Event(35, "Canlı Jazz Trio", "Haller atmosferinde caz standartları.", Category.CONCERT, 39.7755, 30.5135, "Haller Underground", "Eskişehir", d(5, 21, 0), 120.0, "https://images.unsplash.com/photo-1511192336575-5a79af67a629?q=80&w=1200&auto=format&fit=crop", listOf("jazz", "konser"), priceLevel = 1, crowdLevel = 2, isIndoor = true),
        Event(36, "Fidan Dikme Etkinliği", "Gelecek nesillere daha yeşil bir Eskişehir için.", Category.OUTDOOR, 39.8000, 30.5500, "Ağaçlandırma Sahası", "Eskişehir", d(5, 10, 30), 0.0, "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?q=80&w=1200&auto=format&fit=crop", listOf("doğa", "sosyal"), priceLevel = 0, crowdLevel = 3, isIndoor = false),

        // ================= DAY 6 =================
        Event(37, "Klasik Müzik Matinesi", "Pazar sabahı huzurlu bir klasik müzik dinletisi.", Category.CONCERT, 39.7892, 30.5123, "Kültür Sarayı", "Eskişehir", d(6, 11, 0), 100.0, "https://images.unsplash.com/photo-1465847899034-d174fc932ce9?q=80&w=1200&auto=format&fit=crop", listOf("klasik", "müzik"), priceLevel = 1, crowdLevel = 2, isIndoor = true),
        Event(38, "Masal Okumaları", "Masal Şatosu'nda çocuklar için interaktif hikaye saati.", Category.FAMILY, 39.7675, 30.4785, "Masal Şatosu", "Eskişehir", d(6, 14, 30), 40.0, "https://images.unsplash.com/photo-1472586662442-3eec04b9dbda?q=80&w=1200&auto=format&fit=crop", listOf("çocuk", "masal"), priceLevel = 1, crowdLevel = 2, isIndoor = true),
        Event(39, "Piknik ve Oyunlar", "Kanlıkavak Parkı'nda aile boyu pazar keyfi.", Category.PARK, 39.7610, 30.5055, "Kanlıkavak", "Eskişehir", d(6, 12, 0), 0.0, "https://images.unsplash.com/photo-1500673922987-e212871fec22?q=80&w=1200&auto=format&fit=crop", listOf("piknik", "oyun"), priceLevel = 0, crowdLevel = 3, isIndoor = false),
        Event(40, "Yerel Pazar Gezisi", "Odunpazarı el sanatları ve yöresel ürünler pazarı.", Category.CULTURE, 39.7640, 30.5260, "Tarihi Meydan", "Eskişehir", d(6, 10, 0), 0.0, "https://images.unsplash.com/photo-1542038784456-1ea8e935640e?q=80&w=1200&auto=format&fit=crop", listOf("pazar", "kültür"), priceLevel = 0, crowdLevel = 3, isIndoor = false),
        Event(41, "Gitar Resitali", "Solo gitar performansı ve dünya müzikleri.", Category.CONCERT, 39.7750, 30.5140, "Haller Sahne", "Eskişehir", d(6, 19, 0), 75.0, "https://images.unsplash.com/photo-1510915361894-db8b60106cb1?q=80&w=1200&auto=format&fit=crop", listOf("müzik", "gitar"), priceLevel = 1, crowdLevel = 2, isIndoor = true),
        Event(42, "Modern Dans", "Beden diliyle anlatılan çağdaş hikayeler.", Category.THEATER, 39.7890, 30.5120, "Opera Salonu", "Eskişehir", d(6, 20, 30), 120.0, "https://images.unsplash.com/photo-1503095396549-807a8bc3667c?q=80&w=1200&auto=format&fit=crop", listOf("dans", "sanat"), priceLevel = 1, crowdLevel = 2, isIndoor = true),

        // ================= DAY 7 =================
        Event(43, "Sabah Koşusu", "Kentpark'ta profesyonel antrenör eşliğinde koordineli koşu.", Category.SPORTS, 39.7820, 30.5560, "Kentpark", "Eskişehir", d(7, 7, 30), 0.0, "https://images.unsplash.com/photo-1461896836934-ffe607ba8211?q=80&w=1200&auto=format&fit=crop", listOf("koşu", "spor"), priceLevel = 0, crowdLevel = 2, isIndoor = false),
        Event(44, "Teknoloji Zirvesi", "Yapay zeka ve geleceğin teknolojileri paneli.", Category.TECHNOLOGY, 39.7915, 30.5010, "AU Kongre Merkezi", "Eskişehir", d(7, 9, 30), 0.0, "https://images.unsplash.com/photo-1518770660439-4636190af475?q=80&w=1200&auto=format&fit=crop", listOf("teknoloji", "panel"), priceLevel = 0, crowdLevel = 3, isIndoor = true),
        Event(45, "Çay Atölyesi", "Dünya çaylarını tanıma ve doğru demleme teknikleri.", Category.FOOD, 39.7785, 30.5130, "Tea Lab", "Eskişehir", d(7, 16, 0), 200.0, "https://images.unsplash.com/photo-1576092768241-dec231879fc3?q=80&w=1200&auto=format&fit=crop", listOf("çay", "atölye"), priceLevel = 2, crowdLevel = 1, isIndoor = true),
        Event(46, "Sokak Fotoğrafçılığı", "Eskişehir'in hareketli caddelerinde anı yakalayın.", Category.TECHNOLOGY, 39.7770, 30.5180, "İstiklal Cd.", "Eskişehir", d(7, 13, 0), 100.0, "https://images.unsplash.com/photo-1542038784456-1ea8e935640e?q=80&w=1200&auto=format&fit=crop", listOf("fotoğraf", "sokak"), priceLevel = 1, crowdLevel = 2, isIndoor = false),
        Event(47, "Ebru Gösterisi", "Usta ellerden suyun üzerindeki sanatın sırları.", Category.WORKSHOP, 39.7650, 30.5280, "Tarihi Külliye", "Eskişehir", d(7, 11, 0), 0.0, "https://images.unsplash.com/photo-1513364235450-736868841793?q=80&w=1200&auto=format&fit=crop", listOf("ebru", "sanat"), priceLevel = 0, crowdLevel = 2, isIndoor = true),
        Event(48, "Yerel Korolar Konseri", "Şehrin amatör korolarından Türk Müziği seçkisi.", Category.CONCERT, 39.7745, 30.5210, "Zübeyde Hanım KM", "Eskişehir", d(7, 19, 30), 50.0, "https://images.unsplash.com/photo-1465847899034-d174fc932ce9?q=80&w=1200&auto=format&fit=crop", listOf("koro", "müzik"), priceLevel = 1, crowdLevel = 3, isIndoor = true)
    )
}
