package com.algaworks.algadelivery.delivery.tracking.infrastructure.http.client.config;

import com.algaworks.algadelivery.delivery.tracking.infrastructure.http.client.CourierApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class CourierApiClientConfig {
    @Value("${courier-management.api.base-url}")
    private String url;

    @Bean
    public CourierApiClient courierApiClient(RestClient.Builder restClientBuilder) {
        RestClient restClient = restClientBuilder.baseUrl(url).build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(CourierApiClient.class);
    }
}
