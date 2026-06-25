package com.quantity.measurement;

import java.util.function.Function;

/**
 * Standalone enum representing temperature units (Celsius, Fahrenheit, Kelvin).
 * Does not support arithmetic operations by default.
 */
public enum TemperatureUnit implements IMeasurable {

    /** Celsius temperature unit (base unit). */
    CELSIUS(celsius -> celsius, celsius -> celsius),

    /** Fahrenheit temperature unit. */
    FAHRENHEIT(fahrenheit -> (fahrenheit - 32.0) * 5.0 / 9.0, celsius -> (celsius * 9.0 / 5.0) + 32.0),

    /** Kelvin temperature unit. */
    KELVIN(kelvin -> kelvin - 273.15, celsius -> celsius + 273.15);

    private final Function<Double, Double> toBase;
    private final Function<Double, Double> fromBase;
    private final SupportsArithmetic supportsArithmetic = () -> false;

    TemperatureUnit(Function<Double, Double> toBase, Function<Double, Double> fromBase) {
        this.toBase = toBase;
        this.fromBase = fromBase;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return toBase.apply(value);
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return fromBase.apply(baseValue);
    }

    @Override
    public boolean supportsArithmetic() {
        return supportsArithmetic.isSupported();
    }

    @Override
    public void validateOperationSupport(String operation) {
        String op = operation.toLowerCase();
        String opName;
        if (op.contains("add")) {
            opName = "addition";
        } else if (op.contains("sub")) {
            opName = "subtraction";
        } else if (op.contains("div")) {
            opName = "division";
        } else {
            opName = operation;
        }
        throw new UnsupportedOperationException("Temperature does not support " + opName + " operation");
    }

    /**
     * Required by some test cases/validation.
     * @return the name of this unit
     */
    public String getUnitName() {
        return this.name();
    }

    /**
     * Required by some test cases/validation.
     * Throws UnsupportedOperationException as temperature conversions are non-linear.
     */
    public double getConversionFactor() {
        throw new UnsupportedOperationException("Temperature conversions are non-linear and do not use a constant conversion factor");
    }

    @Override
    public String getMeasurementType() {
        return "Temperature";
    }
}
