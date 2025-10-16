package com.practica.ingestionservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ActaDto(
        @JsonProperty("actaId") int actaId,
        String titulo,
        String proyecto,
        String descripcion,
        @JsonProperty("quorumTipo") String quorumTipo,
        String fecha,
        String acta,
        String mayoria,
        int miembros,
        int afirmativos,
        int negativos,
        int abstenciones,
        int presentes,
        int ausentes,
        int amn,
        String resultado,
        List<VotoDto> votos,
        List<String> observaciones

) {}
