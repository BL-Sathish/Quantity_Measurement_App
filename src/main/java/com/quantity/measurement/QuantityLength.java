package com.quantity.measurement;

import java.util.Objects;

/**
 * Represents an immutable length measurement with a specific unit.
 * Supports equality comparison across units and explicit unit-to-unit conversion.
 *
 * <p>Conversion formula: result = value × (sourceUnit.factor / targetUnit.factor)
 * where each unit's factor is the number of base units (INCH) it equals.</p>
 *
 * <p>Instances are immutable: conversions return new instances rather than
 * modifying the current one, ensuring value-object semantics.</p>
 */
public class QuantityLength {
    private final double value;
    private final LengthUnit unit;

    /**
     * Constructs a QuantityLength with the given value and unit.
     *
     * @param value the numeric measurement value
     * @param unit  the unit of measurement (must not be null)
     * @throws IllegalArgumentException if unit is null, or value is NaN / infinite
     */
    public QuantityLength(double value, LengthUnit unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be a finite number, but was: " + value);
        }
        this.value = value;
        this.unit = unit;
    }

    // ─────────────────────────────────────────────
    // Public API: conversion
    // ─────────────────────────────────────────────

    /**
     * Instance method: converts this length to a new QuantityLength expressed in the target unit.
     * Returns a new immutable instance; does not modify the current object.
     *
     * @param targetUnit the desired target unit (must not be null)
     * @return a new QuantityLength in the target unit
     * @throws IllegalArgumentException if targetUnit is null
     */
    public QuantityLength convertTo(LengthUnit targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        double convertedValue = convertValue(this.value, this.unit, targetUnit);
        return new QuantityLength(convertedValue, targetUnit);
    }

    /**
     * Static convenience method: converts a raw numeric value from sourceUnit to targetUnit.
     * Formula: result = value × (sourceUnit.factor / targetUnit.factor)
     *
     * @param value      the numeric measurement to convert
     * @param sourceUnit the unit of the input value (must not be null)
     * @param targetUnit the desired output unit (must not be null)
     * @return the converted value in targetUnit
     * @throws IllegalArgumentException if any argument is invalid (null unit, NaN, infinite)
     */
    public static double convert(double value, LengthUnit sourceUnit, LengthUnit targetUnit) {
        if (sourceUnit == null || targetUnit == null) {
            throw new IllegalArgumentException("Source and target units cannot be null");
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be a finite number, but was: " + value);
        }
        return convertValue(value, sourceUnit, targetUnit);
    }

    // ─────────────────────────────────────────────
    // Private helpers
    // ─────────────────────────────────────────────

    /**
     * Core conversion: normalises value to the base unit (INCH), then converts to target.
     */
    private static double convertValue(double value, LengthUnit sourceUnit, LengthUnit targetUnit) {
        double inBaseUnit = value * sourceUnit.getBaseUnitConversionFactor();
        return inBaseUnit / targetUnit.getBaseUnitConversionFactor();
    }

    /**
     * Returns this value expressed in the base unit (INCH) — used for equality comparison.
     */
    private double toBaseUnit() {
        return this.value * this.unit.getBaseUnitConversionFactor();
    }

    // ─────────────────────────────────────────────
    // Object overrides
    // ─────────────────────────────────────────────

    /**
     * Two QuantityLength objects are equal if they represent the same physical length
     * when both are converted to the common base unit (INCH).
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        QuantityLength that = (QuantityLength) obj;
        return Double.compare(this.toBaseUnit(), that.toBaseUnit()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(toBaseUnit());
    }

    /**
     * Human-readable representation, e.g. "QuantityLength{1.0 FEET}".
     */
    @Override
    public String toString() {
        return "QuantityLength{" + value + " " + unit + "}";
    }
}
