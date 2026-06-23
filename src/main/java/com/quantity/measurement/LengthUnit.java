package com.quantity.measurement;

public enum LengthUnit {
    FEET(12.0),
    INCH(1.0),
    YARD(36.0),
    CENTIMETER(0.393701);

    private final double baseUnitConversionFactor;

    LengthUnit(double baseUnitConversionFactor) {
        this.baseUnitConversionFactor = baseUnitConversionFactor;
    }

    public double getBaseUnitConversionFactor() {
        return baseUnitConversionFactor;
    }
}
