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
                new QuantityWeight(1.0, WeightUnit.KILOGRAM).equals(new QuantityLength(1.0, LengthUnit.FEET)));
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
        double result = QuantityLength.convert(value, fromUnit, toUnit);
        System.out.printf("Input: convert(%.4f, %s, %s) → Output: %.4f%n", value, fromUnit, toUnit, result);
    }

    public static void demonstrateLengthConversion(QuantityLength length, LengthUnit toUnit) {
        QuantityLength converted = length.convertTo(toUnit);
        System.out.printf("Input: %s.convertTo(%s) → Output: %s%n", length, toUnit, converted);
    }

    public static void demonstrateAddition(double val1, LengthUnit unit1, double val2, LengthUnit unit2) {
        QuantityLength q1 = new QuantityLength(val1, unit1);
        QuantityLength q2 = new QuantityLength(val2, unit2);
        QuantityLength sum = q1.add(q2);
        System.out.printf("Input: add(%s, %s)\nOutput: %s\n\n", q1, q2, sum);
    }

    public static void demonstrateAddition(double val1, LengthUnit unit1, double val2, LengthUnit unit2, LengthUnit targetUnit) {
        QuantityLength q1 = new QuantityLength(val1, unit1);
        QuantityLength q2 = new QuantityLength(val2, unit2);
        QuantityLength sum = QuantityLength.add(q1, q2, targetUnit);
        System.out.printf("Input: add(%s, %s, %s)\nOutput: %s\n\n", q1, q2, targetUnit, sum);
    }

    public static void demonstrateLengthEquality(QuantityLength a, QuantityLength b) {
        System.out.printf("Equality: %s == %s → %b%n", a, b, a.equals(b));
    }

    public static void demonstrateLengthComparison(double val1, LengthUnit unit1, double val2, LengthUnit unit2) {
        QuantityLength a = new QuantityLength(val1, unit1);
        QuantityLength b = new QuantityLength(val2, unit2);
        demonstrateLengthEquality(a, b);
    }

    // ─────────────────────────────────────────────
    // UC9: Weight demos
    // ─────────────────────────────────────────────
    public static void demonstrateWeightEquality(double val1, WeightUnit unit1, double val2, WeightUnit unit2) {
        QuantityWeight a = new QuantityWeight(val1, unit1);
        QuantityWeight b = new QuantityWeight(val2, unit2);
        System.out.printf("Equality: %s == %s → %b%n", a, b, a.equals(b));
    }

    public static void demonstrateWeightConversion(double value, WeightUnit fromUnit, WeightUnit toUnit) {
        QuantityWeight original = new QuantityWeight(value, fromUnit);
        QuantityWeight converted = original.convertTo(toUnit);
        System.out.printf("Input: %s.convertTo(%s) → Output: %s%n", original, toUnit, converted);
    }

    public static void demonstrateWeightAddition(double val1, WeightUnit unit1, double val2, WeightUnit unit2) {
        QuantityWeight q1 = new QuantityWeight(val1, unit1);
        QuantityWeight q2 = new QuantityWeight(val2, unit2);
        QuantityWeight sum = q1.add(q2);
        System.out.printf("Input: add(%s, %s)\nOutput: %s\n\n", q1, q2, sum);
    }

    public static void demonstrateWeightAddition(double val1, WeightUnit unit1, double val2, WeightUnit unit2, WeightUnit targetUnit) {
        QuantityWeight q1 = new QuantityWeight(val1, unit1);
        QuantityWeight q2 = new QuantityWeight(val2, unit2);
        QuantityWeight sum = QuantityWeight.add(q1, q2, targetUnit);
        System.out.printf("Input: add(%s, %s, %s)\nOutput: %s\n\n", q1, q2, targetUnit, sum);
    }
}
