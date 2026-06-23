package com.quantity.measurement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite covering UC3 (backward compat), UC4 (yards/cm), and UC5 (unit conversion).
 */
class QuantityMeasurementAppTest {

    private static final double EPSILON = 1e-6;

    // ─────────────────────────────────────────────
    // UC3 backward compatibility: Feet & Inches equality
    // ─────────────────────────────────────────────

    @Test
    void testEquality_FeetToFeet_SameValue() {
        assertTrue(new QuantityLength(1.0, LengthUnit.FEET).equals(new QuantityLength(1.0, LengthUnit.FEET)));
    }

    @Test
    void testEquality_InchToInch_SameValue() {
        assertTrue(new QuantityLength(1.0, LengthUnit.INCH).equals(new QuantityLength(1.0, LengthUnit.INCH)));
    }

    @Test
    void testEquality_FeetToInch_EquivalentValue() {
        assertTrue(new QuantityLength(1.0, LengthUnit.FEET).equals(new QuantityLength(12.0, LengthUnit.INCH)));
    }

    @Test
    void testEquality_InchToFeet_EquivalentValue() {
        assertTrue(new QuantityLength(12.0, LengthUnit.INCH).equals(new QuantityLength(1.0, LengthUnit.FEET)));
    }

    @Test
    void testEquality_FeetToFeet_DifferentValue() {
        assertFalse(new QuantityLength(1.0, LengthUnit.FEET).equals(new QuantityLength(2.0, LengthUnit.FEET)));
    }

    @Test
    void testEquality_InchToInch_DifferentValue() {
        assertFalse(new QuantityLength(1.0, LengthUnit.INCH).equals(new QuantityLength(2.0, LengthUnit.INCH)));
    }

    @Test
    void testEquality_SameReference() {
        QuantityLength feet = new QuantityLength(1.0, LengthUnit.FEET);
        assertTrue(feet.equals(feet));
    }

    @Test
    void testEquality_NullComparison() {
        assertFalse(new QuantityLength(1.0, LengthUnit.FEET).equals(null));
    }

    @Test
    void testEquality_NonNumericInput() {
        assertFalse(new QuantityLength(1.0, LengthUnit.FEET).equals("1.0 ft"));
    }

    // ─────────────────────────────────────────────
    // UC4 backward compatibility: Yards & Centimeters
    // ─────────────────────────────────────────────

    @Test
    void testEquality_YardToYard_SameValue() {
        assertTrue(new QuantityLength(1.0, LengthUnit.YARD).equals(new QuantityLength(1.0, LengthUnit.YARD)));
    }

    @Test
    void testEquality_YardToFeet_EquivalentValue() {
        assertTrue(new QuantityLength(1.0, LengthUnit.YARD).equals(new QuantityLength(3.0, LengthUnit.FEET)));
    }

    @Test
    void testEquality_YardToInches_EquivalentValue() {
        assertTrue(new QuantityLength(1.0, LengthUnit.YARD).equals(new QuantityLength(36.0, LengthUnit.INCH)));
    }

