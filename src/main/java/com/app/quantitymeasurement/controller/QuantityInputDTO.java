package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.model.QuantityDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * DTO representing the request payload wrapper for REST quantity measurement operations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityInputDTO {

    @Valid
    @NotNull(message = "thisQuantityDTO cannot be null")
    private QuantityDTO thisQuantityDTO;

    @Valid
    @NotNull(message = "thatQuantityDTO cannot be null")
    private QuantityDTO thatQuantityDTO;
}
