package com.quantity.measurement;

public enum LengthUnit {
    FEET(12.0),
    INCH(1.0);

    private final double baseUnitConversionFactor;

    LengthUnit(double baseUnitConversionFactor) {
        this.baseUnitConversionFactor = baseUnitConversionFactor;
    }

    public double getBaseUnitConversionFactor() {
        return baseUnitConversionFactor;
    }
}
