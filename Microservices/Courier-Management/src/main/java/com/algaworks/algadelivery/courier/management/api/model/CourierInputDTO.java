package com.algaworks.algadelivery.courier.management.api.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CourierInputDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String phone;
}
