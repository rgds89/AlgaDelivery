package com.algaworks.algadelivery.courier.management.domain.repository;

import com.algaworks.algadelivery.courier.management.domain.model.Courier;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CourierRepositoryTest {
    @Autowired
    CourierRepository courierRepository;

    @Test
    public void shouldPersist(){
        // Given
         Courier courier = Courier.brandNew("John Doe", "123-456-7890");
         courier.assignDelivery(UUID.randomUUID());
         courierRepository.saveAndFlush(courier);

        // When
        Courier persistedCourier = courierRepository.findById(courier.getId())
                .orElseThrow(() -> new IllegalArgumentException("Courier not found"));

        // Then
        assertEquals(1, persistedCourier.getPendingDeliveries().size());
    }

}