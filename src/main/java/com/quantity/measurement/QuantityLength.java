package com.quantity.measurement;

import java.util.Objects;

public class QuantityLength {
    private final double value;
    private final LengthUnit unit;

    public QuantityLength(double value, LengthUnit unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        this.value = value;
        this.unit = unit;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        QuantityLength that = (QuantityLength) obj;
        
        double thisBaseValue = this.value * this.unit.getBaseUnitConversionFactor();
        double thatBaseValue = that.value * that.unit.getBaseUnitConversionFactor();
        
        return Double.compare(thisBaseValue, thatBaseValue) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, unit);
    }
}
