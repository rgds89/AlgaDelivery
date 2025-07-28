package com.algaworks.algadelivery.delivery.tracking.api.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DeliveryInputDTO {
    @NotNull
    @Valid
    private ContactPointInputDTO sender;
    @NotNull
    @Valid
    private ContactPointInputDTO recipient;
    @NotEmpty
    @Valid
    @Size(min = 1)
    private List<ItemInputDTO> items;
}
