package com.eskisehir.eventapi.config;

import com.eskisehir.eventapi.domain.model.Category;
import com.eskisehir.eventapi.domain.model.Event;
import com.eskisehir.eventapi.repository.EventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final EventRepository eventRepository;

    public DataSeeder(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (eventRepository.count() == 0) {
            seedEvents();
        }
    }

    private void seedEvents() {
        List<Event> events = new ArrayList<>();

        addEvent(events, "Eskişehir Balon Festivali", Category.FESTIVAL, "Gökyüzü renk renk balonlarla dolacak", 39.7667, 30.5256, "Odunpazarı", 3, 50, Arrays.asList("festival", "açık-hava", "aile"), "kalabalık", "başlangıç", "grup", "hafif", "yeni insanlar", true, true, true, true, true, 240, false, "Çocuk", "Biraz", false, "Sabah", "Çok");
        addEvent(events, "Sanat Galerisi - Modern İzlenimler", Category.EXHIBITION, "Çağdaş sanat eserleri sergisi", 39.7800, 30.5100, "Ankara Caddesi Sanat Merkezi", 5, 25, Arrays.asList("sanat", "sergi", "kültür"), "sessiz", "orta", "her biri", "pasif", "yalnız", true, true, true, false, true, 120, true, "Yetişkin", "Çok", false, "Öğle", "Az");
        addEvent(events, "Rock Konseri - Local Bandlar", Category.CONCERT, "Eskişehir'in yerel rock gruplarından konser", 39.7900, 30.5300, "Live Club", 7, 75, Arrays.asList("müzik", "rock", "live"), "kalabalık", "başlangıç", "grup", "orta", "arkadaş", true, true, true, true, true, 180, true, "Genç", "Biraz", false, "Gece", "Çok");
        addEvent(events, "Workshop - Seramik Sanatı", Category.WORKSHOP, "Seramik sanatı öğrenme atölyesi", 39.7500, 30.5500, "Sanat Atölyesi", 2, 40, Arrays.asList("workshop", "sanat", "eğitim"), "sessiz", "başlangıç", "çift", "hafif", "arkadaş", false, false, true, true, false, 120, true, "Yetişkin", "Biraz", false, "Öğle", "Az");
        addEvent(events, "Yoga ve Meditasyon Sınıfı", Category.SPORTS, "Sabah yoga oturumu ve meditasyon", 39.7600, 30.5000, "Wellness Center", 1, 30, Arrays.asList("spor", "wellness", "yoga"), "sessiz", "başlangıç", "her biri", "hafif", "yalnız", true, false, true, false, false, 60, true, "Yetişkin", "Biraz", false, "Sabah", "Az");

        addEvent(events, "Basket Turnuvası - Erkekler", Category.SPORTS, "Şehir içi basket turnuvası", 39.7700, 30.5400, "Spor Salonu", 4, 100, Arrays.asList("spor", "basketbol", "turnuva"), "kalabalık", "orta", "grup", "yoğun", "yeni insanlar", true, true, true, true, true, 240, true, "Genç", "Hiç", false, "Öğle", "Çok");
        addEvent(events, "Klasik Müzik Konseri", Category.CONCERT, "Piyanist solisti ile Chopin eserleri", 39.7750, 30.5150, "Şehir Tiyatrosu", 6, 120, Arrays.asList("müzik", "klasik", "konser"), "sessiz", "orta", "her biri", "pasif", "yalnız", true, true, true, true, true, 150, true, "Yetişkin", "Çok", false, "Akşam", "Az");
        addEvent(events, "Film Festivali Açılış", Category.CINEMA, "Uluslararası film festivali açılış filmi", 39.7650, 30.5250, "Sinema Sarayı", 8, 35, Arrays.asList("sinema", "festival", "film"), "kalabalık", "başlangıç", "grup", "pasif", "arkadaş", true, true, true, true, true, 180, true, "Yetişkin", "Biraz", false, "Akşam", "Çok");
        addEvent(events, "Tiyatro Oyunu - Modern Draması", Category.THEATER, "Yerel oyuncularla modern tiyatro", 39.7800, 30.5200, "Devlet Tiyatrosu", 9, 60, Arrays.asList("tiyatro", "draması", "gösteri"), "sessiz", "orta", "her biri", "pasif", "yalnız", true, true, true, true, true, 120, true, "Yetişkin", "Çok", false, "Akşam", "Az");
        addEvent(events, "Fotoğrafçılık Atölyesi", Category.WORKSHOP, "Dijital kamera kullanımı", 39.7550, 30.5350, "Sanat Merkezi", 3, 50, Arrays.asList("workshop", "fotoğrafçılık", "eğitim"), "hafif", "başlangıç", "çift", "hafif", "arkadaş", true, false, true, true, false, 180, true, "Genç", "Biraz", false, "Öğle", "Orta");
        addEvent(events, "Yüzme Kursu", Category.SPORTS, "Başlangıç seviyesi yüzme", 39.7500, 30.5450, "Yüzme Havuzu", 5, 45, Arrays.asList("spor", "yüzme", "eğitim"), "hafif", "başlangıç", "grup", "orta", "yeni insanlar", true, true, true, true, false, 90, false, "Çocuk", "Hiç", false, "Öğle", "Orta");
        addEvent(events, "Kuran Tilaveti Yarışması", Category.CONFERENCE, "Ulusal kuran tilaveti yarışması", 39.7800, 30.5350, "İmam Hatip Lisesi", 10, 0, Arrays.asList("kültür", "yarışma", "eğitim"), "sessiz", "ileri", "grup", "hafif", "yeni insanlar", true, true, true, true, true, 240, true, "Yetişkin", "Çok", false, "Sabah", "Orta");
        addEvent(events, "Stand-up Komedi Gecesi", Category.STANDUP, "Ünlü komedyen performansı", 39.7700, 30.5300, "Sanat Merkezı", 11, 85, Arrays.asList("komedi", "eğlence", "gösteri"), "kalabalık", "başlangıç", "grup", "hafif", "arkadaş", true, true, true, true, true, 120, true, "Genç", "Hiç", false, "Gece", "Çok");
        addEvent(events, "Ressam Sergisi - İmpressionizm", Category.EXHIBITION, "Ressam Ahmet'in impressionist tabloları", 39.7720, 30.5180, "Galeri Eskişehir", 12, 20, Arrays.asList("sanat", "resim", "sergi"), "sessiz", "orta", "her biri", "pasif", "yalnız", true, true, true, false, true, 180, true, "Yetişkin", "Çok", false, "Öğle", "Az");
        addEvent(events, "Jazz Gecesi", Category.CONCERT, "Canlı jazz müziği ve meyhane ortamı", 39.7750, 30.5320, "Blue Note Cafe", 13, 95, Arrays.asList("müzik", "jazz", "gece"), "kalabalık", "başlangıç", "grup", "orta", "arkadaş", true, true, true, true, true, 180, true, "Yetişkin", "Biraz", false, "Gece", "Çok");
        addEvent(events, "Satranç Turnuvası", Category.SPORTS, "Şehir satranç şampiyonası", 39.7680, 30.5220, "Satranç Klübü", 14, 60, Arrays.asList("spor", "satranç", "turnuva"), "sessiz", "orta", "çift", "pasif", "yeni insanlar", true, false, true, true, false, 300, true, "Yetişkin", "Hiç", false, "Öğle", "Az");
        addEvent(events, "Spor Dansı Gösterisi", Category.SPORTS, "Profesyonel dansçılarla spor dansı", 39.7600, 30.5400, "Dul Merkez Kültür", 15, 70, Arrays.asList("spor", "dans", "gösteri"), "kalabalık", "başlangıç", "grup", "yoğun", "arkadaş", true, true, true, true, true, 150, true, "Genç", "Hiç", false, "Akşam", "Çok");
        addEvent(events, "Deizm Sohbeti", Category.CONFERENCE, "Felsefe ve inanç üzerine tartışma", 39.7550, 30.5180, "Kültür Merkezi", 16, 15, Arrays.asList("felsefe", "konferans", "eğitim"), "sessiz", "ileri", "grup", "pasif", "yeni insanlar", true, false, true, true, true, 120, true, "Yetişkin", "Çok", false, "Akşam", "Az");
        addEvent(events, "Sushi Yapımı Kursu", Category.WORKSHOP, "Japon mutfağı ve sushi teknikleri", 39.7620, 30.5280, "Mutfak Okulu", 17, 85, Arrays.asList("workshop", "mutfak", "eğitim"), "karma", "başlangıç", "çift", "hafif", "arkadaş", false, false, true, true, true, 120, true, "Yetişkin", "Biraz", false, "Öğle", "Az");
        addEvent(events, "Doğa Yürüyüşü", Category.SPORTS, "Sultaniye çayı kenarında doğa turu", 39.7800, 30.5100, "Sultaniye Çayı", 18, 20, Arrays.asList("spor", "doğa", "açık-hava"), "hafif", "başlangıç", "grup", "orta", "yeni insanlar", false, false, true, true, true, 180, false, "Yetişkin", "Biraz", true, "Sabah", "Orta");
        addEvent(events, "Markaz Şiir Gecesi", Category.THEATER, "Şairlardan live şiir dinletisi", 39.7700, 30.5350, "Kütüphane Salon", 19, 25, Arrays.asList("sanat", "şiir", "edebiyat"), "sessiz", "orta", "her biri", "pasif", "yalnız", true, true, true, true, true, 150, true, "Yetişkin", "Çok", false, "Akşam", "Az");

        eventRepository.saveAll(events);
    }

    private void addEvent(List<Event> events, String name, Category category, String description, double lat, double lon,
                         String venue, int daysFromNow, double price, List<String> tags, String envType, String difficulty,
                         String groupSize, String activityLevel, String socialAspect, boolean wheelchair, boolean parking,
                         boolean transport, boolean photography, boolean foodDrink, int duration,
                         boolean isIndoor, String ageGroup, String culturalValue, boolean weatherDependent,
                         String bestTimeOfDay, String crowdSize) {
        Event event = new Event();
        event.setName(name);
        event.setCategory(category);
        event.setDescription(description);
        event.setLatitude(lat);
        event.setLongitude(lon);
        event.setVenue(venue);
        event.setDate(LocalDateTime.now().plusDays(daysFromNow));
        event.setPrice(price);
        event.setTags(tags);
        event.setEnvironmentType(envType);
        event.setDifficultyLevel(difficulty);
        event.setGroupSizeType(groupSize);
        event.setActivityLevel(activityLevel);
        event.setSocialAspect(socialAspect);
        event.setIsWheelchairAccessible(wheelchair);
        event.setHasParking(parking);
        event.setHasPublicTransport(transport);
        event.setAllowsPhotography(photography);
        event.setHasFoodDrink(foodDrink);
        event.setLanguage("TR");
        event.setDuration(duration);
        event.setIsIndoor(isIndoor);
        event.setAgeGroup(ageGroup);
        event.setCulturalValue(culturalValue);
        event.setWeatherDependent(weatherDependent);
        event.setBestTimeOfDay(bestTimeOfDay);
        event.setCrowdSize(crowdSize);
        events.add(event);
    }
}
