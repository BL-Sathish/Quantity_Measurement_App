package com.app.quantitymeasurement.exception;

/**
 * Custom exception to represent database connectivity or transaction errors.
 * Extends RuntimeException for unchecked exception behavior.
 */
public class DatabaseException extends RuntimeException {

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
