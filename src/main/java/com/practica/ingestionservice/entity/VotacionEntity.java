package com.practica.ingestionservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("votaciones")
public record VotacionEntity(
        @Id String id,
        @Column("acta_id") String actaId,
        @Column("orden_votacion") int ordenVotacion,
        @Column("titulo_votacion") String tituloVotacion,
        String resultado,
        @Column("votos_afirmativos") int votosAfirmativos,
        @Column("votos_negativos") int votosNegativos,
        @Column("votos_abstenciones") int votosAbstenciones
) {}
