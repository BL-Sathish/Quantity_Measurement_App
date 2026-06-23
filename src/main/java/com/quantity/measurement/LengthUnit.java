package com.quantity.measurement;

/**
 * Standalone enum representing length units with full conversion responsibility.
 *
 * <p>Each constant stores a conversion factor relative to the base unit (FEET).
 * The factor represents "how many feet does 1 of this unit equal?".</p>
 *
 * <p>Conversion responsibilities are centralised here, following the
 * Single Responsibility Principle. QuantityLength delegates all
 * unit-specific logic to this enum.</p>
 *
 * <p>This design eliminates circular dependencies and establishes a scalable
 * pattern for future measurement categories (WeightUnit, VolumeUnit, etc.).</p>
 */
public enum LengthUnit {

    /** 1 foot = 1 foot (base unit). */
    FEET(1.0),

    /** 1 inch = 1/12 foot. */
    INCH(1.0 / 12.0),

    /** 1 yard = 3 feet. */
    YARD(3.0),

    /** 1 centimetre = 1/30.48 foot (since 1 foot = 30.48 cm). */
    CENTIMETER(1.0 / 30.48);

    // ─────────────────────────────────────────────
    // Instance state
    // ─────────────────────────────────────────────

    /**
     * How many base units (feet) one of this unit equals.
     * E.g. YARD → 3.0, INCH → 0.08333…
     */
    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    // ─────────────────────────────────────────────
    // Public API
    // ─────────────────────────────────────────────

    /**
     * Returns the conversion factor relative to the base unit (FEET).
     *
     * @return how many feet one of this unit equals
     */
    public double getConversionFactor() {
        return conversionFactor;
    }

    /**
     * Converts a value expressed in <em>this</em> unit to the base unit (FEET).
     *
     * <p>Example: {@code LengthUnit.INCH.convertToBaseUnit(12.0)} → 1.0</p>
     *
     * @param value the measurement in this unit
     * @return the equivalent measurement in feet
     */
    public double convertToBaseUnit(double value) {
        return value * conversionFactor;
    }

    /**
     * Converts a base-unit (FEET) value to <em>this</em> unit.
     *
     * <p>Example: {@code LengthUnit.INCH.convertFromBaseUnit(1.0)} → 12.0</p>
     *
     * @param baseValue the measurement in feet
     * @return the equivalent measurement in this unit
     */
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactor;
    }
}
