package com.app.quantitymeasurement.services;

import com.app.quantitymeasurement.entity.QuantityDTO;

/**
 * Service interface for quantity measurement operations.
 * Following the Dependency Injection and Interface Segregation Principles.
 */
public interface IQuantityMeasurementService {
    
    /**
     * Compares two quantities for equality.
     * Maps inputs to internal models, performs the comparison, saves to repository, and returns result.
     *
     * @param op1 the first quantity DTO
     * @param op2 the second quantity DTO
     * @return true if equal, false otherwise
     */
    boolean compare(QuantityDTO op1, QuantityDTO op2);

    /**
     * Converts a quantity to a target unit.
     * Maps inputs to internal models, performs the conversion, saves to repository, and returns result.
     *
     * @param op the source quantity DTO
     * @param targetUnit the target unit DTO
     * @return a new QuantityDTO with converted value and target unit
     */
    QuantityDTO convert(QuantityDTO op, QuantityDTO.IMeasurableUnit targetUnit);

    /**
     * Adds two quantities and returns the result in the target unit.
     * Maps inputs to internal models, performs addition, saves to repository, and returns result.
     *
     * @param op1 the first quantity DTO
     * @param op2 the second quantity DTO
     * @param targetUnit the target unit DTO (if null, defaults to op1's unit)
     * @return a new QuantityDTO with the sum in target unit
     */
    QuantityDTO add(QuantityDTO op1, QuantityDTO op2, QuantityDTO.IMeasurableUnit targetUnit);

    /**
     * Subtracts the second quantity from the first and returns the result in the target unit.
     * Maps inputs to internal models, performs subtraction, saves to repository, and returns result.
     *
     * @param op1 the first quantity DTO (minuend)
     * @param op2 the second quantity DTO (subtrahend)
     * @param targetUnit the target unit DTO (if null, defaults to op1's unit)
     * @return a new QuantityDTO with the difference in target unit
     */
    QuantityDTO subtract(QuantityDTO op1, QuantityDTO op2, QuantityDTO.IMeasurableUnit targetUnit);

    /**
     * Divides the first quantity by the second, returning a dimensionless ratio.
     * Maps inputs to internal models, performs division, saves to repository, and returns ratio.
     *
     * @param op1 the first quantity DTO
     * @param op2 the second quantity DTO
     * @return a dimensionless ratio
     */
    double divide(QuantityDTO op1, QuantityDTO op2);
}
