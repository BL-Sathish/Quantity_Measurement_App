package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.services.IQuantityMeasurementService;

import java.util.logging.Logger;

/**
 * Controller layer exposing API methods for quantity measurement operations.
 * Routes incoming calls to the service layer, demonstrating Dependency Injection and Separation of Concerns.
 * Enhanced with java.util.logging for UC16.
 */
public class QuantityMeasurementController {

    private static final Logger LOGGER = Logger.getLogger(QuantityMeasurementController.class.getName());

    private final IQuantityMeasurementService service;

    /**
     * Constructor injection of the service layer.
     *
     * @param service the quantity measurement service
     */
    public QuantityMeasurementController(IQuantityMeasurementService service) {
        if (service == null) {
            throw new IllegalArgumentException("Service cannot be null");
        }
        this.service = service;
        LOGGER.info("QuantityMeasurementController initialized.");
    }

    /**
     * API to compare two quantities.
     */
    public boolean performComparison(QuantityDTO op1, QuantityDTO op2) {
        LOGGER.fine("Controller: performComparison invoked");
        return service.compare(op1, op2);
    }

    /**
     * API to convert a quantity to a target unit.
     */
    public QuantityDTO performConversion(QuantityDTO op, QuantityDTO.IMeasurableUnit targetUnit) {
        LOGGER.fine("Controller: performConversion invoked");
        return service.convert(op, targetUnit);
    }

    /**
     * API to add two quantities.
     */
    public QuantityDTO performAddition(QuantityDTO op1, QuantityDTO op2, QuantityDTO.IMeasurableUnit targetUnit) {
        LOGGER.fine("Controller: performAddition invoked");
        return service.add(op1, op2, targetUnit);
    }

    /**
     * API to subtract two quantities.
     */
    public QuantityDTO performSubtraction(QuantityDTO op1, QuantityDTO op2, QuantityDTO.IMeasurableUnit targetUnit) {
        LOGGER.fine("Controller: performSubtraction invoked");
        return service.subtract(op1, op2, targetUnit);
    }

    /**
     * API to divide two quantities.
     */
    public double performDivision(QuantityDTO op1, QuantityDTO op2) {
        LOGGER.fine("Controller: performDivision invoked");
        return service.divide(op1, op2);
    }
}
