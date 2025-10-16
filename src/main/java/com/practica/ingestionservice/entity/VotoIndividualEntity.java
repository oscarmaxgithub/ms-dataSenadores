package com.practica.ingestionservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("votos_individuales")
public record VotoIndividualEntity(
        @Id Integer id, // Autogenerado por la DB
        @Column("votacion_id") String votacionId,
        @Column("senador_id") String senadorId,
        String voto
) {}
