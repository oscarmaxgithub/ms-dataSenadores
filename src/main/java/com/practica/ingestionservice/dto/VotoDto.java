package com.practica.ingestionservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Representa una Votación dentro de un Acta.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record VotoDto(
        String nombre,
        String voto,
        String banca
) {}
