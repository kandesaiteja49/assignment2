package com.airtribe.meditrack.services;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class EmbeddingService {

    private final EmbeddingModel embeddingModel;

    // Add @Qualifier here to tell Spring exactly which bean to use
    public EmbeddingService(@Qualifier("ollamaEmbeddingModel") EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    public float[] getEmbedding(String text) {
        return embeddingModel.embed(text);
    }
}