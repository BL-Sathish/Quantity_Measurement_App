package com.quantity.measurement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    // --- Feet Tests ---

    @Test
    void testEquality_SameValue() {
        QuantityMeasurementApp.Feet feet1 = new QuantityMeasurementApp.Feet(1.0);
        QuantityMeasurementApp.Feet feet2 = new QuantityMeasurementApp.Feet(1.0);
        assertTrue(feet1.equals(feet2), "1.0 ft should be equal to 1.0 ft");
    }

    @Test
    void testEquality_DifferentValue() {
        QuantityMeasurementApp.Feet feet1 = new QuantityMeasurementApp.Feet(1.0);
        QuantityMeasurementApp.Feet feet2 = new QuantityMeasurementApp.Feet(2.0);
        assertFalse(feet1.equals(feet2), "1.0 ft should not be equal to 2.0 ft");
    }

    @Test
    void testEquality_NullComparison() {
        QuantityMeasurementApp.Feet feet1 = new QuantityMeasurementApp.Feet(1.0);
        assertFalse(feet1.equals(null), "Value should not be equal to null");
    }

    @Test
    void testEquality_NonNumericInput() {
        QuantityMeasurementApp.Feet feet1 = new QuantityMeasurementApp.Feet(1.0);
        String nonNumericInput = "1.0 ft";
        assertFalse(feet1.equals(nonNumericInput), "Value should not be equal to an object of different type");
    }

    @Test
    void testEquality_SameReference() {
        QuantityMeasurementApp.Feet feet1 = new QuantityMeasurementApp.Feet(1.0);
        assertTrue(feet1.equals(feet1), "Value should be equal to itself (reflexive)");
    }

    // --- Inches Tests ---

    @Test
    void testInchesEquality_SameValue() {
        QuantityMeasurementApp.Inches inches1 = new QuantityMeasurementApp.Inches(1.0);
        QuantityMeasurementApp.Inches inches2 = new QuantityMeasurementApp.Inches(1.0);
        assertTrue(inches1.equals(inches2), "1.0 inch should be equal to 1.0 inch");
    }

    @Test
    void testInchesEquality_DifferentValue() {
        QuantityMeasurementApp.Inches inches1 = new QuantityMeasurementApp.Inches(1.0);
        QuantityMeasurementApp.Inches inches2 = new QuantityMeasurementApp.Inches(2.0);
        assertFalse(inches1.equals(inches2), "1.0 inch should not be equal to 2.0 inch");
    }

    @Test
    void testInchesEquality_NullComparison() {
        QuantityMeasurementApp.Inches inches1 = new QuantityMeasurementApp.Inches(1.0);
        assertFalse(inches1.equals(null), "Value should not be equal to null");
    }

    @Test
    void testInchesEquality_NonNumericInput() {
        QuantityMeasurementApp.Inches inches1 = new QuantityMeasurementApp.Inches(1.0);
        String nonNumericInput = "1.0 inch";
        assertFalse(inches1.equals(nonNumericInput), "Value should not be equal to an object of different type");
    }

    @Test
    void testInchesEquality_SameReference() {
        QuantityMeasurementApp.Inches inches1 = new QuantityMeasurementApp.Inches(1.0);
        assertTrue(inches1.equals(inches1), "Value should be equal to itself (reflexive)");
    }
}
