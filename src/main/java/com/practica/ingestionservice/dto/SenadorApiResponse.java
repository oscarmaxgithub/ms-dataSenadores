package com.practica.ingestionservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SenadorApiResponse(List<SenadorDto> data) {}
