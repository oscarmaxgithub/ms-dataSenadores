package com.practica.ingestionservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Representa el voto de un Senador individual.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record VotoPorSenadorDto(
        String senadorId,
        String nombre,
        String bloque,
        String provincia,
        String voto
) {}
