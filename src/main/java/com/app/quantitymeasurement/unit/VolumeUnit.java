package com.app.quantitymeasurement.unit;

/**
 * Standalone enum representing volume units with full conversion responsibility.
 *
 * <p>Each constant stores a conversion factor relative to the base unit (LITRE).
 * The factor represents "how many litres does 1 of this unit equal?".</p>
 *
 * <p>Mirrors the LengthUnit and WeightUnit design, reinforcing consistency
 * across measurement categories.</p>
 */
public enum VolumeUnit implements IMeasurable {

    /** 1 litre = 1 litre (base unit). */
    LITRE(1.0),

    /** 1 millilitre = 0.001 litre. */
    MILLILITRE(0.001),

    /** 1 gallon ≈ 3.78541 litres. */
    GALLON(3.78541);

    // ─────────────────────────────────────────────
    // Instance state
    // ─────────────────────────────────────────────

    private final double conversionFactor;

    VolumeUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    // ─────────────────────────────────────────────
    // Public API
    // ─────────────────────────────────────────────

    /**
     * Returns the conversion factor relative to the base unit (LITRE).
     *
     * @return how many litres one of this unit equals
     */
    public double getConversionFactor() {
        return conversionFactor;
    }

    /**
     * Converts a value expressed in <em>this</em> unit to the base unit (LITRE).
     *
     * <p>Example: {@code VolumeUnit.MILLILITRE.convertToBaseUnit(1000.0)} → 1.0</p>
     *
     * @param value the measurement in this unit
     * @return the equivalent measurement in litres
     */
    public double convertToBaseUnit(double value) {
        return value * conversionFactor;
    }

    /**
     * Converts a base-unit (LITRE) value to <em>this</em> unit.
     *
     * <p>Example: {@code VolumeUnit.MILLILITRE.convertFromBaseUnit(1.0)} → 1000.0</p>
     *
     * @param baseValue the measurement in litres
     * @return the equivalent measurement in this unit
     */
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactor;
    }

    @Override
    public String getMeasurementType() {
        return "Volume";
    }
}
