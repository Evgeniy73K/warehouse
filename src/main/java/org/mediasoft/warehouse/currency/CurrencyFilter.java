package org.mediasoft.warehouse.currency;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.mediasoft.warehouse.currency.enums.CurrencyEnum;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CurrencyFilter extends OncePerRequestFilter {
    private final CurrencyProvider currencyProvider;
    public static BigDecimal currRate;
    public static CurrencyEnum currencyEnum;
    private final HttpSession httpSession;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getHeader("currency") == null && httpSession.getAttribute("currency") == null) {
            httpSession.setAttribute("currency", CurrencyEnum.RUB);

        } else if(request.getHeader("currency") != null && httpSession.getAttribute("currency") == null) {
            httpSession.setAttribute("currency", CurrencyEnum.fromValue(request.getHeader("currency")));
        } else if(request.getHeader("currency") != null && httpSession.getAttribute("currency") != request.getHeader("currency")) {
            httpSession.setAttribute("currency", CurrencyEnum.fromValue(request.getHeader("currency")));
        }

        currRate = currencyProvider.getCurrRate(CurrencyEnum.fromValue(httpSession.getAttribute("currency").toString()));
        currencyEnum = CurrencyEnum.fromValue(httpSession.getAttribute("currency").toString());
        filterChain.doFilter(request, response);
    }
}