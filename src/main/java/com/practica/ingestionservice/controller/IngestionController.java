package com.practica.ingestionservice.controller;

import com.practica.ingestionservice.dto.SenadorDto;
import com.practica.ingestionservice.entity.SenadorEntity;
import com.practica.ingestionservice.service.IngestationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class IngestionController {
    private final IngestationService ingestionService;

    public IngestionController(IngestationService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping("/ingest-senado")
    public Mono<ResponseEntity<String>> ingestData() {
        return ingestionService.fetchAndSaveSenadores()
                .collectList()
                .map(results -> ResponseEntity.ok(String.format("Ingesta completada. Actas procesadas: %d", results.size())))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(500).body(String.format("Error durante la ingesta: %s", e.getMessage()))));
    }

    @PostMapping("/senadores")
    public Mono<List<SenadorEntity>> getDataSenadores() {
        return ingestionService.fetchAndSaveSenadores()
                .collectList();

    }
}
