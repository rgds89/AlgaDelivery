package com.algaworks.algadelivery.delivery.tracking.infrastructure.http.client.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class CourierPayoutResultDTO {
    private BigDecimal payoutFee;
}
