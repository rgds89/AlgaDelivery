package com.algaworks.algadelivery.courier.management.domain.service;

import com.algaworks.algadelivery.courier.management.domain.repository.CourierRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CourierDeliveryService {
    private final CourierRepository courierRepository;

    public void assign(UUID deliveryId){
        var courier = courierRepository.findTop1ByOrderByLastFulfilledDeliveryAtAsc().orElseThrow(
                () -> new IllegalArgumentException("No couriers available to assign to delivery: " + deliveryId)
        );
        courier.assignDelivery(deliveryId);
        courierRepository.saveAndFlush(courier);
        log.info("Assigned delivery {} to courier {}", deliveryId, courier.getId());
    }

    public void fulfill(UUID deliveryId) {
        var courier = courierRepository.findByPendingDeliveries_id(deliveryId).orElseThrow(
                () -> new IllegalArgumentException("No courier found with pending delivery: " + deliveryId)
        );
        courier.fulfillDelivery(deliveryId);
        courierRepository.saveAndFlush(courier);
        log.info("Fulfilled delivery {} by courier {}", deliveryId, courier.getId());
    }
}
