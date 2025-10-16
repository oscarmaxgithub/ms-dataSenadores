package com.practica.ingestionservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("laureates")
public record Laureate(@Id Integer id, String knownName, String gender, String birthDate) {}
