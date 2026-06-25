package com.quantity.measurement;

import java.util.List;

/**
 * Repository interface following the Interface Segregation Principle.
 */
public interface IQuantityMeasurementRepository {
    void save(QuantityMeasurementEntity entity);
    List<QuantityMeasurementEntity> getAllMeasurements();
}
