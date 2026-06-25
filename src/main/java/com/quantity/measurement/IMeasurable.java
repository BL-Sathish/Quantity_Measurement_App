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

    /**
     * Lambda expression/interface representing the default behavior of supporting arithmetic operations.
     */
    SupportsArithmetic supportsArithmetic = () -> true;

    /**
     * Indicates whether this unit supports arithmetic operations.
     * Default implementation delegates to the functional interface lambda.
     */
    default boolean supportsArithmetic() {
        return supportsArithmetic.isSupported();
    }

    /**
     * Checks if this unit supports addition.
     */
    default boolean supportsAddition() {
        return supportsArithmetic();
    }

    /**
     * Checks if this unit supports subtraction.
     */
    default boolean supportsSubtraction() {
        return supportsArithmetic();
    }

    /**
     * Checks if this unit supports division.
     */
    default boolean supportsDivision() {
        return supportsArithmetic();
    }

    /**
     * Validates that a given arithmetic operation is supported by this unit.
     * Implementations may throw UnsupportedOperationException for unsupported operations.
     * Default implementation does nothing (all operations supported).
     */
    default void validateOperationSupport(String operation) {
        // No-op for units that support all operations.
    }
}
