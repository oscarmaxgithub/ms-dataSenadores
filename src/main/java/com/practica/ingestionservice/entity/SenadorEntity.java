package com.practica.ingestionservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("senadores")
public record SenadorEntity(
        @Id String id,
        String nombre,
        String apellido,
        String bloque,
        String provincia,
        String partido,
        String email,
        String telefono,
        @Column("mandato_inicio") LocalDate mandatoInicio,
        @Column("mandato_fin") LocalDate mandatoFin
) {}
