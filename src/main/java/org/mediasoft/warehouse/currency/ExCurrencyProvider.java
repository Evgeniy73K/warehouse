package org.mediasoft.warehouse.currency;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mediasoft.warehouse.controller.dto.ResponseCurrencyDto;
import org.mediasoft.warehouse.currency.client.impl.ReceiveCurrencyApiClientImpl;
import org.mediasoft.warehouse.currency.enums.CurrencyEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExCurrencyProvider {
    @Value(value = "${currency-service.file}")
    private String filePath;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ReceiveCurrencyApiClientImpl receiveCurrencyService;

    @SneakyThrows
    public BigDecimal getCurrRate(CurrencyEnum currency) {
        ResponseCurrencyDto responseCurrencyDto;
        BigDecimal currRate;

        try {
            responseCurrencyDto = receiveCurrencyService.getGetResponseCurrencyDto();
            var file = new File(filePath);
            var writer = new FileWriter(file);
            writer.write(objectMapper.writeValueAsString(responseCurrencyDto));
            writer.close();

        } catch (WebClientException e) {
            log.warn(e.getMessage());
            try (var responseCurrency = new FileInputStream(filePath)) {
                responseCurrencyDto = objectMapper.readValue(responseCurrency, ResponseCurrencyDto.class);
            }
        }
        currRate = switch (currency) {
            case EUR -> responseCurrencyDto.getEUR();
            case USD -> responseCurrencyDto.getUSD();
            case CNY -> responseCurrencyDto.getCNY();
            default -> BigDecimal.ONE;
        };

        return currRate;
    }
}
