package com.quantity.measurement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QuantityMeasurementApp {

    public static void main(String[] args) {
        SpringApplication.run(QuantityMeasurementApp.class, args);

        // UC5: Unit-to-Unit Conversion demonstration
        System.out.println("=== UC5: Unit Conversion Demo ===");
        demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCH);
        demonstrateLengthConversion(3.0, LengthUnit.YARD, LengthUnit.FEET);
        demonstrateLengthConversion(36.0, LengthUnit.INCH, LengthUnit.YARD);
        demonstrateLengthConversion(1.0, LengthUnit.CENTIMETER, LengthUnit.INCH);
        demonstrateLengthConversion(0.0, LengthUnit.FEET, LengthUnit.INCH);

        // Overloaded form: pass a QuantityLength instance directly
        System.out.println("\n=== Overloaded demonstrateLengthConversion ===");
        QuantityLength lengthInYards = new QuantityLength(1.0, LengthUnit.YARD);
        demonstrateLengthConversion(lengthInYards, LengthUnit.INCH);

        // Equality demonstration
        System.out.println("\n=== Equality Demo ===");
        demonstrateLengthComparison(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCH);
        demonstrateLengthComparison(1.0, LengthUnit.YARD, 3.0, LengthUnit.FEET);
    }

    // ─────────────────────────────────────────────
    // Overloaded demonstrateLengthConversion
    // ─────────────────────────────────────────────

    /**
     * Method 1: Takes raw value and two units — converts and prints result.
     */
    public static void demonstrateLengthConversion(double value, LengthUnit fromUnit, LengthUnit toUnit) {
        double result = QuantityLength.convert(value, fromUnit, toUnit);
        System.out.printf("Input: convert(%.4f, %s, %s) → Output: %.4f%n",
                value, fromUnit, toUnit, result);
    }

    /**
     * Method 2: Takes an existing QuantityLength instance and a target unit.
     * Demonstrates method overloading — same name, different parameter list.
     */
    public static void demonstrateLengthConversion(QuantityLength length, LengthUnit toUnit) {
        QuantityLength converted = length.convertTo(toUnit);
        System.out.printf("Input: %s.convertTo(%s) → Output: %s%n",
                length, toUnit, converted);
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
