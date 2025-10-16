package com.practica.ingestionservice.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webclient(){
        final int bufferSize= 16*1024*1024;
        HttpClient httpClient= HttpClient.create()
                .followRedirect(true);

        return WebClient.builder()
                .baseUrl("https://api.argentinadatos.com/v1/senado/senadores")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer->configurer.defaultCodecs().maxInMemorySize(bufferSize))
                        .build())
                .build();
    }
}
