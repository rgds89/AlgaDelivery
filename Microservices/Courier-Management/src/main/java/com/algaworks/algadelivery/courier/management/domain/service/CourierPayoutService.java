package com.algaworks.algadelivery.courier.management.domain.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CourierPayoutService {
    public BigDecimal calculate(Double distanceInKm) {
        return new BigDecimal("10.00")
                .multiply(BigDecimal.valueOf(distanceInKm))
                .setScale(2, RoundingMode.HALF_EVEN);
    }
}
