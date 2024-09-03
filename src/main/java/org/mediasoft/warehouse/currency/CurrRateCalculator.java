package org.mediasoft.warehouse.currency;

import lombok.experimental.UtilityClass;
import org.mediasoft.warehouse.controller.dto.ResponseProductDto;


import java.math.RoundingMode;

import static org.mediasoft.warehouse.currency.CurrencyFilter.currRate;
import static org.mediasoft.warehouse.currency.CurrencyFilter.currencyEnum;

@UtilityClass
public class CurrRateCalculator {
    public ResponseProductDto setCurrency(ResponseProductDto responseProductDto) {
        responseProductDto.setPrice(responseProductDto.getPrice().multiply(currRate).setScale(2, RoundingMode.HALF_UP));
        responseProductDto.setCurrency(currencyEnum);
        return responseProductDto;
    }
}
