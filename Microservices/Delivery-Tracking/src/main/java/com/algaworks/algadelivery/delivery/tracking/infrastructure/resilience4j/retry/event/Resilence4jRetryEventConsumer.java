package com.algaworks.algadelivery.delivery.tracking.infrastructure.resilience4j.retry.event;

import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import io.github.resilience4j.retry.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Resilence4jRetryEventConsumer implements RegistryEventConsumer<Retry> {
    @Override
    public void onEntryAddedEvent(EntryAddedEvent<Retry> entryAddedEvent) {
        entryAddedEvent.getAddedEntry().getEventPublisher()
                .onEvent(event -> log.info("Retry added event: {}", event.toString()));
    }

    @Override
    public void onEntryRemovedEvent(EntryRemovedEvent<Retry> entryRemoveEvent) {
        entryRemoveEvent.getRemovedEntry().getEventPublisher()
                .onEvent(event -> log.info("Retry remove event: {}", event.toString()));
    }

    @Override
    public void onEntryReplacedEvent(EntryReplacedEvent<Retry> entryReplacedEvent) {
        entryReplacedEvent.getOldEntry().getEventPublisher()
                .onEvent(event -> log.info("Retry replaced event: {}", event.toString()));
    }
}
