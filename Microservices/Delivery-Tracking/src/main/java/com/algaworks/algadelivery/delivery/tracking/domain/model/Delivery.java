package com.algaworks.algadelivery.delivery.tracking.domain.model;

import com.algaworks.algadelivery.delivery.tracking.domain.event.DeliveryFulfilledEvent;
import com.algaworks.algadelivery.delivery.tracking.domain.event.DeliveryPickedUpEvent;
import com.algaworks.algadelivery.delivery.tracking.domain.event.DeliveryPlacedEvent;
import com.algaworks.algadelivery.delivery.tracking.domain.exception.DomainException;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(AccessLevel.PRIVATE)
@Getter
public class Delivery extends AbstractAggregateRoot<Delivery> {
    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    private UUID corrierId;
    private OffsetDateTime placedAt;
    private OffsetDateTime assignedAt;
    private OffsetDateTime expectedDeliveryAt;
    private OffsetDateTime fulfilledAt;
    private BigDecimal distanceFee;
    private BigDecimal courierPayout;
    private BigDecimal totalCost;
    private Integer totalItems;
    private DeliveryStatus status;
    @OneToMany(mappedBy = "delivery", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Item> items = new ArrayList<>();
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zipCode", column = @Column(name = "sender_zip_code")),
            @AttributeOverride(name = "street", column = @Column(name = "sender_street")),
            @AttributeOverride(name = "number", column = @Column(name = "sender_number")),
            @AttributeOverride(name = "complement", column = @Column(name = "sender_complement")),
            @AttributeOverride(name = "neighborhood", column = @Column(name = "sender_neighborhood")),
            @AttributeOverride(name = "name", column = @Column(name = "sender_name")),
            @AttributeOverride(name = "phone", column = @Column(name = "sender_phone"))
    })
    private ContactPoint sender;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zipCode", column = @Column(name = "recipient_zip_code")),
            @AttributeOverride(name = "street", column = @Column(name = "recipient_street")),
            @AttributeOverride(name = "number", column = @Column(name = "recipient_number")),
            @AttributeOverride(name = "complement", column = @Column(name = "recipient_complement")),
            @AttributeOverride(name = "neighborhood", column = @Column(name = "recipient_neighborhood")),
            @AttributeOverride(name = "name", column = @Column(name = "recipient_name")),
            @AttributeOverride(name = "phone", column = @Column(name = "recipient_phone"))
    })
    private ContactPoint recipient;

    public static Delivery draft() {
        Delivery delivery = new Delivery();
        delivery.setId(UUID.randomUUID());
        delivery.setStatus(DeliveryStatus.DRAFT);
        delivery.setTotalItems(0);
        delivery.setTotalCost(BigDecimal.ZERO);
        delivery.setCourierPayout(BigDecimal.ZERO);
        delivery.setDistanceFee(BigDecimal.ZERO);
        return delivery;
    }

    public UUID addItem(String name, Integer quantity) {
        Item item = Item.brandNew(name, quantity, this);
        items.add(item);
        calculateTotalsItems();
        return item.getId();
    }

    public void removeItem(UUID itemId) {
        getItems().removeIf(item -> item.getId().equals(itemId));
        calculateTotalsItems();
    }

    public void removeItems() {
        getItems().clear();
        calculateTotalsItems();
    }

    public void changeItemQuantity(UUID itemId, Integer quantity) {
        Item item = getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new DomainException("Item not found with id: " + itemId));
        item.setQuantity(quantity);
        calculateTotalsItems();
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(this.items);
    }

    public void place() {
        verifyIfCanBePlaced();
        setPlacedAt(OffsetDateTime.now());
        changeStatusTo(DeliveryStatus.WAITING_FOR_COURIER);
        super.registerEvent(
                new DeliveryPlacedEvent(this.getPlacedAt(), this.getId())
        );
    }

    public void pickUp(UUID courierId) {
        setAssignedAt(OffsetDateTime.now());
        setCorrierId(courierId);
        changeStatusTo(DeliveryStatus.IN_TRANSIT);
        super.registerEvent(
                new DeliveryPickedUpEvent(this.getAssignedAt(), this.getId())
        );
    }

    public void markAsDelivered() {
        setFulfilledAt(OffsetDateTime.now());
        changeStatusTo(DeliveryStatus.DELIVERED);
        super.registerEvent(
                new DeliveryFulfilledEvent(this.getFulfilledAt(), this.getId())
        );
    }

    public void editPreparationDetails(PreparationDetails preparationDetails) {
        verifyIfCanBeEdited();
        setSender(preparationDetails.getSender());
        setRecipient(preparationDetails.getRecipient());
        setDistanceFee(preparationDetails.getDistanceFee());
        setCourierPayout(preparationDetails.getCourierPayout());
        setExpectedDeliveryAt(OffsetDateTime.now().plus(preparationDetails.getExpectedDeliveryTime()));
        setTotalCost(getDistanceFee().add(getCourierPayout()));
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class PreparationDetails {
        private ContactPoint sender;
        private ContactPoint recipient;
        private BigDecimal distanceFee;
        private BigDecimal courierPayout;
        private Duration expectedDeliveryTime;
    }

    private void calculateTotalsItems() {
        setTotalItems(getItems().stream().mapToInt(Item::getQuantity).sum());
    }

    private void verifyIfCanBeEdited() {
        if (!getStatus().equals(DeliveryStatus.DRAFT)) {
            throw new DomainException("Delivery cannot be edited after it has been placed.");
        }
    }

    private void verifyIfCanBePlaced() {
        if (!isFilled()) {
            throw new DomainException("Delivery must have at least one item.");
        }
        if (!getStatus().equals(DeliveryStatus.DRAFT)) {
            throw new DomainException("Delivery is already placed.");
        }
    }

    private boolean isFilled() {
        return getSender() != null && getRecipient() != null && getTotalCost() != null;
    }

    private void changeStatusTo(DeliveryStatus newStatus) {
        if (newStatus != null && getStatus().canNotChangeTo(newStatus)) {
            throw new DomainException("Cannot change status from " + getStatus() + " to " + newStatus);
        }
        setStatus(newStatus);
    }
}
