package com.practica.ingestionservice.entity;

//import org.springframework.data.annotation.Id;
//import org.springframework.data.relational.core.mapping.Column;
//import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;


public record SenadorEntity(
//        @Id
        String id,
        String nombre,
        String apellido,
        String bloque,
        String provincia,
        String partido,
        String email,
        String telefono,
        LocalDate mandatoInicio,
        LocalDate mandatoFin
) {}
