package com.quantity.measurement;

import java.util.Objects;

/**
 * Represents an immutable length measurement with a specific unit.
 * Supports equality comparison across units, explicit unit-to-unit conversion,
 * and addition of two lengths with optional target-unit specification.
 *
 * <p><strong>UC8 refactoring:</strong> All unit-specific conversion logic is
 * delegated to {@link LengthUnit}. This class focuses solely on value
 * comparison and arithmetic, upholding the Single Responsibility Principle.</p>
 *
 * <p>Instances are immutable: conversions and additions return new instances
 * rather than modifying the current one, ensuring value-object semantics.</p>
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
    // Public API: conversion (delegates to LengthUnit)
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
        double baseValue = this.unit.convertToBaseUnit(this.value);
        double convertedValue = targetUnit.convertFromBaseUnit(baseValue);
        return new QuantityLength(convertedValue, targetUnit);
    }

    /**
     * Static convenience method: converts a raw numeric value from sourceUnit to targetUnit.
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
        double baseValue = sourceUnit.convertToBaseUnit(value);
        return targetUnit.convertFromBaseUnit(baseValue);
    }

    // ─────────────────────────────────────────────
    // Public API: Addition (delegates to LengthUnit)
    // ─────────────────────────────────────────────

    /**
     * Instance method to add another length.
     * The result is returned as a new QuantityLength in this instance's unit.
     *
     * @param other the length to add (must not be null)
     * @return a new QuantityLength representing the sum
     * @throws IllegalArgumentException if the other quantity is null
     */
    public QuantityLength add(QuantityLength other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot add a null quantity");
        }
        double sumInBase = this.unit.convertToBaseUnit(this.value)
                         + other.unit.convertToBaseUnit(other.value);
        double sumInTargetUnit = this.unit.convertFromBaseUnit(sumInBase);
        return new QuantityLength(sumInTargetUnit, this.unit);
    }

    /**
     * Static method to add two lengths, returning the result in the specified target unit.
     *
     * @param q1 the first length to add
     * @param q2 the second length to add
     * @param targetUnit the desired unit for the result
     * @return a new QuantityLength representing the sum
     * @throws IllegalArgumentException if any argument is null
     */
    public static QuantityLength add(QuantityLength q1, QuantityLength q2, LengthUnit targetUnit) {
        if (q1 == null || q2 == null || targetUnit == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        double sumInBase = q1.unit.convertToBaseUnit(q1.value)
                         + q2.unit.convertToBaseUnit(q2.value);
        double sumInTargetUnit = targetUnit.convertFromBaseUnit(sumInBase);
        return new QuantityLength(sumInTargetUnit, targetUnit);
    }

    /**
     * Static method to add two lengths, returning the result in the unit of the first operand.
     *
     * @param q1 the first length to add (also determines the target unit)
     * @param q2 the second length to add
     * @return a new QuantityLength representing the sum
     * @throws IllegalArgumentException if any argument is null
     */
    public static QuantityLength add(QuantityLength q1, QuantityLength q2) {
        if (q1 == null || q2 == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        return add(q1, q2, q1.unit);
    }

    /**
     * Static method to add two raw measurements and return the result in a target unit.
     *
     * @param val1 the numeric value of the first measurement
     * @param unit1 the unit of the first measurement
     * @param val2 the numeric value of the second measurement
     * @param unit2 the unit of the second measurement
     * @param targetUnit the desired unit for the result
     * @return a new QuantityLength representing the sum
     * @throws IllegalArgumentException if any argument is invalid
     */
    public static QuantityLength add(double val1, LengthUnit unit1, double val2, LengthUnit unit2, LengthUnit targetUnit) {
        QuantityLength q1 = new QuantityLength(val1, unit1);
        QuantityLength q2 = new QuantityLength(val2, unit2);
        return add(q1, q2, targetUnit);
    }

    // ─────────────────────────────────────────────
    // Object overrides
    // ─────────────────────────────────────────────

    private static final double EPSILON = 1e-6;

    /**
     * Two QuantityLength objects are equal if they represent the same physical length
     * when both are converted to the common base unit (FEET), within an epsilon.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        QuantityLength that = (QuantityLength) obj;
        double thisBase = this.unit.convertToBaseUnit(this.value);
        double thatBase = that.unit.convertToBaseUnit(that.value);
        return Math.abs(thisBase - thatBase) <= EPSILON;
    }

    @Override
    public int hashCode() {
        double base = this.unit.convertToBaseUnit(this.value);
        return Objects.hash(Math.round(base / EPSILON));
    }

    /**
     * Human-readable representation, e.g. "QuantityLength{1.0 FEET}".
     */
    @Override
    public String toString() {
        return "QuantityLength{" + value + " " + unit + "}";
    }
}
