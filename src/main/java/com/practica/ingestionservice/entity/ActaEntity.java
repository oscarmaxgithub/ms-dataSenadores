package com.practica.ingestionservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;

@Table("actas")
public record ActaEntity(
        @Id String id,
        OffsetDateTime fecha,
        String titulo,
        int periodo,
        String tipo,
        String sumario,
        @Column("version_taquigrafica_url") String versionTaquigraficaUrl,
        @Column("asistencia_presentes") int asistenciaPresentes,
        @Column("asistencia_ausentes") int asistenciaAusentes,
        @Column("asistencia_licencias") int asistenciaLicencias
) {}
