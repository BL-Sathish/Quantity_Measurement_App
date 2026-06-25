package com.quantity.measurement;

/**
 * Custom unchecked exception class for quantity measurement errors.
 */
public class QuantityMeasurementException extends RuntimeException {
    
    public QuantityMeasurementException(String message) {
        super(message);
    }

    public QuantityMeasurementException(String message, Throwable cause) {
        super(message, cause);
    }
}
