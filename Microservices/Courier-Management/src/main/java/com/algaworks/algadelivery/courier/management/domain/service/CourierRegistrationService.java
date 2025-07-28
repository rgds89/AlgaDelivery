package com.algaworks.algadelivery.courier.management.domain.service;

import com.algaworks.algadelivery.courier.management.api.model.CourierInputDTO;
import com.algaworks.algadelivery.courier.management.domain.model.Courier;
import com.algaworks.algadelivery.courier.management.domain.repository.CourierRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class CourierRegistrationService {
    private final CourierRepository courierRepository;

    public Courier register(@Valid CourierInputDTO courierInputDTO) {
        Courier courier = Courier.brandNew(courierInputDTO.getName(), courierInputDTO.getPhone());
        return courierRepository.saveAndFlush(courier);
    }

    public Courier update(UUID courierId, @Valid CourierInputDTO courierInputDTO) {
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Courier not found with id: " + courierId));
        courier.setName(courierInputDTO.getName() != null ? courierInputDTO.getName() : courier.getName());
        courier.setPhone(courierInputDTO.getPhone() != null ? courierInputDTO.getPhone() : courier.getPhone());
        return courierRepository.saveAndFlush(courier);
    }

    public void delete(UUID courierId) {
        courierRepository.deleteById(courierId);
    }

    public PagedModel<Courier> couriers(Pageable pageable) {
        return new PagedModel<>(courierRepository.findAll(pageable));
    }

    public Courier getCourier(UUID courierId) {
        return courierRepository.findById(courierId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Courier not found with id: " + courierId));
    }
}
