package org.mediasoft.warehouse.currency.client.impl;

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
public class ReceiveCurrencyApiClientImpl implements ReceiveCurrencyApiClient {

    @Value(value = "${currency-service.host}")
    private String baseUrl;

    @Value(value = "${currency-service.methods.get-currency}")
    private String endPoint;


    @Cacheable("currency")
    @SneakyThrows
    @Override
    public ResponseCurrencyDto getGetResponseCurrencyDto() {
        log.info("Get currency from {}", baseUrl);

        WebClient client = WebClient.builder()
                .baseUrl(baseUrl)
                .build();

        return client
                .get()
                .uri(endPoint)
                .retrieve()
                .bodyToMono(ResponseCurrencyDto.class)
                .retry(3)
                .block();
    }
}
