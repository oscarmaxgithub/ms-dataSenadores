package com.practica.ingestionservice.service;

import com.practica.ingestionservice.dto.SenadorDto;
import com.practica.ingestionservice.entity.SenadorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SenadorMapper {

    @Mapping(source = "periodoLegal.inicio", target = "mandatoInicio")
    @Mapping(source = "periodoLegal.fin", target = "mandatoFin")
    SenadorEntity toEntity(SenadorDto dto);

}
