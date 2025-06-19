package com.unialfa.hackathon.model.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Converter
public class RespostasConverter implements AttributeConverter<Map<Integer, String>, String> {

    private static final Logger logger = LoggerFactory.getLogger(RespostasConverter.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<Integer, String> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "{}"; // Retorna um JSON vazio em vez de nulo para consistência
        }
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            logger.error("Erro ao converter mapa para JSON", e);
            throw new IllegalArgumentException("Erro ao serializar o mapa de respostas.", e);
        }
    }

    @Override
    public Map<Integer, String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return Collections.emptyMap();
        }
        try {
            return objectMapper.readValue(dbData, new TypeReference<>() {});
        } catch (IOException e) {
            logger.error("Erro ao converter JSON para mapa", e);
            throw new IllegalArgumentException("Erro ao desserializar o mapa de respostas.", e);
        }
    }
}
