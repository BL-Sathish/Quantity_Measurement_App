package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;

import java.util.List;

/**
 * Repository interface following the Interface Segregation Principle.
 * Enhanced for UC16 to support additional query capabilities.
 */
public interface IQuantityMeasurementRepository {
    void save(QuantityMeasurementEntity entity);
    List<QuantityMeasurementEntity> getAllMeasurements();

    /**
     * Retrieves measurements filtered by operation type.
     *
     * @param operationType the type of operation (e.g., "comparison", "conversion", "addition")
     * @return list of matching entities
     */
    List<QuantityMeasurementEntity> getMeasurementsByOperationType(String operationType);

    /**
     * Retrieves a specific measurement by its unique ID.
     *
     * @param id the UUID of the entity
     * @return the matching entity, or null if not found
     */
    QuantityMeasurementEntity getMeasurementById(String id);

    /**
     * Returns the total count of stored measurements.
     *
     * @return total count
     */
    long getCount();

    /**
     * Clears all stored measurements.
     */
    void clear();
}
