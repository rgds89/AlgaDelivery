package com.algaworks.algadelivery.delivery.tracking.infrastructure.http.client;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/api/v1/couriers")
public interface CourierApiClient {
    @PostExchange("/payout-calculation")
    CourierPayoutResultModel calculate(@RequestBody CourierPayoutCalculationInputDTO courierPayoutCalculationInputDTO);
}
