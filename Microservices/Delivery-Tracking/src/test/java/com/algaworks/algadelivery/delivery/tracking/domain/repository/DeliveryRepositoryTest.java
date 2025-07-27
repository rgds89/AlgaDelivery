package com.algaworks.algadelivery.delivery.tracking.domain.repository;

import com.algaworks.algadelivery.delivery.tracking.domain.model.ContactPoint;
import com.algaworks.algadelivery.delivery.tracking.domain.model.Delivery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeliveryRepositoryTest {
    @Autowired
    private DeliveryRepository deliveryRepository;

    @Test
    public void shouldPersist() {
        // Given
        Delivery delivery = Delivery.draft();
        delivery.addItem("coffee-machine", 5);
        delivery.addItem("toaster", 2);
        delivery.editPreparationDetails(createdValidPreparationDetails());

        deliveryRepository.saveAndFlush(delivery);

        // When
        Delivery persistedDelivery = deliveryRepository.findById(delivery.getId())
                .orElseThrow(() -> new IllegalArgumentException("Delivery not found"));

        // Then
        assertEquals(2, persistedDelivery.getItems().size());

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