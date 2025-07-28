package com.algaworks.algadelivery.courier.management.api.controller;

import com.algaworks.algadelivery.courier.management.api.model.CourierInputDTO;
import com.algaworks.algadelivery.courier.management.api.model.CourierPayoutCalculationInputDTO;
import com.algaworks.algadelivery.courier.management.api.model.CourierPayoutResultModel;
import com.algaworks.algadelivery.courier.management.domain.model.Courier;
import com.algaworks.algadelivery.courier.management.domain.service.CourierPayoutService;
import com.algaworks.algadelivery.courier.management.domain.service.CourierRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/couriers")
@RequiredArgsConstructor
public class CourierController {
    private final CourierRegistrationService courierRegistrationService;
    private final CourierPayoutService courierPayoutService;

    @PostMapping
    @ResponseStatus(CREATED)
    @ResponseBody
    public Courier register(@RequestBody @Valid CourierInputDTO courierInputDTO) {
        return courierRegistrationService.register(courierInputDTO);
    }

    @PutMapping("/{courierId}")
    @ResponseStatus(OK)
    @ResponseBody
    public Courier update(@PathVariable UUID couruerId, @RequestBody @Valid CourierInputDTO courierInputDTO) {
        return courierRegistrationService.update(couruerId, courierInputDTO);
    }

    @DeleteMapping("/{courierId}")
    @ResponseStatus(OK)
    public void delete(@PathVariable UUID courierId) {
        courierRegistrationService.delete(courierId);
    }

    @GetMapping
    @ResponseStatus(OK)
    @ResponseBody
    public PagedModel<Courier> couriers(@PageableDefault Pageable pageable) {
        return courierRegistrationService.couriers(pageable);
    }

    @GetMapping("/{courierId}")
    @ResponseStatus(OK)
    @ResponseBody
    public Courier getCourier(@PathVariable UUID courierId) {
        return courierRegistrationService.getCourier(courierId);
    }

    @PostMapping("/payout-calculation")
    @ResponseStatus(OK)
    @ResponseBody
    public CourierPayoutResultModel calculate(@RequestBody CourierPayoutCalculationInputDTO courierPayoutCalculationInputDTO) {
        BigDecimal payoutFee = courierPayoutService.calculate(courierPayoutCalculationInputDTO.getDistanceInKm());
        return new CourierPayoutResultModel(payoutFee);
    }
}
