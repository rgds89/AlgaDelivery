package com.algaworks.algadelivery.courier.management.domain.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(AccessLevel.PRIVATE)
public class AssignedDelivery {
    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    private OffsetDateTime assignedAt;
    @ManyToOne(optional = false)
    @Getter(AccessLevel.PRIVATE)
    private Courier courier;

    static AssignedDelivery pending(UUID id, Courier courier) {
        AssignedDelivery assignedDelivery = new AssignedDelivery();
        assignedDelivery.setId(id);
        assignedDelivery.setAssignedAt(OffsetDateTime.now());
        assignedDelivery.setCourier(courier);
        return assignedDelivery;
    }
}
