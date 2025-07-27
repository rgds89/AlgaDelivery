package com.algaworks.algadelivery.delivery.tracking.domain.model;

import com.algaworks.algadelivery.delivery.tracking.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryTest {

    @Test
    public void shouldChangeToPlaced() {
        // Given
        Delivery delivery = Delivery.draft();
        delivery.editPreparationDetails(createdValidPreparationDetails());

        // When
        delivery.place();

        // Then
        assertEquals(DeliveryStatus.WAITING_FOR_COURIER, delivery.getStatus());
        assertNotNull(delivery.getPlacedAt());
    }

    @Test
    public void shouldNotPlace() {
        // Given
        Delivery delivery = Delivery.draft();

        // When
        assertThrows(DomainException.class, () ->{
            delivery.place();
        });

        // Then
        assertEquals(DeliveryStatus.DRAFT, delivery.getStatus());
        assertNull(delivery.getPlacedAt());
    }

    private Delivery.PreparationDetails createdValidPreparationDetails() {
        ContactPoint sender = ContactPoint.builder()
                .zipCode("12345-678")
                .street("Main St")
                .number("123")
                .complement("Apt 4B")
                .neighborhood("Downtown")
                .name("John Doe")
                .phone("123-456-7890").build();
        ContactPoint recipient = ContactPoint.builder()
                .zipCode("98765-432")
                .street("Second St")
                .number("456")
                .complement("Suite 5")
                .neighborhood("Uptown")
                .name("Jane Smith")
                .phone("098-765-4321").build();

        return Delivery.PreparationDetails.builder()
                .sender(sender)
                .recipient(recipient)
                .distanceFee(BigDecimal.valueOf(15.00))
                .courierPayout(BigDecimal.valueOf(5.00))
                .expectedDeliveryTime(Duration.ofHours(5))
                .build();
    }

}