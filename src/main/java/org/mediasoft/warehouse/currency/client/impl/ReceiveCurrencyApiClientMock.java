package org.mediasoft.warehouse.currency.client.impl;

import org.jeasy.random.EasyRandom;
import org.mediasoft.warehouse.controller.dto.ResponseCurrencyDto;
import org.mediasoft.warehouse.currency.client.ReceiveCurrencyApiClient;
import org.springframework.stereotype.Service;

@Service
public class ReceiveCurrencyApiClientMock implements ReceiveCurrencyApiClient {
    private final EasyRandom easyRandom = new EasyRandom();
    @Override
    public ResponseCurrencyDto getGetResponseCurrencyDto() {
        return easyRandom.nextObject(ResponseCurrencyDto.class);
    }
}
