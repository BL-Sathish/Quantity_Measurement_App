package com.quantity.measurement;

/**
 * Controller layer exposing API methods for quantity measurement operations.
 * Routes incoming calls to the service layer, demonstrating Dependency Injection and Separation of Concerns.
 */
public class QuantityMeasurementController {

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
    }

    /**
     * API to compare two quantities.
     */
    public boolean performComparison(QuantityDTO op1, QuantityDTO op2) {
        return service.compare(op1, op2);
    }

    /**
     * API to convert a quantity to a target unit.
     */
    public QuantityDTO performConversion(QuantityDTO op, QuantityDTO.IMeasurableUnit targetUnit) {
        return service.convert(op, targetUnit);
    }

    /**
     * API to add two quantities.
     */
    public QuantityDTO performAddition(QuantityDTO op1, QuantityDTO op2, QuantityDTO.IMeasurableUnit targetUnit) {
        return service.add(op1, op2, targetUnit);
    }

    /**
     * API to subtract two quantities.
     */
    public QuantityDTO performSubtraction(QuantityDTO op1, QuantityDTO op2, QuantityDTO.IMeasurableUnit targetUnit) {
        return service.subtract(op1, op2, targetUnit);
    }

    /**
     * API to divide two quantities.
     */
    public double performDivision(QuantityDTO op1, QuantityDTO op2) {
        return service.divide(op1, op2);
    }
}
