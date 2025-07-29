package com.algaworks.algadelivery.delivery.tracking.infrastructure.event;

import com.algaworks.algadelivery.delivery.tracking.domain.event.DeliveryFulfilledEvent;
import com.algaworks.algadelivery.delivery.tracking.domain.event.DeliveryPickedUpEvent;
import com.algaworks.algadelivery.delivery.tracking.domain.event.DeliveryPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeliveryDomainEventHandler {
    private final IntegrationEventPublisher integrationEventPublisher;

    @Value("${kafka.topic.delivery-tracking.name:deliveries.v1.events}")
    private String topicName;

    @EventListener
    void handle(DeliveryPlacedEvent event) {
        log.info("Handling DeliveryPlacedEvent for delivery with id: {}", event.getDeliveryId());
        integrationEventPublisher.publish(event, event.getDeliveryId().toString(), topicName);
    }

    @EventListener
    void handle(DeliveryPickedUpEvent event) {
        log.info("Handling DeliveryPickUpEvent for delivery with id: {}", event.getDeliveryId());
        integrationEventPublisher.publish(event, event.getDeliveryId().toString(), topicName);
    }

    @EventListener
    void handle(DeliveryFulfilledEvent event) {
        log.info("Handling DeliveryFulfilledEvent for delivery with id: {}", event.getDeliveryId());
        integrationEventPublisher.publish(event, event.getDeliveryId().toString(), topicName);
    }
}
