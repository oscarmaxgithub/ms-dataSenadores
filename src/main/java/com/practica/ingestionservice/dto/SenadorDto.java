package com.practica.ingestionservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SenadorDto(
        String id,
        String nombre,
        String apellido,
        String bloque,
        String provincia,
        String partido,
        String email,
        String telefono,
        MandatoDto periodoLegal
) {}
