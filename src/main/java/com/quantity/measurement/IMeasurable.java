package com.quantity.measurement;

/**
 * Common interface for measurement units across different categories.
 * Enforces the presence of conversion methods relative to a base unit.
 */
public interface IMeasurable {
    /**
     * Converts a value expressed in this unit to the category's base unit.
     *
     * @param value the measurement in this unit
     * @return the equivalent measurement in the base unit
     */
    double convertToBaseUnit(double value);

    /**
     * Converts a base-unit value to this unit.
     *
     * @param baseValue the measurement in the base unit
     * @return the equivalent measurement in this unit
     */
    double convertFromBaseUnit(double baseValue);
}
