package com.app.quantitymeasurement.services;

import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;

import java.util.List;

/**
 * Service interface for quantity measurement operations.
 * Modernized for UC17 Spring integration returning DTOs.
 */
public interface IQuantityMeasurementService {
    
    /**
     * Compares two quantities and returns comparison DTO.
     */
    QuantityMeasurementDTO compare(QuantityDTO op1, QuantityDTO op2);

    /**
     * Converts a quantity and returns conversion DTO.
     */
    QuantityMeasurementDTO convert(QuantityDTO op, QuantityDTO.IMeasurableUnit targetUnit);

    /**
     * Adds two quantities and returns result DTO.
     */
    QuantityMeasurementDTO add(QuantityDTO op1, QuantityDTO op2, QuantityDTO.IMeasurableUnit targetUnit);

    /**
     * Subtracts the second quantity from the first and returns difference DTO.
     */
    QuantityMeasurementDTO subtract(QuantityDTO op1, QuantityDTO op2, QuantityDTO.IMeasurableUnit targetUnit);

    /**
     * Divides the first quantity by the second, returning ratio DTO.
     */
    QuantityMeasurementDTO divide(QuantityDTO op1, QuantityDTO op2);

    /**
     * Retrieves operations history filtered by operation type.
     */
    List<QuantityMeasurementDTO> getHistoryByOperation(String operation);

    /**
     * Retrieves operations history filtered by measurement type.
     */
    List<QuantityMeasurementDTO> getHistoryByMeasurementType(String measurementType);

    /**
     * Returns total count of operations for a specific type.
     */
    long getCountByOperation(String operation);
}
