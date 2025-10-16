package com.practica.ingestionservice.service;

import com.practica.ingestionservice.dto.SenadorApiResponse;
import com.practica.ingestionservice.dto.SenadorDto;
import com.practica.ingestionservice.entity.SenadorEntity;
import com.practica.ingestionservice.repository.SenadorRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@AllArgsConstructor
public class IngestationService {

    private static final Logger log = LoggerFactory.getLogger(IngestationService.class);
//    private final WebClient webClient = WebClient.create("https://api.argentinadatos.com/v1/senado");
    private final WebClient webClient ;
    private final SenadorMapper senadorMapper;
    private final SenadorRepository senadorRepository;

//    public IngestionService(SenadorMapper senadorMapper, SenadorRepository senadorRepository) {
//        this.senadorMapper = senadorMapper;
//        this.senadorRepository = senadorRepository;
//    }

    public Flux<SenadorEntity> fetchAndSaveSenadores() {
        return webClient.get().retrieve()
                .bodyToFlux(SenadorDto.class)
                .map(senadorMapper::toEntity);
    }

@Transactional
protected Mono<SenadorEntity> saveOrUpdate(SenadorEntity senador) {
        return senadorRepository.save(senador)
                .doOnSuccess(s -> log.info("Senador guardado: {} {}", s.nombre(), s.apellido()))
                .onErrorResume(DataIntegrityViolationException.class, e -> {
                    log.warn("El senador con ID {} ya existe, se omite.", senador.id());
                    return Mono.empty();
                });
    }
}
