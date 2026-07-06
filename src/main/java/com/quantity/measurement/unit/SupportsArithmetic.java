package com.quantity.measurement.unit;

/**
 * Functional interface to indicate whether a measurable unit supports arithmetic operations.
 */
@FunctionalInterface
public interface SupportsArithmetic {
    boolean isSupported();
}
