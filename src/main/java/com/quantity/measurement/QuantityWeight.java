package com.quantity.measurement;

import java.util.Objects;

/**
 * Represents an immutable weight measurement with a specific unit.
 * Supports equality comparison across units, explicit unit-to-unit conversion,
 * and addition of two weights with optional target-unit specification.
 *
 * <p>All unit-specific conversion logic is delegated to {@link WeightUnit}.
 * This class focuses solely on value comparison and arithmetic,
 * mirroring the QuantityLength design from UC8.</p>
 *
 * <p>Instances are immutable: conversions and additions return new instances
 * rather than modifying the current one, ensuring value-object semantics.</p>
 */
public class QuantityWeight {
    private final double value;
    private final WeightUnit unit;

    /**
     * Constructs a QuantityWeight with the given value and unit.
     *
     * @param value the numeric measurement value
     * @param unit  the unit of measurement (must not be null)
     * @throws IllegalArgumentException if unit is null, or value is NaN / infinite
     */
    public QuantityWeight(double value, WeightUnit unit) {
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
    // Public API: conversion (delegates to WeightUnit)
    // ─────────────────────────────────────────────

    /**
     * Instance method: converts this weight to a new QuantityWeight expressed in the target unit.
     *
     * @param targetUnit the desired target unit (must not be null)
     * @return a new QuantityWeight in the target unit
     * @throws IllegalArgumentException if targetUnit is null
     */
    public QuantityWeight convertTo(WeightUnit targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        double baseValue = this.unit.convertToBaseUnit(this.value);
        double convertedValue = targetUnit.convertFromBaseUnit(baseValue);
        return new QuantityWeight(convertedValue, targetUnit);
    }

    /**
     * Static convenience method: converts a raw numeric value from sourceUnit to targetUnit.
     *
     * @param value      the numeric measurement to convert
     * @param sourceUnit the unit of the input value (must not be null)
     * @param targetUnit the desired output unit (must not be null)
     * @return the converted value in targetUnit
     * @throws IllegalArgumentException if any argument is invalid
     */
    public static double convert(double value, WeightUnit sourceUnit, WeightUnit targetUnit) {
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
    // Public API: Addition (delegates to WeightUnit)
    // ─────────────────────────────────────────────

    /**
     * Instance method to add another weight.
     * The result is returned as a new QuantityWeight in this instance's unit.
     *
     * @param other the weight to add (must not be null)
     * @return a new QuantityWeight representing the sum
     * @throws IllegalArgumentException if the other quantity is null
     */
    public QuantityWeight add(QuantityWeight other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot add a null quantity");
        }
        double sumInBase = this.unit.convertToBaseUnit(this.value)
                         + other.unit.convertToBaseUnit(other.value);
        double sumInTargetUnit = this.unit.convertFromBaseUnit(sumInBase);
        return new QuantityWeight(sumInTargetUnit, this.unit);
    }

    /**
     * Static method to add two weights, returning the result in the specified target unit.
     *
     * @param q1         the first weight to add
     * @param q2         the second weight to add
     * @param targetUnit the desired unit for the result
     * @return a new QuantityWeight representing the sum
     * @throws IllegalArgumentException if any argument is null
     */
    public static QuantityWeight add(QuantityWeight q1, QuantityWeight q2, WeightUnit targetUnit) {
        if (q1 == null || q2 == null || targetUnit == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        double sumInBase = q1.unit.convertToBaseUnit(q1.value)
                         + q2.unit.convertToBaseUnit(q2.value);
        double sumInTargetUnit = targetUnit.convertFromBaseUnit(sumInBase);
        return new QuantityWeight(sumInTargetUnit, targetUnit);
    }

    /**
     * Static method to add two weights, returning the result in the unit of the first operand.
     */
    public static QuantityWeight add(QuantityWeight q1, QuantityWeight q2) {
        if (q1 == null || q2 == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        return add(q1, q2, q1.unit);
    }

    /**
     * Static method to add two raw weight measurements and return the result in a target unit.
     */
    public static QuantityWeight add(double val1, WeightUnit unit1, double val2, WeightUnit unit2, WeightUnit targetUnit) {
        QuantityWeight q1 = new QuantityWeight(val1, unit1);
        QuantityWeight q2 = new QuantityWeight(val2, unit2);
        return add(q1, q2, targetUnit);
    }

    // ─────────────────────────────────────────────
    // Object overrides
    // ─────────────────────────────────────────────

    private static final double EPSILON = 1e-6;

    /**
     * Two QuantityWeight objects are equal if they represent the same physical weight
     * when both are converted to the common base unit (KILOGRAM), within an epsilon.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        QuantityWeight that = (QuantityWeight) obj;
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
     * Human-readable representation, e.g. "QuantityWeight{1.0 KILOGRAM}".
     */
    @Override
    public String toString() {
        return "QuantityWeight{" + value + " " + unit + "}";
    }
}
