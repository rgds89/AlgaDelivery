package com.algaworks.algadelivery.delivery.tracking.api.controller;

import com.algaworks.algadelivery.delivery.tracking.api.model.CourierInputDTO;
import com.algaworks.algadelivery.delivery.tracking.api.model.DeliveryInputDTO;
import com.algaworks.algadelivery.delivery.tracking.domain.model.Delivery;
import com.algaworks.algadelivery.delivery.tracking.domain.service.DeliveryCheckpointService;
import com.algaworks.algadelivery.delivery.tracking.domain.service.DeliveryPreparationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/deliveries")
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryPreparationService deliveryPreparationService;
    private final DeliveryCheckpointService deliveryCheckpointService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Delivery draft(@RequestBody @Valid DeliveryInputDTO input) {
        return deliveryPreparationService.draft(input);
    }

    @PutMapping("/{deliveryId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Delivery edit(@PathVariable UUID deliveryId,
                         @RequestBody @Valid DeliveryInputDTO input) {
        return deliveryPreparationService.edit(deliveryId, input);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PagedModel<Delivery> getDeliveries(@PageableDefault Pageable pageable) {
        return deliveryPreparationService.getDeliveries(pageable);
    }

    @GetMapping("/{deliveryId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Delivery getDelivery(@PathVariable UUID deliveryId) {
        return deliveryPreparationService.getDelivery(deliveryId);
    }

    @PostMapping("/{deliveryId}/placement")
    @ResponseStatus(HttpStatus.OK)
    public void place(@PathVariable UUID deliveryId) {
        deliveryCheckpointService.place(deliveryId);
    }

    @PostMapping("/{deliveryId}/pickups")
    @ResponseStatus(HttpStatus.OK)
    public void pickup(@PathVariable UUID deliveryId, @RequestBody CourierInputDTO courierInput) {
        deliveryCheckpointService.pickup(deliveryId, courierInput.getCourierId());
    }

    @PostMapping("/{deliveryId}/completion")
    @ResponseStatus(HttpStatus.OK)
    public void complete(@PathVariable UUID deliveryId) {
        deliveryCheckpointService.complete(deliveryId);
    }
}

