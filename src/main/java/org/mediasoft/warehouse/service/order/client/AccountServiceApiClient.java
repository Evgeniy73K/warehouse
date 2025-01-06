package org.mediasoft.warehouse.service.order.client;

import lombok.RequiredArgsConstructor;
import org.mediasoft.warehouse.configuration.WebClientConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AccountServiceApiClient {

    private final WebClientConfiguration webClientConfiguration;

    @Value("${account-service.methods.get-logins}")
    private String uri;

    public List<Map<String, String>> getLogins(Set<String> logins) {
        return webClientConfiguration
            .accountWebClientConfiguration()
            .post()
            .uri(uri)
            .bodyValue(logins)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<Map<String, String>>>() {
            })
            .block();
    }

}
