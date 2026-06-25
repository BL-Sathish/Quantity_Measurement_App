package com.app.quantitymeasurement.unit;

/**
 * Functional interface to indicate whether a measurable unit supports arithmetic operations.
 */
@FunctionalInterface
public interface SupportsArithmetic {
    boolean isSupported();
}
