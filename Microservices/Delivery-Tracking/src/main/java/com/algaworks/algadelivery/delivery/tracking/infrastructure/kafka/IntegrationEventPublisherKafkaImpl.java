package com.algaworks.algadelivery.delivery.tracking.infrastructure.kafka;

import com.algaworks.algadelivery.delivery.tracking.infrastructure.event.IntegrationEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class IntegrationEventPublisherKafkaImpl implements IntegrationEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publish(Object event, String key, String topic) {
        log.info("Publishing event to topic {} with key {}", topic, key);
        SendResult<String, Object> result = kafkaTemplate.send(topic, key, event).join();
        log.info("Event published to topic {} \n\t offset: {}",
                result.getRecordMetadata().topic(), result.getRecordMetadata().offset());

    }
}
