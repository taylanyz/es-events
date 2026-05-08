package com.eskisehir.eventapi.service;

import com.eskisehir.eventapi.domain.model.Event;
import com.eskisehir.eventapi.dto.RecommendationResponseDto;
import com.eskisehir.eventapi.dto.SmartRecommendationRequest;
import com.eskisehir.eventapi.repository.EventRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ClaudeRecommendationService {

    private static final Logger log = LoggerFactory.getLogger(ClaudeRecommendationService.class);
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent";

    private final EventRepository eventRepository;
    private final String apiKey;
    private final OkHttpClient httpClient;
    private final Gson gson;

    public ClaudeRecommendationService(
            EventRepository eventRepository,
            @Value("${gemini.api.key}") String apiKey) {
        this.eventRepository = eventRepository;
        this.apiKey = apiKey;
        this.httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();
        this.gson = new Gson();
    }

    public List<RecommendationResponseDto> getSmartRecommendations(SmartRecommendationRequest request) {
        try {
            List<Event> allEvents = eventRepository.findAll();

            if (allEvents.isEmpty()) {
                log.warn("No events found in database");
                return new ArrayList<>();
            }

            String prompt = buildDynamicPrompt(request, allEvents);
            log.debug("Sending prompt to Gemini (first 300 chars): {}",
                prompt.substring(0, Math.min(300, prompt.length())));

            String response = callGeminiAPI(prompt);
            log.info("Raw Gemini Response:\n{}", response);

            List<RecommendationResponseDto> recommendations = parseGeminiResponse(response, allEvents);
            log.info("Got {} recommendations from Gemini", recommendations.size());

            return recommendations;

        } catch (Exception e) {
            log.error("Error getting Gemini recommendations: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    private String callGeminiAPI(String prompt) throws Exception {
        JsonObject requestBody = new JsonObject();
        JsonArray contents = new JsonArray();
        JsonObject content = new JsonObject();
        JsonArray parts = new JsonArray();
        JsonObject part = new JsonObject();

        part.addProperty("text", prompt);
        parts.add(part);
        content.add("parts", parts);
        contents.add(content);
        requestBody.add("contents", contents);

        String jsonBody = gson.toJson(requestBody);
        log.debug("Request to Gemini API: {}", jsonBody.substring(0, Math.min(200, jsonBody.length())));

        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));

        String url = GEMINI_API_URL + "?key=" + apiKey;
        Request request = new Request.Builder()
            .url(url)
            .post(body)
            .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                log.error("Gemini API error: {} - {}", response.code(), errorBody);
                throw new RuntimeException("Gemini API failed with code " + response.code());
            }

            String responseBody = response.body().string();
            log.info("Gemini API full response: {}", responseBody);

            JsonObject responseJson = gson.fromJson(responseBody, JsonObject.class);

            if (responseJson.has("candidates") && responseJson.getAsJsonArray("candidates").size() > 0) {
                JsonObject candidate = responseJson.getAsJsonArray("candidates").get(0).getAsJsonObject();
                JsonObject contentObj = candidate.getAsJsonObject("content");
                JsonArray partsArray = contentObj.getAsJsonArray("parts");

                if (partsArray.size() > 0) {
                    String text = partsArray.get(0).getAsJsonObject().get("text").getAsString();
                    log.info("Raw Gemini Response:\n{}", text);
                    return text;
                }
            }

            log.error("Unexpected response format: {}", responseBody);
            throw new RuntimeException("Unexpected Gemini API response format");
        }
    }

    private String buildDynamicPrompt(SmartRecommendationRequest request, List<Event> events) {
        StringBuilder criteria = new StringBuilder();

        if (request.getMaxPrice() != null) {
            criteria.append(String.format("- Max fiyat: %.0f₺\n", request.getMaxPrice()));
        }
        if (request.getMinPrice() != null) {
            criteria.append(String.format("- Min fiyat: %.0f₺\n", request.getMinPrice()));
        }
        if (request.getMinDuration() != null || request.getMaxDuration() != null) {
            criteria.append(String.format("- Süre: %d-%d dakika\n",
                request.getMinDuration() != null ? request.getMinDuration() : 0,
                request.getMaxDuration() != null ? request.getMaxDuration() : 480));
        }
        if (request.getEnvironmentType() != null) {
            criteria.append(String.format("- Ortam: %s\n", request.getEnvironmentType()));
        }
        if (request.getIsIndoor() != null) {
            criteria.append(String.format("- %s hava\n", request.getIsIndoor() ? "Kapalı" : "Açık"));
        }
        if (request.getCrowdSize() != null) {
            criteria.append(String.format("- Kalabalık seviyesi: %s\n", request.getCrowdSize()));
        }
        if (request.getActivityLevel() != null) {
            criteria.append(String.format("- Aktivite seviyesi: %s\n", request.getActivityLevel()));
        }
        if (request.getDifficultyLevel() != null) {
            criteria.append(String.format("- Zorluk seviyesi: %s\n", request.getDifficultyLevel()));
        }
        if (request.getSocialAspect() != null) {
            criteria.append(String.format("- Sosyal aspekt: %s\n", request.getSocialAspect()));
        }
        if (request.getGroupSize() != null) {
            criteria.append(String.format("- Grup boyutu: %s\n", request.getGroupSize()));
        }
        if (request.getAgeGroup() != null) {
            criteria.append(String.format("- Yaş grubu: %s\n", request.getAgeGroup()));
        }
        if (request.getCulturalValue() != null) {
            criteria.append(String.format("- Kültürel değer: %s\n", request.getCulturalValue()));
        }
        if (request.getBestTimeOfDay() != null) {
            criteria.append(String.format("- Zaman: %s\n", request.getBestTimeOfDay()));
        }
        if (request.getWeatherDependent() != null) {
            criteria.append(String.format("- %s bağımlı etkinlikler\n", request.getWeatherDependent() ? "Hava" : "Hava-bağımlı DEĞİL"));
        }
        if (request.getRequireWheelchair() != null && request.getRequireWheelchair()) {
            criteria.append("- TEKERLEKLİ SANDALYE ERİŞİMİ ZORUNLU\n");
        }
        if (request.getRequireParking() != null && request.getRequireParking()) {
            criteria.append("- OTOPARK ZORUNLU\n");
        }
        if (request.getRequireTransport() != null && request.getRequireTransport()) {
            criteria.append("- TOPLU TAŞIMA ZORUNLU\n");
        }
        if (request.getRequirePhotography() != null && request.getRequirePhotography()) {
            criteria.append("- FOTOĞRAF ÇEKMEK ZORUNLU\n");
        }
        if (request.getRequireFood() != null && request.getRequireFood()) {
            criteria.append("- YEMEK/İÇECEK ZORUNLU\n");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== ETKİNLİK KÜMELEME SİSTEMİ ===\n\n");
        sb.append("Aşağıdaki etkinlikler verilmiştir:\n\n");

        for (Event event : events) {
            sb.append(String.format(
                    "ID: %d | **%s** [%s]\n" +
                    "  Fiyat: %.0f₺ | Süre: %d min | Yer: %s\n" +
                    "  Açıklama: %s\n" +
                    "  Ortam: %s | Zorluk: %s | Aktivite: %s | Sosyal: %s | Grup: %s\n" +
                    "  Yaş: %s | Kültür: %s | Zaman: %s | Kalabalık: %s | Kapalı: %s\n" +
                    "  Hava-bağımlı: %s | Erişilebilirlik: WC=%s, Park=%s, Ulaş=%s, Foto=%s, Yemek=%s\n" +
                    "  Etiketler: %s\n\n",
                    event.getId(),
                    event.getName(),
                    event.getCategory().toString(),
                    event.getPrice(),
                    event.getDuration(),
                    event.getVenue(),
                    event.getDescription(),
                    event.getEnvironmentType(),
                    event.getDifficultyLevel(),
                    event.getActivityLevel(),
                    event.getSocialAspect(),
                    event.getGroupSizeType(),
                    event.getAgeGroup() != null ? event.getAgeGroup() : "Hepsi",
                    event.getCulturalValue() != null ? event.getCulturalValue() : "?",
                    event.getBestTimeOfDay() != null ? event.getBestTimeOfDay() : "?",
                    event.getCrowdSize() != null ? event.getCrowdSize() : "?",
                    event.getIsIndoor() ? "Evet" : "Hayır",
                    event.getWeatherDependent() ? "Evet" : "Hayır",
                    event.getIsWheelchairAccessible() ? "✅" : "❌",
                    event.getHasParking() ? "✅" : "❌",
                    event.getHasPublicTransport() ? "✅" : "❌",
                    event.getAllowsPhotography() ? "✅" : "❌",
                    event.getHasFoodDrink() ? "✅" : "❌",
                    event.getTags() != null ? String.join(", ", event.getTags()) : "Yok"
            ));
        }

        sb.append("=== KULLANICI KRİTERLERİ ===\n");
        if (criteria.length() > 0) {
            sb.append(criteria);
        } else {
            sb.append("- Belirli kriter yok (tüm etkinlikler gözönünde bulundurulacak)\n");
        }

        sb.append(String.format(
                "\n=== GÖREV ===\n" +
                "Yukarıdaki etkinlikler içinden bu kriterlere en uygun %d etkinliği seç.\n\n" +
                "=== PUANLAMA ===\n" +
                "1. ZORUNLU KRİTERLER: Erişilebilirlik, otopark vb ZORUNLU olarak işaretlenmiş olan kriterleri kontrol et.\n" +
                "   Zorunlu bir kriter karşılanmazsa, o etkinlik 0 puan alır.\n\n" +
                "2. SEÇENEKLİ KRİTERLER: Diğer tüm kriterler için puanlama yap:\n" +
                "   - Tüm kriterleri karşılarsa: 90-100 puan\n" +
                "   - Çoğunu karşılarsa: 70-89 puan\n" +
                "   - Yarısını karşılarsa: 50-69 puan\n" +
                "   - Azını karşılarsa: 20-49 puan\n" +
                "   - Neredeyse hiç karşılamazsa: 0-19 puan\n\n" +
                "3. HİÇBİR KRİTER SEÇİLMEDİYSE: Tüm etkinlikleri eşit puanla (50-60 puan)\n\n" +
                "=== ÇIKTI FORMATI ===\n" +
                "Şu formatta cevap ver (başka hiçbir şey yazma):\n" +
                "1. [Etkinlik Adı] | Skor: XX | Neden: [Kısaca neden uygun - 20 kelime max]\n" +
                "2. [Etkinlik Adı] | Skor: XX | Neden: [Açıklama]\n" +
                "...\n",
                request.getLimit() != null ? request.getLimit() : 5
        ));

        return sb.toString();
    }

    private List<RecommendationResponseDto> parseGeminiResponse(String response, List<Event> allEvents) {
        List<RecommendationResponseDto> recommendations = new ArrayList<>();
        Map<String, Event> eventMap = allEvents.stream()
                .collect(Collectors.toMap(Event::getName, e -> e));

        String[] lines = response.split("\n");

        for (String line : lines) {
            if (line.trim().isEmpty() || !line.contains("|")) continue;

            try {
                String[] parts = line.split("\\|");
                if (parts.length < 2) continue;

                String namePart = parts[0].trim();
                String eventName = namePart.replaceAll("^\\d+\\.\\s*\\[|\\]$", "").trim();
                eventName = eventName.replaceAll("\\[|\\]", "").trim();

                String scoreStr = parts[1].trim();
                double score = extractScore(scoreStr);

                String explanation = parts.length > 2 ? parts[2].replaceAll("Neden:\\s*", "").trim() : "Uygun eşleşme";

                Event event = findEventByName(eventName, eventMap);
                if (event != null) {
                    RecommendationResponseDto dto = new RecommendationResponseDto(
                            event.getId(),
                            event.getName(),
                            score / 100.0,
                            score / 100.0,
                            0.0,
                            "Bu etkinlik sana uygun çünkü: " + explanation,
                            event.getTags() != null ? event.getTags() : new ArrayList<>()
                    );
                    recommendations.add(dto);
                }
            } catch (Exception e) {
                log.debug("Failed to parse line: {}", line, e);
            }
        }

        return recommendations.stream()
                .sorted((a, b) -> Double.compare(b.getFinalScore(), a.getFinalScore()))
                .collect(Collectors.toList());
    }

    private double extractScore(String scoreStr) {
        try {
            String[] parts = scoreStr.split(":");
            if (parts.length > 1) {
                return Double.parseDouble(parts[1].trim());
            }
        } catch (Exception e) {
            log.debug("Failed to extract score from: {}", scoreStr);
        }
        return 50.0;
    }

    private Event findEventByName(String name, Map<String, Event> eventMap) {
        if (eventMap.containsKey(name)) {
            return eventMap.get(name);
        }

        return eventMap.values().stream()
                .filter(e -> e.getName().toLowerCase().contains(name.toLowerCase()) ||
                        name.toLowerCase().contains(e.getName().toLowerCase()))
                .findFirst()
                .orElse(null);
    }
}
