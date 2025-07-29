package com.algaworks.algadelivery.delivery.tracking.infrastructure.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Value("${kafka.topic.delivery-tracking.name:deliveries.v1.events}")
    private String topicName;
    @Value("${kafka.topic.delivery-tracking.partitions:1}")
    private int partitions;
    @Value("${kafka.topic.delivery-tracking.replicas:1}")
    private int replicas;

    @Bean
    public NewTopic deliveryTrackingTopic() {
        return TopicBuilder
                .name(topicName)
                .partitions(partitions)
                .replicas(replicas)
                .build();
    }
}
