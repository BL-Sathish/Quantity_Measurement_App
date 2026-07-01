package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPA Repository interface for QuantityMeasurementEntity.
 */
@Repository
public interface QuantityMeasurementRepository extends JpaRepository<QuantityMeasurementEntity, Long> {

    /**
     * Automatically generates a query to find all records where the operation matches the provided value.
     */
    List<QuantityMeasurementEntity> findByOperation(String operation);

    /**
     * Finds all records where the thisMeasurementType field matches the provided value.
     */
    List<QuantityMeasurementEntity> findByThisMeasurementType(String measurementType);

    /**
     * Finds all records where the createdAt field is after the specified date.
     */
    List<QuantityMeasurementEntity> findByCreatedAtAfter(LocalDateTime date);

    /**
     * Custom JPQL query retrieving successful operations by operation type.
     */
    @Query("SELECT q FROM QuantityMeasurementEntity q WHERE q.operation = :operation AND q.error = false")
    List<QuantityMeasurementEntity> findSuccessfulOperationsByOperation(@Param("operation") String operation);

    /**
     * Counts the number of records where the operation matches the provided value and error is false.
     */
    long countByOperationAndErrorFalse(String operation);

    /**
     * Finds all records where the error field is true.
     */
    List<QuantityMeasurementEntity> findByErrorTrue();
}
