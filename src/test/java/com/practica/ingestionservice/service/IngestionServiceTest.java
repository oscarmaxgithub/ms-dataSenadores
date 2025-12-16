package com.practica.ingestionservice.service;

import com.practica.ingestionservice.dto.MandatoDto;
import com.practica.ingestionservice.dto.SenadorDto;
import com.practica.ingestionservice.entity.SenadorEntity;
import com.practica.ingestionservice.repository.SenadorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class IngestionServiceTest {

    @InjectMocks
    private  IngestionService ingestionService;

    @Mock
    private WebClient webClient;

    @Mock
    private SenadorMapper senadorMapper;

    @Mock
    private SenadorRepository senadorRepository;



    @Test
    void testFetchSenadores_whenApiReturnsResults_shouldReturnEntities() {
        // --- ARRANGE ---
        MandatoDto mandato = new MandatoDto("2020-01-01", "2024-12-31");
        SenadorDto dto1 = new SenadorDto("1", "First1", "Last1", "B1", "P1", "Pa1", "e1@t.com", "123", mandato);
        SenadorDto dto2 = new SenadorDto("2", "First2", "Last2", "B2", "P2", "Pa2", "e2@t.com", "456", mandato);

        SenadorEntity entity1 = new SenadorEntity("1", "First1", "Last1", "B1", "P1", "Pa1", "e1@t.com", "123", LocalDate.now(), LocalDate.now());
        SenadorEntity entity2 = new SenadorEntity("2", "First2", "Last2", "B2", "P2", "Pa2", "e2@t.com", "456", LocalDate.now(), LocalDate.now());

        // --- MOCKS DE WEBCLIENT (CORREGIDO) ---
        // Usamos 'var' o Raw Types para evitar problemas de genéricos con Mockito
        var requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        var requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        var responseSpec = mock(WebClient.ResponseSpec.class);

        // 1. Al llamar a .get(), devolvemos el UriSpec
        when(webClient.get()).thenReturn(requestHeadersUriSpec);

        // 2. IMPORTANTE: Configuramos .retrieve() en AMBOS objetos para evitar el NULL
        // Caso A: Si tu código hace .uri(...) -> .retrieve()
//        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
//        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        // Caso B: Si tu código hace .get().retrieve() directamente (causa probable de tu error)
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);

        // 3. Respuesta final
        when(responseSpec.bodyToFlux(SenadorDto.class)).thenReturn(Flux.just(dto1, dto2));

        // --- MOCKS DEL MAPPER ---
        when(senadorMapper.toEntity(dto1)).thenReturn(entity1);
        when(senadorMapper.toEntity(dto2)).thenReturn(entity2);

        // --- ACT ---
        Flux<SenadorEntity> result = ingestionService.fetchAndSaveSenadores();

        // --- ASSERT ---
        StepVerifier.create(result)
                .expectNext(entity1)
                .expectNext(entity2)
                .verifyComplete();
    }

    @Test
    void testFetchAndSaveSenadores_whenApiReturnsEmpty_shouldReturnEmptyFlux() {
        // Arrange
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenReturn(Mockito.mock(WebClient.RequestHeadersUriSpec.class));
        when(webClient.get().retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(SenadorDto.class)).thenReturn(Flux.empty());

        // Act
        Flux<SenadorEntity> result = ingestionService.fetchAndSaveSenadores();

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        Mockito.verifyNoInteractions(senadorMapper);
        Mockito.verifyNoInteractions(senadorRepository);
    }

//    @Test
//    void testFetchAndSaveSenadores_whenSaveThrowsError_shouldHandleGracefully() {
//        // Arrange
//        MandatoDto mandato = new MandatoDto("2020-01-01", "2024-12-31");
//        SenadorDto dto1 = new SenadorDto("1", "FirstName1", "LastName1", "Block1", "Province1", "Party1", "email1@test.com", "123456", mandato);
//        SenadorEntity entity1 = new SenadorEntity("1", "FirstName1", "LastName1", "Block1", "Province1", "Party1", "email1@test.com", "123456", LocalDate.of(2020, 1, 1), LocalDate.of(2024, 12, 31));
//
//        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);
//
//        Mockito.when(webClient.get()).thenReturn(Mockito.mock(WebClient.RequestHeadersUriSpec.class));
//        Mockito.when(webClient.get().retrieve()).thenReturn(responseSpec);
//        Mockito.when(responseSpec.bodyToFlux(SenadorDto.class)).thenReturn(Flux.just(dto1));
//        Mockito.when(senadorMapper.toEntity(dto1)).thenReturn(entity1);
//
//        Mockito.when(senadorRepository.save(entity1)).thenReturn(Mono.error(new RuntimeException("Database error")));
//
//        // Act
//        Flux<SenadorEntity> result = ingestionService.fetchAndSaveSenadores();
//
//        // Assert
//        StepVerifier.create(result)
//                .expectError(RuntimeException.class)
//                .verify();
//
//        Mockito.verify(senadorRepository, Mockito.times(0)).save(entity1);
//    }
}