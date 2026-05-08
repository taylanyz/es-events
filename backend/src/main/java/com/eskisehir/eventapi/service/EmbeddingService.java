package com.eskisehir.eventapi.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class EmbeddingService {

    private final Map<String, float[]> embeddingCache = new HashMap<>();

    public float[] getEmbedding(String text) {
        // Return cached embedding if available
        if (embeddingCache.containsKey(text)) {
            return embeddingCache.get(text);
        }

        // Generate embedding (simple TF-IDF like approach for MVP)
        float[] embedding = generateEmbedding(text);
        embeddingCache.put(text, embedding);
        return embedding;
    }

    private float[] generateEmbedding(String text) {
        // Tokenize and create simple embedding
        String[] tokens = tokenize(text.toLowerCase());
        float[] embedding = new float[300];  // 300D embedding

        for (String token : tokens) {
            int hash = Math.abs(token.hashCode());
            for (int i = 0; i < embedding.length; i++) {
                embedding[i] += (float) Math.sin(hash + i) / tokens.length;
            }
        }

        // Normalize
        return normalize(embedding);
    }

    public double cosineSimilarity(float[] vec1, float[] vec2) {
        if (vec1.length != vec2.length) {
            throw new IllegalArgumentException("Vectors must have same dimension");
        }

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (int i = 0; i < vec1.length; i++) {
            dotProduct += vec1[i] * vec2[i];
            norm1 += vec1[i] * vec1[i];
            norm2 += vec2[i] * vec2[i];
        }

        if (norm1 == 0.0 || norm2 == 0.0) {
            return 0.0;
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    private String[] tokenize(String text) {
        // Simple tokenization: split on spaces and punctuation
        return text.replaceAll("[^a-zçğıöşüñ\\s]", "")
                   .split("\\s+");
    }

    private float[] normalize(float[] vector) {
        double norm = 0.0;
        for (float v : vector) {
            norm += v * v;
        }
        norm = Math.sqrt(norm);

        if (norm == 0.0) {
            return vector;
        }

        float[] normalized = new float[vector.length];
        for (int i = 0; i < vector.length; i++) {
            normalized[i] = (float) (vector[i] / norm);
        }
        return normalized;
    }

    public void clearCache() {
        embeddingCache.clear();
    }
}
