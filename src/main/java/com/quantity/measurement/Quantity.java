package com.quantity.measurement;

import java.util.Objects;

/**
 * Represents an immutable measurement with a specific unit.
 * Supports equality comparison, unit conversion, addition, subtraction, and division.
 *
 * <p>All unit-specific conversion logic is delegated to the generic unit U,
 * which must implement {@link IMeasurable}.</p>
 *
 * <p>Subtraction returns a new {@code Quantity<U>} with the difference.
 * Division returns a dimensionless {@code double} representing the ratio.</p>
 *
 * @param <U> The type of the measurement unit (e.g. LengthUnit, WeightUnit)
 */
public class Quantity<U extends IMeasurable> {
    private final double value;
    private final U unit;

    /**
     * Constructs a Quantity with the given value and unit.
     *
     * @param value the numeric measurement value
     * @param unit  the unit of measurement (must not be null)
     * @throws IllegalArgumentException if unit is null, or value is NaN / infinite
     */
    public Quantity(double value, U unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be a finite number, but was: " + value);
        }
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }


    // ─────────────────────────────────────────────
    // Public API: conversion (delegates to U)
    // ─────────────────────────────────────────────

    /**
     * Converts this quantity to a new Quantity expressed in the target unit.
     *
     * @param targetUnit the desired target unit (must not be null)
     * @return a new Quantity in the target unit
     * @throws IllegalArgumentException if targetUnit is null
     */
    public Quantity<U> convertTo(U targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        double baseValue = this.unit.convertToBaseUnit(this.value);
        double convertedValue = targetUnit.convertFromBaseUnit(baseValue);
        if (this.unit instanceof TemperatureUnit) {
            convertedValue = roundToTwoDecimals(convertedValue);
        }
        return new Quantity<>(convertedValue, targetUnit);
    }

    /**
     * Static convenience method: converts a raw numeric value from sourceUnit to targetUnit.
     *
     * @param value      the numeric measurement to convert
     * @param sourceUnit the unit of the input value (must not be null)
     * @param targetUnit the desired output unit (must not be null)
     * @param <U>        the unit type
     * @return the converted value in targetUnit
     * @throws IllegalArgumentException if any argument is invalid
     */
    public static <U extends IMeasurable> double convert(double value, U sourceUnit, U targetUnit) {
        if (sourceUnit == null || targetUnit == null) {
            throw new IllegalArgumentException("Source and target units cannot be null");
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be a finite number, but was: " + value);
        }
        double baseValue = sourceUnit.convertToBaseUnit(value);
        double convertedValue = targetUnit.convertFromBaseUnit(baseValue);
        if (sourceUnit instanceof TemperatureUnit) {
            convertedValue = roundToTwoDecimals(convertedValue);
        }
        return convertedValue;
    }

    // ─────────────────────────────────────────────
    // Public API: Addition
    // ─────────────────────────────────────────────

    /**
     * Adds another quantity to this one.
     * The result is returned as a new Quantity in this instance's unit.
     *
     * @param other the quantity to add (must not be null)
     * @return a new Quantity representing the sum
     * @throws IllegalArgumentException if the other quantity is null
     */
    public Quantity<U> add(Quantity<U> other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot add a null quantity");
        }
        double baseResult = performBaseArithmetic(other, ArithmeticOperation.ADD, null, false);
        double resultInTarget = this.unit.convertFromBaseUnit(baseResult);
        return new Quantity<>(resultInTarget, this.unit);
    }

    /**
     * Adds two quantities, returning the result in the specified target unit.
     */
    public static <U extends IMeasurable> Quantity<U> add(Quantity<U> q1, Quantity<U> q2, U targetUnit) {
        if (q1 == null || q2 == null || targetUnit == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        double baseResult = q1.performBaseArithmetic(q2, ArithmeticOperation.ADD, targetUnit, true);
        double resultInTarget = targetUnit.convertFromBaseUnit(baseResult);
        return new Quantity<>(resultInTarget, targetUnit);
    }

    /**
     * Adds two quantities, returning the result in the unit of the first operand.
     */
    public static <U extends IMeasurable> Quantity<U> add(Quantity<U> q1, Quantity<U> q2) {
        if (q1 == null || q2 == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        return add(q1, q2, q1.unit);
    }

    /**
     * Adds two raw measurements and returns the result in a target unit.
     */
    public static <U extends IMeasurable> Quantity<U> add(double val1, U unit1, double val2, U unit2, U targetUnit) {
        Quantity<U> q1 = new Quantity<>(val1, unit1);
        Quantity<U> q2 = new Quantity<>(val2, unit2);
        return add(q1, q2, targetUnit);
    }

    // ─────────────────────────────────────────────
    // Public API: Subtraction
    // ─────────────────────────────────────────────

    /**
     * Subtracts another quantity from this one.
     * The result is returned as a new Quantity in this instance's unit,
     * rounded to two decimal places.
     *
     * <p>Subtraction is non-commutative: {@code A.subtract(B) ≠ B.subtract(A)}.
     * A positive result means this quantity is larger; a negative result means
     * the other quantity is larger; zero means they are equivalent.</p>
     *
     * <p>Example: {@code new Quantity<>(10.0, FEET).subtract(new Quantity<>(6.0, INCHES))}
     * returns {@code Quantity(9.5, FEET)}.</p>
     *
     * @param other the quantity to subtract (must not be null)
     * @return a new Quantity representing the difference, in this instance's unit
     * @throws IllegalArgumentException if the other quantity is null
     */
    public Quantity<U> subtract(Quantity<U> other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot subtract a null quantity");
        }
        double baseResult = performBaseArithmetic(other, ArithmeticOperation.SUBTRACT, null, false);
        double resultInTarget = this.unit.convertFromBaseUnit(baseResult);
        double rounded = roundToTwoDecimals(resultInTarget);
        return new Quantity<>(rounded, this.unit);
    }

    /**
     * Subtracts another quantity from this one, returning the result
     * in the specified target unit, rounded to two decimal places.
     *
     * <p>Example: {@code new Quantity<>(10.0, FEET).subtract(new Quantity<>(6.0, INCHES), INCHES)}
     * returns {@code Quantity(114.0, INCHES)}.</p>
     *
     * @param other      the quantity to subtract (must not be null)
     * @param targetUnit the desired unit for the result (must not be null)
     * @return a new Quantity representing the difference, in the target unit
     * @throws IllegalArgumentException if any argument is null
     */
    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot subtract a null quantity");
        }
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        double baseResult = performBaseArithmetic(other, ArithmeticOperation.SUBTRACT, targetUnit, true);
        double resultInTarget = targetUnit.convertFromBaseUnit(baseResult);
        double rounded = roundToTwoDecimals(resultInTarget);
        return new Quantity<>(rounded, targetUnit);
    }

    // ─────────────────────────────────────────────
    // Public API: Division
    // ─────────────────────────────────────────────

    /**
     * Divides this quantity by another, returning a dimensionless scalar ratio.
     *
     * <p>Both quantities are converted to the common base unit before division.
     * The result represents how many times this quantity is larger than the other.</p>
     *
     * <p>Division is non-commutative: {@code A.divide(B)} is the reciprocal of
     * {@code B.divide(A)}.</p>
     *
     * <p>Examples:
     * <ul>
     *   <li>{@code new Quantity<>(10.0, FEET).divide(new Quantity<>(2.0, FEET))} → 5.0</li>
     *   <li>{@code new Quantity<>(24.0, INCHES).divide(new Quantity<>(2.0, FEET))} → 1.0</li>
     * </ul></p>
     *
     * @param other the divisor quantity (must not be null, must not be zero)
     * @return the dimensionless ratio (this ÷ other)
     * @throws IllegalArgumentException if the other quantity is null
     * @throws ArithmeticException      if the other quantity is zero (division by zero)
     */
    public double divide(Quantity<U> other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot divide by a null quantity");
        }
        double baseResult = performBaseArithmetic(other, ArithmeticOperation.DIVIDE, null, false);
        return baseResult;
    }

    // ─────────────────────────────────────────────
    // Internal helper methods and enums
    // ─────────────────────────────────────────────

    private double performBaseArithmetic(Quantity<U> other, ArithmeticOperation op, U targetUnit, boolean useTarget) {
        if (other == null) {
            throw new IllegalArgumentException("Other quantity cannot be null");
        }
        if (useTarget && targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        if (this.unit.getClass() != other.unit.getClass()) {
            throw new IllegalArgumentException("Cannot perform arithmetic on different unit types");
        }
        // Validate that the operation is supported by the unit (e.g., Temperature may reject)
        String operationName = op.name().toLowerCase();
        this.unit.validateOperationSupport(operationName);
        other.unit.validateOperationSupport(operationName);
        double thisBase = this.unit.convertToBaseUnit(this.value);
        double otherBase = other.unit.convertToBaseUnit(other.value);
        return op.compute(thisBase, otherBase);
    }

    // Helper method for rounding to two decimal places, handling floating-point edge cases
    private static double roundToTwoDecimals(double value) {
        // Precise rounding to two decimal places using BigDecimal
        return new java.math.BigDecimal(Double.toString(value))
                .setScale(2, java.math.RoundingMode.HALF_UP)
                .doubleValue();
    }



    private enum ArithmeticOperation {
        ADD {
            @Override
            double compute(double leftBase, double rightBase) {
                return leftBase + rightBase;
            }
        },
        SUBTRACT {
            @Override
            double compute(double leftBase, double rightBase) {
                return leftBase - rightBase;
            }
        },
        DIVIDE {
            @Override
            double compute(double leftBase, double rightBase) {
                if (Math.abs(rightBase) < EPSILON) {
                    throw new ArithmeticException("Cannot divide by zero quantity");
                }
                return leftBase / rightBase;
            }
        };

        abstract double compute(double leftBase, double rightBase);
    }

    // ─────────────────────────────────────────────
    // Object overrides
    // ─────────────────────────────────────────────

    private static final double EPSILON = 1e-6;

    /**
     * Two Quantity objects are equal if they represent the same physical amount
     * when both are converted to the common base unit, within an epsilon,
     * AND they belong to the same measurement category (i.e., same unit class).
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Quantity<?> that = (Quantity<?>) obj;
        
        // Prevent cross-category matches (e.g. Length vs Weight)
        if (this.unit.getClass() != that.unit.getClass()) {
            return false;
        }

        double thisBase = this.unit.convertToBaseUnit(this.value);
        double thatBase = ((IMeasurable) that.unit).convertToBaseUnit(that.value);
        
        return Math.abs(thisBase - thatBase) <= EPSILON;
    }

    @Override
    public int hashCode() {
        double base = this.unit.convertToBaseUnit(this.value);
        return Objects.hash(this.unit.getClass(), Math.round(base / EPSILON));
    }

    @Override
    public String toString() {
        return "Quantity{" + value + " " + unit + "}";
    }
}
