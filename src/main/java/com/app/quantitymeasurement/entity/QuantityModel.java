package com.app.quantitymeasurement.entity;

import com.app.quantitymeasurement.unit.IMeasurable;

/**
 * Generic POJO class representing a quantity internally.
 *
 * @param <U> the type of Measurable unit
 */
public class QuantityModel<U extends IMeasurable> {
    private final double value;
    private final U unit;

    public QuantityModel(double value, U unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return "QuantityModel{" +
                "value=" + value +
                ", unit=" + unit +
                '}';
    }
}