    @Test
    void testEquality_MultiUnit_TransitiveProperty() {
        QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARD);
        QuantityLength feet = new QuantityLength(3.0, LengthUnit.FEET);
        QuantityLength inches = new QuantityLength(36.0, LengthUnit.INCH);
        assertTrue(yard.equals(feet));
        assertTrue(feet.equals(inches));
        assertTrue(yard.equals(inches));
    }

    @Test
    void testEquality_AllUnits_ComplexScenario() {
        assertTrue(new QuantityLength(2.0, LengthUnit.YARD).equals(new QuantityLength(6.0, LengthUnit.FEET)));
        assertTrue(new QuantityLength(6.0, LengthUnit.FEET).equals(new QuantityLength(72.0, LengthUnit.INCH)));
        assertTrue(new QuantityLength(2.0, LengthUnit.YARD).equals(new QuantityLength(72.0, LengthUnit.INCH)));
    }

    @Test
    void testEquality_CentimeterToCentimeter_SameValue() {
        assertTrue(new QuantityLength(2.0, LengthUnit.CENTIMETER).equals(new QuantityLength(2.0, LengthUnit.CENTIMETER)));
    }

    @Test
    void testEquality_CentimetersToInches_EquivalentValue() {
        assertTrue(new QuantityLength(1.0, LengthUnit.CENTIMETER).equals(new QuantityLength(0.393701, LengthUnit.INCH)));
    }

    // ─────────────────────────────────────────────
    // UC5: Constructor input validation
    // ─────────────────────────────────────────────

    @Test
    void testConstructor_NullUnit_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new QuantityLength(1.0, null));
    }

    @Test
    void testConstructor_NaNValue_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new QuantityLength(Double.NaN, LengthUnit.FEET));
    }

    @Test
    void testConstructor_PositiveInfinity_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new QuantityLength(Double.POSITIVE_INFINITY, LengthUnit.FEET));
    }

    @Test
    void testConstructor_NegativeInfinity_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new QuantityLength(Double.NEGATIVE_INFINITY, LengthUnit.FEET));
    }

    // ─────────────────────────────────────────────
    // UC5: Static convert() — basic conversions
    // ─────────────────────────────────────────────

    @Test
    void testConversion_FeetToInches() {
        assertEquals(12.0, QuantityLength.convert(1.0, LengthUnit.FEET, LengthUnit.INCH), EPSILON);
    }

    @Test
    void testConversion_InchesToFeet() {
        assertEquals(2.0, QuantityLength.convert(24.0, LengthUnit.INCH, LengthUnit.FEET), EPSILON);
    }

    @Test
    void testConversion_YardsToInches() {
        assertEquals(36.0, QuantityLength.convert(1.0, LengthUnit.YARD, LengthUnit.INCH), EPSILON);
    }

    @Test
    void testConversion_InchesToYards() {
        assertEquals(2.0, QuantityLength.convert(72.0, LengthUnit.INCH, LengthUnit.YARD), EPSILON);
    }

    @Test
    void testConversion_FeetToYards() {
        assertEquals(2.0, QuantityLength.convert(6.0, LengthUnit.FEET, LengthUnit.YARD), EPSILON);
    }

    @Test
    void testConversion_YardsToFeet() {
        assertEquals(9.0, QuantityLength.convert(3.0, LengthUnit.YARD, LengthUnit.FEET), EPSILON);
    }

    @Test
    void testConversion_CentimetersToInches() {
        assertEquals(1.0, QuantityLength.convert(2.54, LengthUnit.CENTIMETER, LengthUnit.INCH), 1e-4);
    }

    @Test
    void testConversion_InchesToCentimeters() {
        assertEquals(2.54, QuantityLength.convert(1.0, LengthUnit.INCH, LengthUnit.CENTIMETER), 1e-3);
    }

    // ─────────────────────────────────────────────
    // UC5: Static convert() — edge cases
    // ─────────────────────────────────────────────

    @Test
    void testConversion_ZeroValue() {
        assertEquals(0.0, QuantityLength.convert(0.0, LengthUnit.FEET, LengthUnit.INCH), EPSILON);
    }

    @Test
    void testConversion_NegativeValue() {
        assertEquals(-12.0, QuantityLength.convert(-1.0, LengthUnit.FEET, LengthUnit.INCH), EPSILON);
    }

    @Test
    void testConversion_SameUnit() {
        assertEquals(5.0, QuantityLength.convert(5.0, LengthUnit.FEET, LengthUnit.FEET), EPSILON);
    }

    @Test
    void testConversion_LargeValue() {
        assertEquals(36000.0, QuantityLength.convert(1000.0, LengthUnit.YARD, LengthUnit.INCH), EPSILON);
    }

    @Test
    void testConversion_SmallValue() {
        assertEquals(0.00001, QuantityLength.convert(0.00001, LengthUnit.FEET, LengthUnit.FEET), EPSILON);
    }

    // ─────────────────────────────────────────────
    // UC5: Static convert() — invalid inputs
    // ─────────────────────────────────────────────

    @Test
    void testConversion_NullSourceUnit_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> QuantityLength.convert(1.0, null, LengthUnit.INCH));
    }

    @Test
    void testConversion_NullTargetUnit_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> QuantityLength.convert(1.0, LengthUnit.FEET, null));
    }

    @Test
    void testConversion_NaNValue_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> QuantityLength.convert(Double.NaN, LengthUnit.FEET, LengthUnit.INCH));
    }

    @Test
    void testConversion_InfiniteValue_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> QuantityLength.convert(Double.POSITIVE_INFINITY, LengthUnit.FEET, LengthUnit.INCH));
    }

    // ─────────────────────────────────────────────
    // UC5: Instance convertTo() method
    // ─────────────────────────────────────────────

    @Test
    void testConvertTo_FeetToInches_ReturnsNewInstance() {
        QuantityLength feet = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength result = feet.convertTo(LengthUnit.INCH);
        assertNotSame(feet, result, "convertTo should return a new instance (immutability)");
        assertEquals(12.0, QuantityLength.convert(1.0, LengthUnit.FEET, LengthUnit.INCH), EPSILON);
    }

    @Test
    void testConvertTo_NullTarget_ThrowsException() {
        QuantityLength feet = new QuantityLength(1.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> feet.convertTo(null));
    }

    // ─────────────────────────────────────────────
    // UC5: Round-trip conversion
    // ─────────────────────────────────────────────

    @Test
    void testConversion_RoundTrip_FeetToInchesAndBack() {
        double original = 5.0;
        double toInches = QuantityLength.convert(original, LengthUnit.FEET, LengthUnit.INCH);
        double backToFeet = QuantityLength.convert(toInches, LengthUnit.INCH, LengthUnit.FEET);
        assertEquals(original, backToFeet, EPSILON);
    }

    @Test
    void testConversion_RoundTrip_YardsToFeetToInchesAndBack() {
        double original = 2.0;
        double toFeet   = QuantityLength.convert(original, LengthUnit.YARD, LengthUnit.FEET);
        double toInches = QuantityLength.convert(toFeet,   LengthUnit.FEET, LengthUnit.INCH);
        double toYards  = QuantityLength.convert(toInches, LengthUnit.INCH, LengthUnit.YARD);
        assertEquals(original, toYards, EPSILON);
    }

    // ─────────────────────────────────────────────
    // UC5: toString override
    // ─────────────────────────────────────────────

    @Test
    void testToString_ContainsValueAndUnit() {
        QuantityLength q = new QuantityLength(1.0, LengthUnit.FEET);
        String str = q.toString();
        assertTrue(str.contains("1.0"), "toString should contain the value");
        assertTrue(str.contains("FEET"), "toString should contain the unit");
    }

    // ─────────────────────────────────────────────
    // UC6: Addition of Two Length Units
    // ─────────────────────────────────────────────

    @Test
    void testAddition_SameUnit_FeetPlusFeet() {
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(2.0, LengthUnit.FEET);
        QuantityLength result = q1.add(q2);
        assertTrue(result.equals(new QuantityLength(3.0, LengthUnit.FEET)));
    }

    @Test
    void testAddition_SameUnit_InchPlusInch() {
        QuantityLength q1 = new QuantityLength(6.0, LengthUnit.INCH);
        QuantityLength q2 = new QuantityLength(6.0, LengthUnit.INCH);
        QuantityLength result = q1.add(q2);
        assertTrue(result.equals(new QuantityLength(12.0, LengthUnit.INCH)));
    }

    @Test
    void testAddition_CrossUnit_FeetPlusInches() {
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCH);
        QuantityLength result = q1.add(q2);
        assertTrue(result.equals(new QuantityLength(2.0, LengthUnit.FEET)));
    }

    @Test
    void testAddition_CrossUnit_InchPlusFeet() {
        QuantityLength q1 = new QuantityLength(12.0, LengthUnit.INCH);
        QuantityLength q2 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength result = q1.add(q2);
        assertTrue(result.equals(new QuantityLength(24.0, LengthUnit.INCH)));
    }

    @Test
    void testAddition_CrossUnit_YardPlusFeet() {
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.YARD);
        QuantityLength q2 = new QuantityLength(3.0, LengthUnit.FEET);
        QuantityLength result = q1.add(q2);
        assertTrue(result.equals(new QuantityLength(2.0, LengthUnit.YARD)));
    }

    @Test
    void testAddition_CrossUnit_CentimeterPlusInch() {
        QuantityLength q1 = new QuantityLength(2.54, LengthUnit.CENTIMETER);
        QuantityLength q2 = new QuantityLength(1.0, LengthUnit.INCH);
        QuantityLength result = q1.add(q2);
        // 1 inch = 2.54 cm -> 2.54 cm + 2.54 cm = 5.08 cm
        assertTrue(result.equals(new QuantityLength(5.08, LengthUnit.CENTIMETER)));
    }

    @Test
    void testAddition_Commutativity() {
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCH);
        QuantityLength result1 = QuantityLength.add(q1, q2, LengthUnit.INCH);
        QuantityLength result2 = QuantityLength.add(q2, q1, LengthUnit.INCH);
        assertTrue(result1.equals(result2));
    }

    @Test
    void testAddition_WithZero() {
        QuantityLength q1 = new QuantityLength(5.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(0.0, LengthUnit.INCH);
        QuantityLength result = q1.add(q2);
        assertTrue(result.equals(new QuantityLength(5.0, LengthUnit.FEET)));
    }

    @Test
    void testAddition_NegativeValues() {
        QuantityLength q1 = new QuantityLength(5.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(-2.0, LengthUnit.FEET);
        QuantityLength result = q1.add(q2);
        assertTrue(result.equals(new QuantityLength(3.0, LengthUnit.FEET)));
    }

    @Test
    void testAddition_NullSecondOperand() {
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> q1.add(null));
    }

    @Test
    void testAddition_LargeValues() {
        QuantityLength q1 = new QuantityLength(1e6, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(1e6, LengthUnit.FEET);
        QuantityLength result = q1.add(q2);
        assertTrue(result.equals(new QuantityLength(2e6, LengthUnit.FEET)));
    }

    @Test
    void testAddition_SmallValues() {
        QuantityLength q1 = new QuantityLength(0.001, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(0.002, LengthUnit.FEET);
        QuantityLength result = q1.add(q2);
        assertTrue(result.equals(new QuantityLength(0.003, LengthUnit.FEET)));
    }

}
