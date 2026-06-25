package com.quantity.measurement;

/**
 * Standalone enum representing weight units with full conversion responsibility.
 *
 * <p>Each constant stores a conversion factor relative to the base unit (KILOGRAM).
 * The factor represents "how many kilograms does 1 of this unit equal?".</p>
 *
 * <p>Mirrors the LengthUnit design from UC8, reinforcing consistency
 * across measurement categories.</p>
 */
public enum WeightUnit implements IMeasurable {

    /** 1 kilogram = 1 kilogram (base unit). */
    KILOGRAM(1.0),

    /** 1 gram = 0.001 kilogram. */
    GRAM(0.001),

    /** 1 pound ≈ 0.453592 kilogram. */
    POUND(0.453592);

    // ─────────────────────────────────────────────
    // Instance state
    // ─────────────────────────────────────────────

    private final double conversionFactor;

    WeightUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    // ─────────────────────────────────────────────
    // Public API
    // ─────────────────────────────────────────────

    /**
     * Returns the conversion factor relative to the base unit (KILOGRAM).
     *
     * @return how many kilograms one of this unit equals
     */
    public double getConversionFactor() {
        return conversionFactor;
    }

    /**
     * Converts a value expressed in <em>this</em> unit to the base unit (KILOGRAM).
     *
     * <p>Example: {@code WeightUnit.GRAM.convertToBaseUnit(1000.0)} → 1.0</p>
     *
     * @param value the measurement in this unit
     * @return the equivalent measurement in kilograms
     */
    public double convertToBaseUnit(double value) {
        return value * conversionFactor;
    }

    /**
     * Converts a base-unit (KILOGRAM) value to <em>this</em> unit.
     *
     * <p>Example: {@code WeightUnit.GRAM.convertFromBaseUnit(1.0)} → 1000.0</p>
     *
     * @param baseValue the measurement in kilograms
     * @return the equivalent measurement in this unit
     */
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactor;
    }

    @Override
    public String getMeasurementType() {
        return "Weight";
    }
}
