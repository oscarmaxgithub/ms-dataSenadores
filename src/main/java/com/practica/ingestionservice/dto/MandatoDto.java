package com.practica.ingestionservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MandatoDto(String inicio, String fin) {}
