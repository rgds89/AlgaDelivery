package com.algaworks.algadelivery.delivery.tracking.infrastructure.http.client.config;

import com.algaworks.algadelivery.delivery.tracking.infrastructure.http.client.service.CourierApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;

@Configuration
public class CourierApiClientConfig {
    @Value("${courier-management.api.base-url:http://courier-management}")
    private String url;
    @Value("${courier-management.api.connect-timeout:10000}")
    private int connectTimeout;
    @Value("${courier-management.api.read-timeout:200000}")
    private int readTimeout;

    @Bean
    @LoadBalanced
    public RestClient.Builder loadBalacendRestCLientBuilder() {
        return RestClient.builder();
    }

    @Bean
    public CourierApiClient courierApiClient(RestClient.Builder restClientBuilder) {
        RestClient restClient = restClientBuilder.baseUrl(url)
                .requestFactory(genarateClientHttpRequestFactory())
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(CourierApiClient.class);
    }

    private ClientHttpRequestFactory genarateClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setConnectTimeout(Duration.ofMillis(connectTimeout));
        simpleClientHttpRequestFactory.setReadTimeout(Duration.ofMillis(readTimeout));
        return simpleClientHttpRequestFactory;
    }
}
