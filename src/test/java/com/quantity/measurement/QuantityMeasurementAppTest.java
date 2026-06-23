package com.quantity.measurement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

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
}
