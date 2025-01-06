package org.mediasoft.warehouse.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {
    @Value("${currency-service.host}")
    private String currencyServiceHost;
    @Value("${crm-service.host}")
    private String crmServiceHost;
    @Value("${account-service.host}")
    private String accountServiceHost;


    @Bean
    public WebClient currencyWebClientConfiguration() {
        return WebClient.builder()
                .baseUrl(currencyServiceHost)
                .build();
    }

    @Bean
    public WebClient crmWebClientConfiguration() {
        return WebClient.builder()
            .baseUrl(crmServiceHost)
            .build();
    }

    @Bean
    public WebClient accountWebClientConfiguration() {
        return WebClient.builder()
            .baseUrl(accountServiceHost)
            .build();
    }
}