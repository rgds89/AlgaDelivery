package com.algaworks.algadelivery.delivery.tracking.infrastructure.http.client.service;

import com.algaworks.algadelivery.delivery.tracking.infrastructure.http.client.model.CourierPayoutCalculationInputDTO;
import com.algaworks.algadelivery.delivery.tracking.infrastructure.http.client.model.CourierPayoutResultDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/api/v1/couriers")
public interface CourierApiClient {
    @PostExchange("/payout-calculation")
    @Retry(name = "Retry_CourierAPI_calculatePayout")
    @CircuitBreaker(name = "CircuitBreaker_CourierApiClient_calculatePayout")
    CourierPayoutResultDTO calculate(@RequestBody CourierPayoutCalculationInputDTO courierPayoutCalculationInputDTO);
}
