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
public class CrmServiceApiClient {
    private final WebClientConfiguration webClientConfiguration;

    @Value("${crm-service.methods.get-inn}")
    private String uri;

    public List<Map<String, String>> getInnList(Set<String> logins) {
        return webClientConfiguration
            .crmWebClientConfiguration()
            .post()
            .uri(uri)
            .bodyValue(logins)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<Map<String, String>>>() {
            })
            .block();
    }

}
