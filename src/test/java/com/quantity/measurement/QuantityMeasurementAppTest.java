package com.quantity.measurement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    // ─────────────────────────────────────────────
    // UC3: Feet & Inches (backward compatibility)
    // ─────────────────────────────────────────────

    @Test
    void testEquality_FeetToFeet_SameValue() {
        QuantityLength feet1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength feet2 = new QuantityLength(1.0, LengthUnit.FEET);
        assertTrue(feet1.equals(feet2), "1.0 ft should be equal to 1.0 ft");
    }

    @Test
    void testEquality_InchToInch_SameValue() {
        QuantityLength inch1 = new QuantityLength(1.0, LengthUnit.INCH);
        QuantityLength inch2 = new QuantityLength(1.0, LengthUnit.INCH);
        assertTrue(inch1.equals(inch2), "1.0 inch should be equal to 1.0 inch");
    }

    @Test
    void testEquality_FeetToInch_EquivalentValue() {
        QuantityLength feet = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength inches = new QuantityLength(12.0, LengthUnit.INCH);
        assertTrue(feet.equals(inches), "1.0 ft should be equal to 12.0 inches");
    }

    @Test
    void testEquality_InchToFeet_EquivalentValue() {
        QuantityLength inches = new QuantityLength(12.0, LengthUnit.INCH);
        QuantityLength feet = new QuantityLength(1.0, LengthUnit.FEET);
        assertTrue(inches.equals(feet), "12.0 inches should be equal to 1.0 ft");
    }

    @Test
    void testEquality_FeetToFeet_DifferentValue() {
        QuantityLength feet1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength feet2 = new QuantityLength(2.0, LengthUnit.FEET);
        assertFalse(feet1.equals(feet2), "1.0 ft should not be equal to 2.0 ft");
    }

    @Test
    void testEquality_InchToInch_DifferentValue() {
        QuantityLength inch1 = new QuantityLength(1.0, LengthUnit.INCH);
        QuantityLength inch2 = new QuantityLength(2.0, LengthUnit.INCH);
        assertFalse(inch1.equals(inch2), "1.0 inch should not be equal to 2.0 inch");
    }

    @Test
    void testEquality_NullUnit() {
        assertThrows(IllegalArgumentException.class, () -> {
            new QuantityLength(1.0, null);
        }, "Should throw exception when unit is null");
    }

    @Test
    void testEquality_SameReference() {
        QuantityLength feet = new QuantityLength(1.0, LengthUnit.FEET);
        assertTrue(feet.equals(feet), "Value should be equal to itself (reflexive)");
    }

    @Test
    void testEquality_NullComparison() {
        QuantityLength feet = new QuantityLength(1.0, LengthUnit.FEET);
        assertFalse(feet.equals(null), "Value should not be equal to null");
    }

    @Test
    void testEquality_NonNumericInput() {
        QuantityLength feet = new QuantityLength(1.0, LengthUnit.FEET);
        String nonNumericInput = "1.0 ft";
        assertFalse(feet.equals(nonNumericInput), "Value should not be equal to an object of different type");
    }

    // ─────────────────────────────────────────────
    // UC4: Yard tests
    // ─────────────────────────────────────────────

    @Test
    void testEquality_YardToYard_SameValue() {
        QuantityLength yard1 = new QuantityLength(1.0, LengthUnit.YARD);
        QuantityLength yard2 = new QuantityLength(1.0, LengthUnit.YARD);
        assertTrue(yard1.equals(yard2), "1.0 yard should be equal to 1.0 yard");
    }

    @Test
    void testEquality_YardToYard_DifferentValue() {
        QuantityLength yard1 = new QuantityLength(1.0, LengthUnit.YARD);
        QuantityLength yard2 = new QuantityLength(2.0, LengthUnit.YARD);
        assertFalse(yard1.equals(yard2), "1.0 yard should not be equal to 2.0 yards");
    }

    @Test
    void testEquality_YardToFeet_EquivalentValue() {
        QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARD);
        QuantityLength feet = new QuantityLength(3.0, LengthUnit.FEET);
        assertTrue(yard.equals(feet), "1.0 yard should be equal to 3.0 feet");
    }

    @Test
    void testEquality_FeetToYard_EquivalentValue() {
        QuantityLength feet = new QuantityLength(3.0, LengthUnit.FEET);
        QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARD);
        assertTrue(feet.equals(yard), "3.0 feet should be equal to 1.0 yard (symmetric)");
    }

    @Test
    void testEquality_YardToInches_EquivalentValue() {
        QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARD);
        QuantityLength inches = new QuantityLength(36.0, LengthUnit.INCH);
        assertTrue(yard.equals(inches), "1.0 yard should be equal to 36.0 inches");
    }

    @Test
    void testEquality_InchesToYard_EquivalentValue() {
        QuantityLength inches = new QuantityLength(36.0, LengthUnit.INCH);
        QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARD);
        assertTrue(inches.equals(yard), "36.0 inches should be equal to 1.0 yard (symmetric)");
    }

    @Test
    void testEquality_YardToFeet_NonEquivalentValue() {
        QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARD);
        QuantityLength feet = new QuantityLength(2.0, LengthUnit.FEET);
        assertFalse(yard.equals(feet), "1.0 yard should not be equal to 2.0 feet");
    }

    @Test
    void testEquality_YardSameReference() {
        QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARD);
        assertTrue(yard.equals(yard), "Yard object should be equal to itself (reflexive)");
    }

    @Test
    void testEquality_YardNullComparison() {
        QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARD);
        assertFalse(yard.equals(null), "Yard object should not be equal to null");
    }

    @Test
    void testEquality_YardWithNullUnit() {
        assertThrows(IllegalArgumentException.class, () -> {
            new QuantityLength(1.0, null);
        }, "Should throw exception when unit is null for yard");
    }

    // ─────────────────────────────────────────────
    // UC4: Centimeter tests
    // ─────────────────────────────────────────────

    @Test
    void testEquality_CentimeterToCentimeter_SameValue() {
        QuantityLength cm1 = new QuantityLength(2.0, LengthUnit.CENTIMETER);
        QuantityLength cm2 = new QuantityLength(2.0, LengthUnit.CENTIMETER);
        assertTrue(cm1.equals(cm2), "2.0 cm should be equal to 2.0 cm");
    }

    @Test
    void testEquality_CentimeterToCentimeter_DifferentValue() {
        QuantityLength cm1 = new QuantityLength(1.0, LengthUnit.CENTIMETER);
        QuantityLength cm2 = new QuantityLength(2.0, LengthUnit.CENTIMETER);
        assertFalse(cm1.equals(cm2), "1.0 cm should not be equal to 2.0 cm");
    }

    @Test
    void testEquality_CentimetersToInches_EquivalentValue() {
        QuantityLength cm = new QuantityLength(1.0, LengthUnit.CENTIMETER);
        QuantityLength inches = new QuantityLength(0.393701, LengthUnit.INCH);
        assertTrue(cm.equals(inches), "1.0 cm should be equal to 0.393701 inches");
    }

    @Test
    void testEquality_InchesToCentimeters_EquivalentValue() {
        QuantityLength inches = new QuantityLength(0.393701, LengthUnit.INCH);
        QuantityLength cm = new QuantityLength(1.0, LengthUnit.CENTIMETER);
        assertTrue(inches.equals(cm), "0.393701 inches should be equal to 1.0 cm (symmetric)");
    }

    @Test
    void testEquality_CentimetersToFeet_NonEquivalentValue() {
        QuantityLength cm = new QuantityLength(1.0, LengthUnit.CENTIMETER);
        QuantityLength feet = new QuantityLength(1.0, LengthUnit.FEET);
        assertFalse(cm.equals(feet), "1.0 cm should not be equal to 1.0 ft");
    }

    @Test
    void testEquality_CentimetersSameReference() {
        QuantityLength cm = new QuantityLength(1.0, LengthUnit.CENTIMETER);
        assertTrue(cm.equals(cm), "Centimeter object should be equal to itself (reflexive)");
    }

    @Test
    void testEquality_CentimetersNullComparison() {
        QuantityLength cm = new QuantityLength(1.0, LengthUnit.CENTIMETER);
        assertFalse(cm.equals(null), "Centimeter object should not be equal to null");
    }

    @Test
    void testEquality_CentimetersWithNullUnit() {
        assertThrows(IllegalArgumentException.class, () -> {
            new QuantityLength(1.0, null);
        }, "Should throw exception when unit is null for centimeter");
    }

    // ─────────────────────────────────────────────
    // UC4: Multi-unit transitive & complex scenarios
    // ─────────────────────────────────────────────

    @Test
    void testEquality_MultiUnit_TransitiveProperty() {
        QuantityLength yard  = new QuantityLength(1.0, LengthUnit.YARD);
        QuantityLength feet  = new QuantityLength(3.0, LengthUnit.FEET);
        QuantityLength inches = new QuantityLength(36.0, LengthUnit.INCH);
        // A == B
        assertTrue(yard.equals(feet), "1.0 yard == 3.0 feet");
        // B == C
        assertTrue(feet.equals(inches), "3.0 feet == 36.0 inches");
        // Transitive: A == C
        assertTrue(yard.equals(inches), "1.0 yard == 36.0 inches (transitive)");
    }

    @Test
    void testEquality_AllUnits_ComplexScenario() {
        QuantityLength yard2   = new QuantityLength(2.0, LengthUnit.YARD);
        QuantityLength feet6   = new QuantityLength(6.0, LengthUnit.FEET);
        QuantityLength inches72 = new QuantityLength(72.0, LengthUnit.INCH);
        assertTrue(yard2.equals(feet6),   "2.0 yards == 6.0 feet");
        assertTrue(feet6.equals(inches72),"6.0 feet == 72.0 inches");
        assertTrue(yard2.equals(inches72),"2.0 yards == 72.0 inches");
    }
}
