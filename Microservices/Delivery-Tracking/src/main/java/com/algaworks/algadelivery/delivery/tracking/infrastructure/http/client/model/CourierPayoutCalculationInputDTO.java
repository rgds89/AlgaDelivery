package com.algaworks.algadelivery.delivery.tracking.infrastructure.http.client.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CourierPayoutCalculationInputDTO {
    private Double distanceInKm;
}
