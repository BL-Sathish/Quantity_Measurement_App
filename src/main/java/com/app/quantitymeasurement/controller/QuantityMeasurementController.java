package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;
import com.app.quantitymeasurement.services.IQuantityMeasurementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.logging.Logger;

/**
 * REST Controller exposing HTTP endpoints for quantity measurement services.
 * Annotated with Swagger tag for OpenAPI documentation.
 */
@RestController
@RequestMapping("/api/v1/quantities")
@Tag(name = "Quantity Measurements", description = "REST API for quantity measurement operations")
public class QuantityMeasurementController {

    private static final Logger LOGGER = Logger.getLogger(QuantityMeasurementController.class.getName());

    @Autowired
    private IQuantityMeasurementService service;

    @PostMapping("/compare")
    @Operation(summary = "Compare two quantities")
    public QuantityMeasurementDTO compareQuantities(@Valid @RequestBody QuantityInputDTO input) {
        LOGGER.fine("Controller: compareQuantities invoked");
        return service.compare(input.getThisQuantityDTO(), input.getThatQuantityDTO());
    }

    @PostMapping("/convert")
    @Operation(summary = "Convert a quantity to another unit")
    public QuantityMeasurementDTO convertQuantity(@Valid @RequestBody QuantityInputDTO input) {
        LOGGER.fine("Controller: convertQuantity invoked");
        return service.convert(input.getThisQuantityDTO(), input.getThatQuantityDTO().getUnit());
    }

    @PostMapping("/add")
    @Operation(summary = "Add two quantities")
    public QuantityMeasurementDTO addQuantities(@Valid @RequestBody QuantityInputDTO input) {
        LOGGER.fine("Controller: addQuantities invoked");
        // Target unit defaults to the first operand's unit
        QuantityDTO.IMeasurableUnit targetUnit = input.getThisQuantityDTO().getUnit();
        return service.add(input.getThisQuantityDTO(), input.getThatQuantityDTO(), targetUnit);
    }

    @GetMapping("/history/operation/{operation}")
    @Operation(summary = "Get measurement history by operation type")
    public List<QuantityMeasurementDTO> getOperationHistory(@PathVariable("operation") String operation) {
        LOGGER.fine("Controller: getOperationHistory invoked for operation: " + operation);
        return service.getHistoryByOperation(operation);
    }

    @GetMapping("/history/type/{type}")
    @Operation(summary = "Get measurement history by measurement type")
    public List<QuantityMeasurementDTO> getMeasurementTypeHistory(@PathVariable("type") String type) {
        LOGGER.fine("Controller: getMeasurementTypeHistory invoked for type: " + type);
        return service.getHistoryByMeasurementType(type);
    }

    @GetMapping("/count/{operation}")
    @Operation(summary = "Get count of operations by type")
    public long getOperationCount(@PathVariable("operation") String operation) {
        LOGGER.fine("Controller: getOperationCount invoked for operation: " + operation);
        return service.getCountByOperation(operation);
    }
}
