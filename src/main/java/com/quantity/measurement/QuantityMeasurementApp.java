package com.quantity.measurement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QuantityMeasurementApp {
    public static void main(String[] args) {
        SpringApplication.run(QuantityMeasurementApp.class, args);

        // UC6: Addition demonstration
        System.out.println("\n=== UC6: Addition Demo ===");
        demonstrateAddition(1.0, LengthUnit.FEET, 2.0, LengthUnit.FEET);
        demonstrateAddition(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCH);
        demonstrateAddition(12.0, LengthUnit.INCH, 1.0, LengthUnit.FEET);
        demonstrateAddition(1.0, LengthUnit.YARD, 3.0, LengthUnit.FEET);
        demonstrateAddition(36.0, LengthUnit.INCH, 1.0, LengthUnit.YARD);
        demonstrateAddition(2.54, LengthUnit.CENTIMETER, 1.0, LengthUnit.INCH);
        demonstrateAddition(5.0, LengthUnit.FEET, 0.0, LengthUnit.INCH);
        demonstrateAddition(5.0, LengthUnit.FEET, -2.0, LengthUnit.FEET);

        // UC7: Addition with explicit target unit demonstration
        System.out.println("\n=== UC7: Addition with Target Unit Demo ===");
        demonstrateAddition(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCH, LengthUnit.FEET);
        demonstrateAddition(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCH, LengthUnit.INCH);
        demonstrateAddition(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCH, LengthUnit.YARD);

        // UC8: Refactored LengthUnit conversion demo
        System.out.println("\n=== UC8: Standalone LengthUnit Conversion Demo ===");
        demonstrateUnitConversion(LengthUnit.FEET, 12.0);
        demonstrateUnitConversion(LengthUnit.INCH, 12.0);
        demonstrateUnitConversion(LengthUnit.YARD, 1.0);
        demonstrateUnitConversion(LengthUnit.CENTIMETER, 30.48);

        // ─────────────────────────────────────────────
        // UC9: Weight Measurement Demonstrations
        // ─────────────────────────────────────────────

        System.out.println("\n=== UC9: Weight Equality Demo ===");
        demonstrateWeightEquality(1.0, WeightUnit.KILOGRAM, 1.0, WeightUnit.KILOGRAM);
        demonstrateWeightEquality(1.0, WeightUnit.KILOGRAM, 1000.0, WeightUnit.GRAM);
        demonstrateWeightEquality(2.0, WeightUnit.POUND, 2.0, WeightUnit.POUND);
        demonstrateWeightEquality(1.0, WeightUnit.KILOGRAM, 2.20462, WeightUnit.POUND);
        demonstrateWeightEquality(500.0, WeightUnit.GRAM, 0.5, WeightUnit.KILOGRAM);
        demonstrateWeightEquality(1.0, WeightUnit.POUND, 453.592, WeightUnit.GRAM);

        System.out.println("\n=== UC9: Weight Conversion Demo ===");
        demonstrateWeightConversion(1.0, WeightUnit.KILOGRAM, WeightUnit.GRAM);
        demonstrateWeightConversion(2.0, WeightUnit.POUND, WeightUnit.KILOGRAM);
        demonstrateWeightConversion(500.0, WeightUnit.GRAM, WeightUnit.POUND);
        demonstrateWeightConversion(0.0, WeightUnit.KILOGRAM, WeightUnit.GRAM);

        System.out.println("\n=== UC9: Weight Addition Demo ===");
        demonstrateWeightAddition(1.0, WeightUnit.KILOGRAM, 2.0, WeightUnit.KILOGRAM);
        demonstrateWeightAddition(1.0, WeightUnit.KILOGRAM, 1000.0, WeightUnit.GRAM);
        demonstrateWeightAddition(500.0, WeightUnit.GRAM, 0.5, WeightUnit.KILOGRAM);

        System.out.println("\n=== UC9: Weight Addition with Target Unit Demo ===");
        demonstrateWeightAddition(1.0, WeightUnit.KILOGRAM, 1000.0, WeightUnit.GRAM, WeightUnit.GRAM);
        demonstrateWeightAddition(1.0, WeightUnit.POUND, 453.592, WeightUnit.GRAM, WeightUnit.POUND);
        demonstrateWeightAddition(2.0, WeightUnit.KILOGRAM, 4.0, WeightUnit.POUND, WeightUnit.KILOGRAM);

        System.out.println("\n=== UC9: Category Incompatibility Demo ===");
        System.out.printf("Quantity(1.0, KILOGRAM).equals(Quantity(1.0, FEET)) → %b%n",
                new Quantity<>(1.0, WeightUnit.KILOGRAM).equals(new Quantity<>(1.0, LengthUnit.FEET)));

        // ─────────────────────────────────────────────
        // UC11: Volume Measurement Demonstrations
        // ─────────────────────────────────────────────

        System.out.println("\n=== UC11: Volume Equality Demo ===");
        demonstrateVolumeEquality(1.0, VolumeUnit.LITRE, 1.0, VolumeUnit.LITRE);
        demonstrateVolumeEquality(1.0, VolumeUnit.LITRE, 1000.0, VolumeUnit.MILLILITRE);
        demonstrateVolumeEquality(1.0, VolumeUnit.GALLON, 1.0, VolumeUnit.GALLON);
        demonstrateVolumeEquality(1.0, VolumeUnit.LITRE, 0.264172, VolumeUnit.GALLON);
        demonstrateVolumeEquality(500.0, VolumeUnit.MILLILITRE, 0.5, VolumeUnit.LITRE);
        demonstrateVolumeEquality(3.78541, VolumeUnit.LITRE, 1.0, VolumeUnit.GALLON);

        System.out.println("\n=== UC11: Volume Conversion Demo ===");
        demonstrateVolumeConversion(1.0, VolumeUnit.LITRE, VolumeUnit.MILLILITRE);
        demonstrateVolumeConversion(2.0, VolumeUnit.GALLON, VolumeUnit.LITRE);
        demonstrateVolumeConversion(500.0, VolumeUnit.MILLILITRE, VolumeUnit.GALLON);
        demonstrateVolumeConversion(0.0, VolumeUnit.LITRE, VolumeUnit.MILLILITRE);
        demonstrateVolumeConversion(1.0, VolumeUnit.LITRE, VolumeUnit.LITRE);

        System.out.println("\n=== UC11: Volume Addition Demo ===");
        demonstrateVolumeAddition(1.0, VolumeUnit.LITRE, 2.0, VolumeUnit.LITRE);
        demonstrateVolumeAddition(1.0, VolumeUnit.LITRE, 1000.0, VolumeUnit.MILLILITRE);
        demonstrateVolumeAddition(500.0, VolumeUnit.MILLILITRE, 0.5, VolumeUnit.LITRE);
        demonstrateVolumeAddition(2.0, VolumeUnit.GALLON, 3.78541, VolumeUnit.LITRE);

        System.out.println("\n=== UC11: Volume Addition with Target Unit Demo ===");
        demonstrateVolumeAddition(1.0, VolumeUnit.LITRE, 1000.0, VolumeUnit.MILLILITRE, VolumeUnit.MILLILITRE);
        demonstrateVolumeAddition(1.0, VolumeUnit.GALLON, 3.78541, VolumeUnit.LITRE, VolumeUnit.GALLON);
        demonstrateVolumeAddition(500.0, VolumeUnit.MILLILITRE, 1.0, VolumeUnit.LITRE, VolumeUnit.GALLON);
        demonstrateVolumeAddition(2.0, VolumeUnit.LITRE, 4.0, VolumeUnit.GALLON, VolumeUnit.LITRE);

        System.out.println("\n=== UC11: Category Incompatibility Demo ===");
        System.out.printf("Quantity(1.0, LITRE).equals(Quantity(1.0, FEET)) → %b%n",
                new Quantity<>(1.0, VolumeUnit.LITRE).equals(new Quantity<>(1.0, LengthUnit.FEET)));
        System.out.printf("Quantity(1.0, LITRE).equals(Quantity(1.0, KILOGRAM)) → %b%n",
                new Quantity<>(1.0, VolumeUnit.LITRE).equals(new Quantity<>(1.0, WeightUnit.KILOGRAM)));
    }

    // ─────────────────────────────────────────────
    // UC8: LengthUnit direct conversion demo
    // ─────────────────────────────────────────────
    public static void demonstrateUnitConversion(LengthUnit unit, double value) {
        double toBase   = unit.convertToBaseUnit(value);
        double fromBase = unit.convertFromBaseUnit(toBase);
        System.out.printf("Input: LengthUnit.%s.convertToBaseUnit(%.4f) → Output: %.4f%n", unit, value, toBase);
        System.out.printf("Input: LengthUnit.%s.convertFromBaseUnit(%.4f) → Output: %.4f%n%n", unit, toBase, fromBase);
    }

    // ─────────────────────────────────────────────
    // Length: Conversion, Equality, Addition
    // ─────────────────────────────────────────────
    public static void demonstrateLengthConversion(double value, LengthUnit fromUnit, LengthUnit toUnit) {
        double result = Quantity.convert(value, fromUnit, toUnit);
        System.out.printf("Input: convert(%.4f, %s, %s) → Output: %.4f%n", value, fromUnit, toUnit, result);
    }

    public static void demonstrateLengthConversion(Quantity<LengthUnit> length, LengthUnit toUnit) {
        Quantity<LengthUnit> converted = length.convertTo(toUnit);
        System.out.printf("Input: %s.convertTo(%s) → Output: %s%n", length, toUnit, converted);
    }

    public static void demonstrateAddition(double val1, LengthUnit unit1, double val2, LengthUnit unit2) {
        Quantity<LengthUnit> q1 = new Quantity<>(val1, unit1);
        Quantity<LengthUnit> q2 = new Quantity<>(val2, unit2);
        Quantity<LengthUnit> sum = q1.add(q2);
        System.out.printf("Input: add(%s, %s)\nOutput: %s\n\n", q1, q2, sum);
    }

    public static void demonstrateAddition(double val1, LengthUnit unit1, double val2, LengthUnit unit2, LengthUnit targetUnit) {
        Quantity<LengthUnit> q1 = new Quantity<>(val1, unit1);
        Quantity<LengthUnit> q2 = new Quantity<>(val2, unit2);
        Quantity<LengthUnit> sum = Quantity.add(q1, q2, targetUnit);
        System.out.printf("Input: add(%s, %s, %s)\nOutput: %s\n\n", q1, q2, targetUnit, sum);
    }

    public static void demonstrateLengthEquality(Quantity<LengthUnit> a, Quantity<LengthUnit> b) {
        System.out.printf("Equality: %s == %s → %b%n", a, b, a.equals(b));
    }

    public static void demonstrateLengthComparison(double val1, LengthUnit unit1, double val2, LengthUnit unit2) {
        Quantity<LengthUnit> a = new Quantity<>(val1, unit1);
        Quantity<LengthUnit> b = new Quantity<>(val2, unit2);
        demonstrateLengthEquality(a, b);
    }

    // ─────────────────────────────────────────────
    // UC9: Weight demos
    // ─────────────────────────────────────────────
    public static void demonstrateWeightEquality(double val1, WeightUnit unit1, double val2, WeightUnit unit2) {
        Quantity<WeightUnit> a = new Quantity<>(val1, unit1);
        Quantity<WeightUnit> b = new Quantity<>(val2, unit2);
        System.out.printf("Equality: %s == %s → %b%n", a, b, a.equals(b));
    }

    public static void demonstrateWeightConversion(double value, WeightUnit fromUnit, WeightUnit toUnit) {
        Quantity<WeightUnit> original = new Quantity<>(value, fromUnit);
        Quantity<WeightUnit> converted = original.convertTo(toUnit);
        System.out.printf("Input: %s.convertTo(%s) → Output: %s%n", original, toUnit, converted);
    }

    public static void demonstrateWeightAddition(double val1, WeightUnit unit1, double val2, WeightUnit unit2) {
        Quantity<WeightUnit> q1 = new Quantity<>(val1, unit1);
        Quantity<WeightUnit> q2 = new Quantity<>(val2, unit2);
        Quantity<WeightUnit> sum = q1.add(q2);
        System.out.printf("Input: add(%s, %s)\nOutput: %s\n\n", q1, q2, sum);
    }

    public static void demonstrateWeightAddition(double val1, WeightUnit unit1, double val2, WeightUnit unit2, WeightUnit targetUnit) {
        Quantity<WeightUnit> q1 = new Quantity<>(val1, unit1);
        Quantity<WeightUnit> q2 = new Quantity<>(val2, unit2);
        Quantity<WeightUnit> sum = Quantity.add(q1, q2, targetUnit);
        System.out.printf("Input: add(%s, %s, %s)\nOutput: %s\n\n", q1, q2, targetUnit, sum);
    }

    // ─────────────────────────────────────────────
    // UC11: Volume demos
    // ─────────────────────────────────────────────
    public static void demonstrateVolumeEquality(double val1, VolumeUnit unit1, double val2, VolumeUnit unit2) {
        Quantity<VolumeUnit> a = new Quantity<>(val1, unit1);
        Quantity<VolumeUnit> b = new Quantity<>(val2, unit2);
        System.out.printf("Equality: %s == %s → %b%n", a, b, a.equals(b));
    }

    public static void demonstrateVolumeConversion(double value, VolumeUnit fromUnit, VolumeUnit toUnit) {
        Quantity<VolumeUnit> original = new Quantity<>(value, fromUnit);
        Quantity<VolumeUnit> converted = original.convertTo(toUnit);
        System.out.printf("Input: %s.convertTo(%s) → Output: %s%n", original, toUnit, converted);
    }

    public static void demonstrateVolumeAddition(double val1, VolumeUnit unit1, double val2, VolumeUnit unit2) {
        Quantity<VolumeUnit> q1 = new Quantity<>(val1, unit1);
        Quantity<VolumeUnit> q2 = new Quantity<>(val2, unit2);
        Quantity<VolumeUnit> sum = q1.add(q2);
        System.out.printf("Input: add(%s, %s)\nOutput: %s\n\n", q1, q2, sum);
    }

    public static void demonstrateVolumeAddition(double val1, VolumeUnit unit1, double val2, VolumeUnit unit2, VolumeUnit targetUnit) {
        Quantity<VolumeUnit> q1 = new Quantity<>(val1, unit1);
        Quantity<VolumeUnit> q2 = new Quantity<>(val2, unit2);
        Quantity<VolumeUnit> sum = Quantity.add(q1, q2, targetUnit);
        System.out.printf("Input: add(%s, %s, %s)\nOutput: %s\n\n", q1, q2, targetUnit, sum);
    }
}
