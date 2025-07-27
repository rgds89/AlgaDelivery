package com.algaworks.algadelivery.courier.management.domain.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(AccessLevel.PRIVATE)
public class Courier {
    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    @Setter
    private String name;
    @Setter
    private String phone;
    private Integer fulfilledDeliveriesQuantity;
    private Integer pendingDeliveriesQuantity;
    private OffsetDateTime lastFulfilledDeliveryAt;
    @OneToMany(mappedBy = "courier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssignedDelivery> pendingDeliveries = new ArrayList<>();

    public List<AssignedDelivery> getPendingDeliveries() {
        return Collections.unmodifiableList(this.pendingDeliveries);
    }

    public static Courier brandNew(String name, String phone) {
        Courier courier = new Courier();
        courier.setId(UUID.randomUUID());
        courier.setName(name);
        courier.setPhone(phone);
        courier.setFulfilledDeliveriesQuantity(0);
        courier.setPendingDeliveriesQuantity(0);
        return courier;
    }

    public void assignDelivery(UUID deliveryId) {
        this.pendingDeliveries.add(AssignedDelivery.pending(deliveryId, this));
        this.pendingDeliveriesQuantity++;
    }

    public void fulfillDelivery(UUID deliveryId) {
        AssignedDelivery assignedDelivery = this.pendingDeliveries.stream()
                .filter(pending -> pending.getId().equals(deliveryId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Delivery not found"));

        this.pendingDeliveries.remove(assignedDelivery);
        this.pendingDeliveriesQuantity--;
        this.fulfilledDeliveriesQuantity++;
        this.lastFulfilledDeliveryAt = OffsetDateTime.now();
    }
}
