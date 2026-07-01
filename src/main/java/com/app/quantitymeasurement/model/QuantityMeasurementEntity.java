package com.app.quantitymeasurement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * JPA entity representing a quantity measurement record.
 * Annotated with Lombok to reduce boilerplate.
 */
@Entity
@Table(name = "quantity_measurement_entity")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityMeasurementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "this_value")
    private Double thisValue;

    @Column(name = "this_unit")
    private String thisUnit;

    @Column(name = "this_measurement_type")
    private String thisMeasurementType;

    @Column(name = "that_value")
    private Double thatValue;

    @Column(name = "that_unit")
    private String thatUnit;

    @Column(name = "that_measurement_type")
    private String thatMeasurementType;

    @Column(name = "operation")
    private String operation;

    @Column(name = "result_string")
    private String resultString;

    @Column(name = "result_value")
    private Double resultValue;

    @Column(name = "result_unit")
    private String resultUnit;

    @Column(name = "result_measurement_type")
    private String resultMeasurementType;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "error")
    private Boolean error;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Custom constructor for comparisons
    public QuantityMeasurementEntity(Double thisValue, String thisUnit, String thisMeasurementType,
                                     Double thatValue, String thatUnit, String thatMeasurementType,
                                     String operation, String resultString, Boolean error) {
        this.thisValue = thisValue;
        this.thisUnit = thisUnit;
        this.thisMeasurementType = thisMeasurementType;
        this.thatValue = thatValue;
        this.thatUnit = thatUnit;
        this.thatMeasurementType = thatMeasurementType;
        this.operation = operation;
        this.resultString = resultString;
        this.error = error;
    }

    // Custom constructor for conversions
    public QuantityMeasurementEntity(Double thisValue, String thisUnit, String thisMeasurementType,
                                     Double thatValue, String thatUnit, String thatMeasurementType,
                                     String operation, Double resultValue, String resultUnit,
                                     String resultMeasurementType, Boolean error) {
        this.thisValue = thisValue;
        this.thisUnit = thisUnit;
        this.thisMeasurementType = thisMeasurementType;
        this.thatValue = thatValue;
        this.thatUnit = thatUnit;
        this.thatMeasurementType = thatMeasurementType;
        this.operation = operation;
        this.resultValue = resultValue;
        this.resultUnit = resultUnit;
        this.resultMeasurementType = resultMeasurementType;
        this.error = error;
    }

    // Custom constructor for arithmetic additions/subtractions
    public QuantityMeasurementEntity(Double thisValue, String thisUnit, String thisMeasurementType,
                                     Double thatValue, String thatUnit, String thatMeasurementType,
                                     String operation, Double resultValue, String resultUnit,
                                     String resultMeasurementType, String resultString, Boolean error) {
        this.thisValue = thisValue;
        this.thisUnit = thisUnit;
        this.thisMeasurementType = thisMeasurementType;
        this.thatValue = thatValue;
        this.thatUnit = thatUnit;
        this.thatMeasurementType = thatMeasurementType;
        this.operation = operation;
        this.resultValue = resultValue;
        this.resultUnit = resultUnit;
        this.resultMeasurementType = resultMeasurementType;
        this.resultString = resultString;
        this.error = error;
    }

    // Custom constructor for errors
    public QuantityMeasurementEntity(String errorMessage, String operation, Boolean error) {
        this.errorMessage = errorMessage;
        this.operation = operation;
        this.error = error;
    }
}
