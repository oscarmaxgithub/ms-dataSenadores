package com.practica.ingestionservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Representa el conteo de votos totales.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record VotosTotalesDto(
        int afirmativos,
        int negativos,
        int abstenciones
) {}
