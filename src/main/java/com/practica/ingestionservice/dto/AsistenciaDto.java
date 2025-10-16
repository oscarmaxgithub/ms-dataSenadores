package com.practica.ingestionservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Representa el bloque de asistencia.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record AsistenciaDto(
        int presentes,
        int ausentes,
        int licencias
) {}
