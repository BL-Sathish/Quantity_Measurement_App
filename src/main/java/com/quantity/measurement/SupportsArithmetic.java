package com.quantity.measurement;

/**
 * Functional interface to indicate whether a measurable unit supports arithmetic operations.
 */
@FunctionalInterface
public interface SupportsArithmetic {
    boolean isSupported();
}
