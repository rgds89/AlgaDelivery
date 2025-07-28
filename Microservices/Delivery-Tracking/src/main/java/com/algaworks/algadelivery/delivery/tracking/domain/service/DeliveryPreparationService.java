package com.algaworks.algadelivery.delivery.tracking.domain.service;

import com.algaworks.algadelivery.delivery.tracking.api.model.DeliveryInputDTO;
import com.algaworks.algadelivery.delivery.tracking.domain.exception.DomainException;
import com.algaworks.algadelivery.delivery.tracking.domain.model.ContactPoint;
import com.algaworks.algadelivery.delivery.tracking.domain.model.Delivery;
import com.algaworks.algadelivery.delivery.tracking.domain.repository.DeliveryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryPreparationService {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryTimeEstimationService deliveryTimeEstimationService;
    private final CourierPayoutCalculationService courierPayoutCalculationService;

    @Transactional
    public Delivery draft(DeliveryInputDTO deliveryInputDTO) {
        Delivery delivery = Delivery.draft();
        handlePreparation(deliveryInputDTO, delivery);
        return deliveryRepository.saveAndFlush(delivery);
    }

    @Transactional
    public Delivery edit(UUID deliveryId, DeliveryInputDTO deliveryInputDTO) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DomainException("Delivery not found"));

        delivery.removeItems();
        handlePreparation(deliveryInputDTO, delivery);
        return deliveryRepository.saveAndFlush(delivery);
    }

    @Transactional
    public PagedModel<Delivery> getDeliveries(Pageable pageable) {
        return new PagedModel<>(deliveryRepository.findAll(pageable));
    }

    @Transactional
    public Delivery getDelivery(UUID deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Delivery not found"));
    }

    private void handlePreparation(DeliveryInputDTO deliveryInputDTO, Delivery delivery) {
        ContactPoint sender = ContactPoint.builder()
                .zipCode(deliveryInputDTO.getSender().getZipCode())
                .street(deliveryInputDTO.getSender().getStreet())
                .number(deliveryInputDTO.getSender().getNumber())
                .complement(deliveryInputDTO.getSender().getComplement())
                .neighborhood(deliveryInputDTO.getSender().getNeighborhood())
                .name(deliveryInputDTO.getSender().getName())
                .phone(deliveryInputDTO.getSender().getPhone())
                .build();

        ContactPoint recipient = ContactPoint.builder()
                .zipCode(deliveryInputDTO.getRecipient().getZipCode())
                .street(deliveryInputDTO.getRecipient().getStreet())
                .number(deliveryInputDTO.getRecipient().getNumber())
                .complement(deliveryInputDTO.getRecipient().getComplement())
                .neighborhood(deliveryInputDTO.getRecipient().getNeighborhood())
                .name(deliveryInputDTO.getRecipient().getName())
                .phone(deliveryInputDTO.getRecipient().getPhone())
                .build();
        var preparationDetails = Delivery.PreparationDetails.builder()
                .recipient(recipient)
                .sender(sender)
                .expectedDeliveryTime(deliveryTimeEstimationService.estimate(sender, recipient).getEstimatedTime())
                .courierPayout(
                        courierPayoutCalculationService.calculatePayout(
                                deliveryTimeEstimationService.estimate(sender, recipient)
                                        .getDistanceInKm())

                )
                .distanceFee(new BigDecimal(3)
                        .multiply(BigDecimal.valueOf(
                                deliveryTimeEstimationService
                                        .estimate(sender, recipient)
                                        .getDistanceInKm()
                        ))
                        .setScale(2, RoundingMode.HALF_EVEN))
                .build();
        delivery.editPreparationDetails(preparationDetails);
        deliveryInputDTO.getItems().forEach(itemInput -> {
            delivery.addItem(itemInput.getName(), itemInput.getQuantity());
        });

    }
}
