package com.Literatura.Literatura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

@Service
public class ConvierteDatos implements IConvierteDatos {
    private final ObjectMapper mapper;

    public ConvierteDatos() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule()); // Registrar el módulo JavaTimeModule
    }

    @Override
    public <T> T obtenerDatos(String json, Class<T> clase) {
        try {
            return mapper.readValue(json, clase);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
