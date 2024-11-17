package org.mediasoft.warehouse.currency;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.mediasoft.warehouse.controller.dto.ResponseProductDto;
import org.mediasoft.warehouse.currency.enums.CurrencyEnum;
import org.springframework.stereotype.Component;


import java.math.RoundingMode;


@RequiredArgsConstructor
@Component
public class CurrRateCalculator {
    private final HttpSession httpSession;
    private final CurrRateProvider currRateProvider;



    public ResponseProductDto setCurrency(ResponseProductDto responseProductDto) {
        var currencyEnum = CurrencyEnum.fromValue(httpSession.getAttribute("currency").toString());
        var currRate = currRateProvider.getCurrRate(CurrencyEnum.fromValue(httpSession.getAttribute("currency").toString()));

        responseProductDto.setPrice(responseProductDto.getPrice().multiply(currRate).setScale(2, RoundingMode.HALF_UP));
        responseProductDto.setCurrency(currencyEnum);
        return responseProductDto;
    }
}