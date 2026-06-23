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
        demonstrateAddition(1.0, LengthUnit.YARD, 3.0, LengthUnit.FEET, LengthUnit.YARD);
        demonstrateAddition(36.0, LengthUnit.INCH, 1.0, LengthUnit.YARD, LengthUnit.FEET);
        demonstrateAddition(2.54, LengthUnit.CENTIMETER, 1.0, LengthUnit.INCH, LengthUnit.CENTIMETER);
        demonstrateAddition(5.0, LengthUnit.FEET, 0.0, LengthUnit.INCH, LengthUnit.YARD);
        demonstrateAddition(5.0, LengthUnit.FEET, -2.0, LengthUnit.FEET, LengthUnit.INCH);

        // UC8: Refactored design — LengthUnit conversion responsibility demo
        System.out.println("\n=== UC8: Standalone LengthUnit Conversion Demo ===");
        demonstrateUnitConversion(LengthUnit.FEET, 12.0);
        demonstrateUnitConversion(LengthUnit.INCH, 12.0);
        demonstrateUnitConversion(LengthUnit.YARD, 1.0);
        demonstrateUnitConversion(LengthUnit.CENTIMETER, 30.48);

        System.out.println("\n--- Refactored API backward compatibility ---");
        demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCH);
        demonstrateLengthConversion(new QuantityLength(1.0, LengthUnit.FEET), LengthUnit.INCH);
        demonstrateLengthConversion(1.0, LengthUnit.YARD, LengthUnit.FEET);

        System.out.println("\n--- Equality via refactored delegation ---");
        demonstrateLengthEquality(
                new QuantityLength(36.0, LengthUnit.INCH),
                new QuantityLength(1.0, LengthUnit.YARD));
        demonstrateLengthEquality(
                new QuantityLength(1.0, LengthUnit.FEET),
                new QuantityLength(12.0, LengthUnit.INCH));

        System.out.println("\n--- Addition via refactored delegation ---");
        demonstrateAddition(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCH, LengthUnit.FEET);
        demonstrateAddition(1.0, LengthUnit.YARD, 3.0, LengthUnit.FEET, LengthUnit.YARD);
    }

    // ─────────────────────────────────────────────
    // UC8: LengthUnit direct conversion demo
    // ─────────────────────────────────────────────
    /**
     * Demonstrates LengthUnit's own convertToBaseUnit and convertFromBaseUnit methods.
     */
    public static void demonstrateUnitConversion(LengthUnit unit, double value) {
        double toBase   = unit.convertToBaseUnit(value);
        double fromBase = unit.convertFromBaseUnit(toBase);
        System.out.printf("Input: LengthUnit.%s.convertToBaseUnit(%.4f) → Output: %.4f%n", unit, value, toBase);
        System.out.printf("Input: LengthUnit.%s.convertFromBaseUnit(%.4f) → Output: %.4f%n%n", unit, toBase, fromBase);
    }

    // ─────────────────────────────────────────────
    // Overloaded demonstrateLengthConversion
    // ─────────────────────────────────────────────
    /**
     * Method 1: Takes raw value and two units — converts and prints result.
     */
    public static void demonstrateLengthConversion(double value, LengthUnit fromUnit, LengthUnit toUnit) {
        double result = QuantityLength.convert(value, fromUnit, toUnit);
        System.out.printf("Input: convert(%.4f, %s, %s) → Output: %.4f%n", value, fromUnit, toUnit, result);
    }

    /**
     * Method 2: Takes an existing QuantityLength instance and a target unit.
     * Demonstrates method overloading — same name, different parameter list.
     */
    public static void demonstrateLengthConversion(QuantityLength length, LengthUnit toUnit) {
        QuantityLength converted = length.convertTo(toUnit);
        System.out.printf("Input: %s.convertTo(%s) → Output: %s%n", length, toUnit, converted);
    }

    // ─────────────────────────────────────────────
    // Addition API (UC6 & UC7)
    // ─────────────────────────────────────────────
    /**
     * Demonstrates addition of two lengths using the default target (first operand's unit).
     */
    public static void demonstrateAddition(double val1, LengthUnit unit1, double val2, LengthUnit unit2) {
        QuantityLength q1 = new QuantityLength(val1, unit1);
        QuantityLength q2 = new QuantityLength(val2, unit2);
        QuantityLength sum = q1.add(q2);
        System.out.printf("Input: add(%s, %s)\nOutput: %s\n\n", q1, q2, sum);
    }

    /**
     * Demonstrates addition of two lengths with an explicit target unit.
     */
    public static void demonstrateAddition(double val1, LengthUnit unit1, double val2, LengthUnit unit2, LengthUnit targetUnit) {
        QuantityLength q1 = new QuantityLength(val1, unit1);
        QuantityLength q2 = new QuantityLength(val2, unit2);
        QuantityLength sum = QuantityLength.add(q1, q2, targetUnit);
        System.out.printf("Input: add(%s, %s, %s)\nOutput: %s\n\n", q1, q2, targetUnit, sum);
    }

    // ─────────────────────────────────────────────
    // Equality & comparison API
    // ─────────────────────────────────────────────
    /**
     * Checks and prints equality of two QuantityLength objects.
     */
    public static void demonstrateLengthEquality(QuantityLength a, QuantityLength b) {
        System.out.printf("Equality: %s == %s → %b%n", a, b, a.equals(b));
    }

    /**
     * Creates two QuantityLength instances from raw values and delegates to demonstrateLengthEquality.
     */
    public static void demonstrateLengthComparison(double val1, LengthUnit unit1,
                                                   double val2, LengthUnit unit2) {
        QuantityLength a = new QuantityLength(val1, unit1);
        QuantityLength b = new QuantityLength(val2, unit2);
        demonstrateLengthEquality(a, b);
    }
}
