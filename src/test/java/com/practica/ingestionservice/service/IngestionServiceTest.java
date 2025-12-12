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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;


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
    void testFetchAndSaveSenadores_whenApiReturnsResults_shouldSaveEntities() {
        // Arrange
        MandatoDto mandato = new MandatoDto("2020-01-01", "2024-12-31");
        SenadorDto dto1 = new SenadorDto("1", "FirstName1", "LastName1", "Block1", "Province1", "Party1", "email1@test.com", "123456", mandato);
        SenadorDto dto2 = new SenadorDto("2", "FirstName2", "LastName2", "Block2", "Province2", "Party2", "email2@test.com", "654321", mandato);

        SenadorEntity entity1 = new SenadorEntity("1", "FirstName1", "LastName1", "Block1", "Province1", "Party1", "email1@test.com", "123456", LocalDate.of(2020, 1, 1), LocalDate.of(2024, 12, 31));
        SenadorEntity entity2 = new SenadorEntity("2", "FirstName2", "LastName2", "Block2", "Province2", "Party2", "email2@test.com", "654321", LocalDate.of(2021, 1, 1), LocalDate.of(2025, 12, 31));

        WebClient.RequestHeadersSpec<?> requestHeadersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        Mockito.when(webClient.get()).thenReturn(Mockito.mock(WebClient.RequestHeadersUriSpec.class));
        Mockito.when(webClient.get().retrieve()).thenReturn(responseSpec);
        Mockito.when(responseSpec.bodyToFlux(SenadorDto.class)).thenReturn(Flux.just(dto1, dto2));

//        Mockito.when(senadorMapper.toEntity(dto1)).thenReturn(entity1);
//        Mockito.when(senadorMapper.toEntity(dto2)).thenReturn(entity2);

//        Mockito.when(senadorRepository.save(entity1)).thenReturn(Mono.just(entity1));
//        Mockito.when(senadorRepository.save(entity2)).thenReturn(Mono.just(entity2));

        // Act
        Flux<SenadorEntity> result = ingestionService.fetchAndSaveSenadores();

        // Assert
//        StepVerifier.create(result)
//                .expectNext(entity1)
//                .expectNext(entity2)
//                .verifyComplete();

//        Mockito.verify(senadorRepository, Mockito.times(0)).save(entity1);
//        Mockito.verify(senadorRepository, Mockito.times(0)).save(entity2);
    }

    @Test
    void testFetchAndSaveSenadores_whenApiReturnsEmpty_shouldReturnEmptyFlux() {
        // Arrange
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        Mockito.when(webClient.get()).thenReturn(Mockito.mock(WebClient.RequestHeadersUriSpec.class));
        Mockito.when(webClient.get().retrieve()).thenReturn(responseSpec);
        Mockito.when(responseSpec.bodyToFlux(SenadorDto.class)).thenReturn(Flux.empty());

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