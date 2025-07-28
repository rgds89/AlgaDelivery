package com.algaworks.algadelivery.delivery.tracking.infrastructure.fake;

import com.algaworks.algadelivery.delivery.tracking.domain.model.ContactPoint;
import com.algaworks.algadelivery.delivery.tracking.domain.service.DeliveryTimeEstimationService;
import com.algaworks.algadelivery.delivery.tracking.domain.service.DeliveyEstimate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class DeliveryTimeEstimationServiceFakeImpl implements DeliveryTimeEstimationService {
    @Override
    public DeliveyEstimate estimate(ContactPoint sender, ContactPoint recipient) {
        return new DeliveyEstimate(
                Duration.ofHours(3),
                3.1
        );
    }
}
