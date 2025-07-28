package com.algaworks.algadelivery.delivery.tracking.api.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ContactPointInputDTO {
    @NotBlank
    @Valid
    private String zipCode;
    @NotBlank
    @Valid
    private String street;
    @NotBlank
    @Valid
    private String number;
    private String complement;
    private  String neighborhood;
    @NotBlank
    @Valid
    private String name;
    @NotBlank
    @Valid
    private String phone;
}
