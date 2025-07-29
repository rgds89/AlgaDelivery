package com.algaworks.algadelivery.courier.management.infrastructure.kafka;

import com.algaworks.algadelivery.courier.management.domain.service.CourierDeliveryService;
import com.algaworks.algadelivery.courier.management.infrastructure.event.DeliveryFulfilledIntegrationEvent;
import com.algaworks.algadelivery.courier.management.infrastructure.event.DeliveryPlacedIntegrationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "${kafka.topic.name:deliveries.v1.events}",
        groupId = "${kafka.consumer.group-id:courier-management}")
@Slf4j
@RequiredArgsConstructor
public class KafkaDeliveriesMessageHandler {
    private final CourierDeliveryService courierDeliveryService;

    @KafkaHandler(isDefault = true)
    void defaultHandler(@Payload Object object) {
        log.info("Default handler invoked for message: {}", object);
    }

    @KafkaHandler
    void handle(@Payload DeliveryPlacedIntegrationEvent event) {
        log.info("Handling DeliveryPlacedIntegrationEvent: {}", event);
        courierDeliveryService.assign(event.getDeliveryId());
    }

    @KafkaHandler
    void handle(@Payload DeliveryFulfilledIntegrationEvent event) {
        log.info("Handling DeliveryFulfilledIntegrationEvent: {}", event);
        courierDeliveryService.fulfill(event.getDeliveryId());
    }
}
