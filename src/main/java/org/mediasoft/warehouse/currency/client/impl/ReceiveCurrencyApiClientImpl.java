package org.mediasoft.warehouse.currency.client.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mediasoft.warehouse.controller.dto.ResponseCurrencyDto;
import org.mediasoft.warehouse.currency.client.ReceiveCurrencyApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReceiveCurrencyApiClientImpl implements ReceiveCurrencyApiClient {

    private final WebClient webClient;

    @Value("${currency-service.methods.get-currency}")
    private String endPoint;


    @Cacheable("currency")
    @SneakyThrows
    @Override
    public ResponseCurrencyDto getGetResponseCurrencyDto() {
        log.info("!!!!!! Получаем курс из сервиса !!! Get currency from {}", webClient);

        return webClient
                .get()
                .uri(endPoint)
                .retrieve()
                .bodyToMono(ResponseCurrencyDto.class)
                .retry(3)
                .block();
    }
}

