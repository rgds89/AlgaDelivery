package com.algaworks.algadelivery.delivery.tracking.infrastructure.resilience4j.circuitbreaker.event;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import io.github.resilience4j.retry.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Resilence4jCircuitBreakerEventConsumer implements RegistryEventConsumer<CircuitBreaker> {
    @Override
    public void onEntryAddedEvent(EntryAddedEvent<CircuitBreaker> entryAddedEvent) {
        entryAddedEvent.getAddedEntry().getEventPublisher()
                .onEvent(event -> log.info("CircuitBreaker added event: {}", event.toString()));
    }

    @Override
    public void onEntryRemovedEvent(EntryRemovedEvent<CircuitBreaker> entryRemoveEvent) {
        entryRemoveEvent.getRemovedEntry().getEventPublisher()
                .onEvent(event -> log.info("CircuitBreaker remove event: {}", event.toString()));
    }

    @Override
    public void onEntryReplacedEvent(EntryReplacedEvent<CircuitBreaker> entryReplacedEvent) {
        entryReplacedEvent.getOldEntry().getEventPublisher()
                .onEvent(event -> log.info("CircuitBreaker replaced event: {}", event.toString()));
    }
}
