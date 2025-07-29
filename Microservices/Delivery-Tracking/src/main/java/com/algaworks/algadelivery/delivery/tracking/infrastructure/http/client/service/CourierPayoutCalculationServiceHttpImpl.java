package com.algaworks.algadelivery.delivery.tracking.infrastructure.http.client.service;

import com.algaworks.algadelivery.delivery.tracking.domain.service.CourierPayoutCalculationService;
import com.algaworks.algadelivery.delivery.tracking.infrastructure.http.client.exception.BadGatewayException;
import com.algaworks.algadelivery.delivery.tracking.infrastructure.http.client.exception.GatewayTimeoutException;
import com.algaworks.algadelivery.delivery.tracking.infrastructure.http.client.model.CourierPayoutCalculationInputDTO;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CourierPayoutCalculationServiceHttpImpl implements CourierPayoutCalculationService {

    private final CourierApiClient courierApiClient;

    @Override
    public BigDecimal calculatePayout(Double distanceInKm) {
        try {
            return courierApiClient.calculate(new CourierPayoutCalculationInputDTO(distanceInKm)).getPayoutFee();
        } catch (HttpServerErrorException | CallNotPermittedException | IllegalArgumentException ex) {
            throw new BadGatewayException("Error while calculating payout for distance: " + distanceInKm + " km. " +
                    "Please try again later or contact support if the issue persists.");
        } catch (ResourceAccessException ex) {
            throw new GatewayTimeoutException("The service is currently unavailable. Please try again later.");
        }
    }
}
