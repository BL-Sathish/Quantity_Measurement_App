package com.quantity.measurement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite covering UC3–UC8.
 */
class QuantityMeasurementAppTest {

    private static final double EPSILON = 1e-6;

    // ─────────────────────────────────────────────
    // UC3 backward compatibility: Feet & Inches equality
    // ─────────────────────────────────────────────

    @Test
    void testEquality_FeetToFeet_SameValue() {
        assertTrue(new Quantity<>(1.0, LengthUnit.FEET).equals(new Quantity<>(1.0, LengthUnit.FEET)));
    }

    @Test
    void testEquality_InchToInch_SameValue() {
        assertTrue(new Quantity<>(1.0, LengthUnit.INCH).equals(new Quantity<>(1.0, LengthUnit.INCH)));
    }

    @Test
    void testEquality_FeetToInch_EquivalentValue() {
        assertTrue(new Quantity<>(1.0, LengthUnit.FEET).equals(new Quantity<>(12.0, LengthUnit.INCH)));
    }

    @Test
    void testEquality_InchToFeet_EquivalentValue() {
        assertTrue(new Quantity<>(12.0, LengthUnit.INCH).equals(new Quantity<>(1.0, LengthUnit.FEET)));
    }

    @Test
    void testEquality_FeetToFeet_DifferentValue() {
        assertFalse(new Quantity<>(1.0, LengthUnit.FEET).equals(new Quantity<>(2.0, LengthUnit.FEET)));
    }

    @Test
    void testEquality_InchToInch_DifferentValue() {
        assertFalse(new Quantity<>(1.0, LengthUnit.INCH).equals(new Quantity<>(2.0, LengthUnit.INCH)));
    }

    @Test
    void testEquality_SameReference() {
        Quantity<LengthUnit> feet = new Quantity<>(1.0, LengthUnit.FEET);
        assertTrue(feet.equals(feet));
    }

    @Test
    void testEquality_NullComparison() {
        assertFalse(new Quantity<>(1.0, LengthUnit.FEET).equals(null));
    }

    @Test
    void testEquality_NonNumericInput() {
        assertFalse(new Quantity<>(1.0, LengthUnit.FEET).equals("1.0 ft"));
    }

    // ─────────────────────────────────────────────
    // UC4 backward compatibility: Yards & Centimeters
    // ─────────────────────────────────────────────

    @Test
    void testEquality_YardToYard_SameValue() {
        assertTrue(new Quantity<>(1.0, LengthUnit.YARD).equals(new Quantity<>(1.0, LengthUnit.YARD)));
    }

    @Test
    void testEquality_YardToFeet_EquivalentValue() {
        assertTrue(new Quantity<>(1.0, LengthUnit.YARD).equals(new Quantity<>(3.0, LengthUnit.FEET)));
    }

    @Test
    void testEquality_YardToInches_EquivalentValue() {
        assertTrue(new Quantity<>(1.0, LengthUnit.YARD).equals(new Quantity<>(36.0, LengthUnit.INCH)));
    }

    @Test
    void testEquality_MultiUnit_TransitiveProperty() {
        Quantity<LengthUnit> yard = new Quantity<>(1.0, LengthUnit.YARD);
        Quantity<LengthUnit> feet = new Quantity<>(3.0, LengthUnit.FEET);
        Quantity<LengthUnit> inches = new Quantity<>(36.0, LengthUnit.INCH);
        assertTrue(yard.equals(feet));
        assertTrue(feet.equals(inches));
        assertTrue(yard.equals(inches));
    }

    @Test
    void testEquality_AllUnits_ComplexScenario() {
        assertTrue(new Quantity<>(2.0, LengthUnit.YARD).equals(new Quantity<>(6.0, LengthUnit.FEET)));
        assertTrue(new Quantity<>(6.0, LengthUnit.FEET).equals(new Quantity<>(72.0, LengthUnit.INCH)));
        assertTrue(new Quantity<>(2.0, LengthUnit.YARD).equals(new Quantity<>(72.0, LengthUnit.INCH)));
    }

    @Test
    void testEquality_CentimeterToCentimeter_SameValue() {
        assertTrue(new Quantity<>(2.0, LengthUnit.CENTIMETER).equals(new Quantity<>(2.0, LengthUnit.CENTIMETER)));
    }

    @Test
    void testEquality_CentimetersToInches_EquivalentValue() {
        assertTrue(new Quantity<>(1.0, LengthUnit.CENTIMETER).equals(new Quantity<>(0.393701, LengthUnit.INCH)));
    }

    // ─────────────────────────────────────────────
    // UC5: Constructor input validation
    // ─────────────────────────────────────────────

    @Test
    void testConstructor_NullUnit_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(1.0, null));
    }

    @Test
    void testConstructor_NaNValue_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(Double.NaN, LengthUnit.FEET));
    }

    @Test
    void testConstructor_PositiveInfinity_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(Double.POSITIVE_INFINITY, LengthUnit.FEET));
    }

    @Test
    void testConstructor_NegativeInfinity_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(Double.NEGATIVE_INFINITY, LengthUnit.FEET));
    }

    // ─────────────────────────────────────────────
    // UC5: Static convert() — basic conversions
    // ─────────────────────────────────────────────

    @Test
    void testConversion_FeetToInches() {
        assertEquals(12.0, Quantity.convert(1.0, LengthUnit.FEET, LengthUnit.INCH), EPSILON);
    }

    @Test
    void testConversion_InchesToFeet() {
        assertEquals(2.0, Quantity.convert(24.0, LengthUnit.INCH, LengthUnit.FEET), EPSILON);
    }

    @Test
    void testConversion_YardsToInches() {
        assertEquals(36.0, Quantity.convert(1.0, LengthUnit.YARD, LengthUnit.INCH), EPSILON);
    }

    @Test
    void testConversion_InchesToYards() {
        assertEquals(2.0, Quantity.convert(72.0, LengthUnit.INCH, LengthUnit.YARD), EPSILON);
    }

    @Test
    void testConversion_FeetToYards() {
        assertEquals(2.0, Quantity.convert(6.0, LengthUnit.FEET, LengthUnit.YARD), EPSILON);
    }

    @Test
    void testConversion_YardsToFeet() {
        assertEquals(9.0, Quantity.convert(3.0, LengthUnit.YARD, LengthUnit.FEET), EPSILON);
    }

    @Test
    void testConversion_CentimetersToInches() {
        assertEquals(1.0, Quantity.convert(2.54, LengthUnit.CENTIMETER, LengthUnit.INCH), 1e-4);
    }

    @Test
    void testConversion_InchesToCentimeters() {
        assertEquals(2.54, Quantity.convert(1.0, LengthUnit.INCH, LengthUnit.CENTIMETER), 1e-3);
    }

    // ─────────────────────────────────────────────
    // UC5: Static convert() — edge cases
    // ─────────────────────────────────────────────

    @Test
    void testConversion_ZeroValue() {
        assertEquals(0.0, Quantity.convert(0.0, LengthUnit.FEET, LengthUnit.INCH), EPSILON);
    }

    @Test
    void testConversion_NegativeValue() {
        assertEquals(-12.0, Quantity.convert(-1.0, LengthUnit.FEET, LengthUnit.INCH), EPSILON);
    }

    @Test
    void testConversion_SameUnit() {
        assertEquals(5.0, Quantity.convert(5.0, LengthUnit.FEET, LengthUnit.FEET), EPSILON);
    }

    @Test
    void testConversion_LargeValue() {
        assertEquals(36000.0, Quantity.convert(1000.0, LengthUnit.YARD, LengthUnit.INCH), EPSILON);
    }

    @Test
    void testConversion_SmallValue() {
        assertEquals(0.00001, Quantity.convert(0.00001, LengthUnit.FEET, LengthUnit.FEET), EPSILON);
    }

    // ─────────────────────────────────────────────
    // UC5: Static convert() — invalid inputs
    // ─────────────────────────────────────────────

    @Test
    void testConversion_NullSourceUnit_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> Quantity.convert(1.0, null, LengthUnit.INCH));
    }

    @Test
    void testConversion_NullTargetUnit_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> Quantity.convert(1.0, LengthUnit.FEET, null));
    }

    @Test
    void testConversion_NaNValue_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> Quantity.convert(Double.NaN, LengthUnit.FEET, LengthUnit.INCH));
    }

    @Test
    void testConversion_InfiniteValue_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> Quantity.convert(Double.POSITIVE_INFINITY, LengthUnit.FEET, LengthUnit.INCH));
    }

    // ─────────────────────────────────────────────
    // UC5: Instance convertTo() method
    // ─────────────────────────────────────────────

    @Test
    void testConvertTo_FeetToInches_ReturnsNewInstance() {
        Quantity<LengthUnit> feet = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> result = feet.convertTo(LengthUnit.INCH);
        assertNotSame(feet, result, "convertTo should return a new instance (immutability)");
        assertEquals(12.0, Quantity.convert(1.0, LengthUnit.FEET, LengthUnit.INCH), EPSILON);
    }

    @Test
    void testConvertTo_NullTarget_ThrowsException() {
        Quantity<LengthUnit> feet = new Quantity<>(1.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> feet.convertTo(null));
    }

    // ─────────────────────────────────────────────
    // UC5: Round-trip conversion
    // ─────────────────────────────────────────────

    @Test
    void testConversion_RoundTrip_FeetToInchesAndBack() {
        double original = 5.0;
        double toInches = Quantity.convert(original, LengthUnit.FEET, LengthUnit.INCH);
        double backToFeet = Quantity.convert(toInches, LengthUnit.INCH, LengthUnit.FEET);
        assertEquals(original, backToFeet, EPSILON);
    }

    @Test
    void testConversion_RoundTrip_YardsToFeetToInchesAndBack() {
        double original = 2.0;
        double toFeet   = Quantity.convert(original, LengthUnit.YARD, LengthUnit.FEET);
        double toInches = Quantity.convert(toFeet,   LengthUnit.FEET, LengthUnit.INCH);
        double toYards  = Quantity.convert(toInches, LengthUnit.INCH, LengthUnit.YARD);
        assertEquals(original, toYards, EPSILON);
    }

    // ─────────────────────────────────────────────
    // UC5: toString override
    // ─────────────────────────────────────────────

    @Test
    void testToString_ContainsValueAndUnit() {
        Quantity<LengthUnit> q = new Quantity<>(1.0, LengthUnit.FEET);
        String str = q.toString();
        assertTrue(str.contains("1.0"), "toString should contain the value");
        assertTrue(str.contains("FEET"), "toString should contain the unit");
    }

    // ─────────────────────────────────────────────
    // UC6: Addition of Two Length Units
    // ─────────────────────────────────────────────

    @Test
    void testAddition_SameUnit_FeetPlusFeet() {
        Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(2.0, LengthUnit.FEET);
        Quantity<LengthUnit> result = q1.add(q2);
        assertTrue(result.equals(new Quantity<>(3.0, LengthUnit.FEET)));
    }

    @Test
    void testAddition_SameUnit_InchPlusInch() {
        Quantity<LengthUnit> q1 = new Quantity<>(6.0, LengthUnit.INCH);
        Quantity<LengthUnit> q2 = new Quantity<>(6.0, LengthUnit.INCH);
        Quantity<LengthUnit> result = q1.add(q2);
        assertTrue(result.equals(new Quantity<>(12.0, LengthUnit.INCH)));
    }

    @Test
    void testAddition_CrossUnit_FeetPlusInches() {
        Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(12.0, LengthUnit.INCH);
        Quantity<LengthUnit> result = q1.add(q2);
        assertTrue(result.equals(new Quantity<>(2.0, LengthUnit.FEET)));
    }

    @Test
    void testAddition_CrossUnit_InchPlusFeet() {
        Quantity<LengthUnit> q1 = new Quantity<>(12.0, LengthUnit.INCH);
        Quantity<LengthUnit> q2 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> result = q1.add(q2);
        assertTrue(result.equals(new Quantity<>(24.0, LengthUnit.INCH)));
    }

    @Test
    void testAddition_CrossUnit_YardPlusFeet() {
        Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.YARD);
        Quantity<LengthUnit> q2 = new Quantity<>(3.0, LengthUnit.FEET);
        Quantity<LengthUnit> result = q1.add(q2);
        assertTrue(result.equals(new Quantity<>(2.0, LengthUnit.YARD)));
    }

    @Test
    void testAddition_CrossUnit_CentimeterPlusInch() {
        Quantity<LengthUnit> q1 = new Quantity<>(2.54, LengthUnit.CENTIMETER);
        Quantity<LengthUnit> q2 = new Quantity<>(1.0, LengthUnit.INCH);
        Quantity<LengthUnit> result = q1.add(q2);
        // 1 inch = 2.54 cm -> 2.54 cm + 2.54 cm = 5.08 cm
        assertTrue(result.equals(new Quantity<>(5.08, LengthUnit.CENTIMETER)));
    }

    @Test
    void testAddition_Commutativity() {
        Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(12.0, LengthUnit.INCH);
        Quantity<LengthUnit> result1 = Quantity.add(q1, q2, LengthUnit.INCH);
        Quantity<LengthUnit> result2 = Quantity.add(q2, q1, LengthUnit.INCH);
        assertTrue(result1.equals(result2));
    }

    @Test
    void testAddition_WithZero() {
        Quantity<LengthUnit> q1 = new Quantity<>(5.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(0.0, LengthUnit.INCH);
        Quantity<LengthUnit> result = q1.add(q2);
        assertTrue(result.equals(new Quantity<>(5.0, LengthUnit.FEET)));
    }

    @Test
    void testAddition_NegativeValues() {
        Quantity<LengthUnit> q1 = new Quantity<>(5.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(-2.0, LengthUnit.FEET);
        Quantity<LengthUnit> result = q1.add(q2);
        assertTrue(result.equals(new Quantity<>(3.0, LengthUnit.FEET)));
    }

    @Test
    void testAddition_NullSecondOperand() {
        Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> q1.add(null));
    }

    @Test
    void testAddition_LargeValues() {
        Quantity<LengthUnit> q1 = new Quantity<>(1e6, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(1e6, LengthUnit.FEET);
        Quantity<LengthUnit> result = q1.add(q2);
        assertTrue(result.equals(new Quantity<>(2e6, LengthUnit.FEET)));
    }

    @Test
    void testAddition_SmallValues() {
        Quantity<LengthUnit> q1 = new Quantity<>(0.001, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(0.002, LengthUnit.FEET);
        Quantity<LengthUnit> result = q1.add(q2);
        assertTrue(result.equals(new Quantity<>(0.003, LengthUnit.FEET)));
    }

    // ─────────────────────────────────────────────
    // UC7: Addition with Target Unit Specification
    // ─────────────────────────────────────────────

    @Test
    void testAddition_WithTargetUnit_FeetAndInches_ToYards() {
        Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(12.0, LengthUnit.INCH);
        Quantity<LengthUnit> result = Quantity.add(q1, q2, LengthUnit.YARD);
        // 1 foot + 12 inches = 2 feet = 0.666666... yards
        assertTrue(result.equals(new Quantity<>(2.0 / 3.0, LengthUnit.YARD)));
    }

    @Test
    void testAddition_StaticRaw_WithTargetUnit_ToYards() {
        Quantity<LengthUnit> result = Quantity.add(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCH, LengthUnit.YARD);
        assertTrue(result.equals(new Quantity<>(2.0 / 3.0, LengthUnit.YARD)));
    }

    @Test
    void testAddition_WithTargetUnit_NullOperand() {
        Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> Quantity.add(q1, null, LengthUnit.FEET));
    }

    // ─────────────────────────────────────────────
    // UC8: Standalone LengthUnit Enum Tests
    // ─────────────────────────────────────────────

    @Test
    void testLengthUnitEnum_FeetConstant() {
        assertEquals(1.0, LengthUnit.FEET.getConversionFactor(), EPSILON);
    }

    @Test
    void testLengthUnitEnum_InchesConstant() {
        assertEquals(1.0 / 12.0, LengthUnit.INCH.getConversionFactor(), EPSILON);
    }

    @Test
    void testLengthUnitEnum_YardsConstant() {
        assertEquals(3.0, LengthUnit.YARD.getConversionFactor(), EPSILON);
    }

    @Test
    void testLengthUnitEnum_CentimetersConstant() {
        assertEquals(1.0 / 30.48, LengthUnit.CENTIMETER.getConversionFactor(), EPSILON);
    }

    // ─────────────────────────────────────────────
    // UC8: convertToBaseUnit tests
    // ─────────────────────────────────────────────

    @Test
    void testConvertToBaseUnit_FeetToFeet() {
        assertEquals(5.0, LengthUnit.FEET.convertToBaseUnit(5.0), EPSILON);
    }

    @Test
    void testConvertToBaseUnit_InchesToFeet() {
        assertEquals(1.0, LengthUnit.INCH.convertToBaseUnit(12.0), EPSILON);
    }

    @Test
    void testConvertToBaseUnit_YardsToFeet() {
        assertEquals(3.0, LengthUnit.YARD.convertToBaseUnit(1.0), EPSILON);
    }

    @Test
    void testConvertToBaseUnit_CentimetersToFeet() {
        assertEquals(1.0, LengthUnit.CENTIMETER.convertToBaseUnit(30.48), 1e-4);
    }

    // ─────────────────────────────────────────────
    // UC8: convertFromBaseUnit tests
    // ─────────────────────────────────────────────

    @Test
    void testConvertFromBaseUnit_FeetToFeet() {
        assertEquals(2.0, LengthUnit.FEET.convertFromBaseUnit(2.0), EPSILON);
    }

    @Test
    void testConvertFromBaseUnit_FeetToInches() {
        assertEquals(12.0, LengthUnit.INCH.convertFromBaseUnit(1.0), EPSILON);
    }

    @Test
    void testConvertFromBaseUnit_FeetToYards() {
        assertEquals(1.0, LengthUnit.YARD.convertFromBaseUnit(3.0), EPSILON);
    }

    @Test
    void testConvertFromBaseUnit_FeetToCentimeters() {
        assertEquals(30.48, LengthUnit.CENTIMETER.convertFromBaseUnit(1.0), 1e-4);
    }

    // ─────────────────────────────────────────────
    // UC8: Refactored Quantity<LengthUnit> delegation tests
    // ─────────────────────────────────────────────

    @Test
    void testQuantityLengthRefactored_Equality() {
        assertTrue(new Quantity<>(1.0, LengthUnit.FEET).equals(new Quantity<>(12.0, LengthUnit.INCH)));
    }

    @Test
    void testQuantityLengthRefactored_ConvertTo() {
        Quantity<LengthUnit> feet = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> inches = feet.convertTo(LengthUnit.INCH);
        assertTrue(inches.equals(new Quantity<>(12.0, LengthUnit.INCH)));
    }

    @Test
    void testQuantityLengthRefactored_Add() {
        Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(12.0, LengthUnit.INCH);
        Quantity<LengthUnit> result = q1.add(q2);
        assertTrue(result.equals(new Quantity<>(2.0, LengthUnit.FEET)));
    }

    @Test
    void testQuantityLengthRefactored_AddWithTargetUnit() {
        Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(12.0, LengthUnit.INCH);
        Quantity<LengthUnit> result = Quantity.add(q1, q2, LengthUnit.YARD);
        assertTrue(result.equals(new Quantity<>(2.0 / 3.0, LengthUnit.YARD)));
    }

    @Test
    void testQuantityLengthRefactored_NullUnit() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(1.0, null));
    }

    @Test
    void testQuantityLengthRefactored_InvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(Double.NaN, LengthUnit.FEET));
    }

    // ─────────────────────────────────────────────
    // UC8: Backward compatibility – all UC1–UC7 operations via refactored design
    // ─────────────────────────────────────────────

    @Test
    void testBackwardCompatibility_UC1EqualityTests() {
        assertTrue(new Quantity<>(0.0, LengthUnit.FEET).equals(new Quantity<>(0.0, LengthUnit.FEET)));
        assertFalse(new Quantity<>(0.0, LengthUnit.FEET).equals(new Quantity<>(1.0, LengthUnit.FEET)));
    }

    @Test
    void testBackwardCompatibility_UC5ConversionTests() {
        assertEquals(12.0, Quantity.convert(1.0, LengthUnit.FEET, LengthUnit.INCH), EPSILON);
        assertEquals(36.0, Quantity.convert(1.0, LengthUnit.YARD, LengthUnit.INCH), EPSILON);
        assertEquals(2.0, Quantity.convert(6.0, LengthUnit.FEET, LengthUnit.YARD), EPSILON);
    }

    @Test
    void testBackwardCompatibility_UC6AdditionTests() {
        Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(12.0, LengthUnit.INCH);
        Quantity<LengthUnit> result = q1.add(q2);
        assertTrue(result.equals(new Quantity<>(2.0, LengthUnit.FEET)));
    }

    @Test
    void testBackwardCompatibility_UC7AdditionWithTargetUnitTests() {
        Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(12.0, LengthUnit.INCH);
        Quantity<LengthUnit> result = Quantity.add(q1, q2, LengthUnit.YARD);
        assertTrue(result.equals(new Quantity<>(2.0 / 3.0, LengthUnit.YARD)));
    }

    // ─────────────────────────────────────────────
    // UC8: Round-trip & immutability
    // ─────────────────────────────────────────────

    @Test
    void testRoundTripConversion_RefactoredDesign() {
        double original = 5.0;
        double toInches = Quantity.convert(original, LengthUnit.FEET, LengthUnit.INCH);
        double backToFeet = Quantity.convert(toInches, LengthUnit.INCH, LengthUnit.FEET);
        assertEquals(original, backToFeet, EPSILON);
    }

    @Test
    void testUnitImmutability() {
        // Enum constants are inherently immutable and thread-safe
        double factor1 = LengthUnit.FEET.getConversionFactor();
        double factor2 = LengthUnit.FEET.getConversionFactor();
        assertEquals(factor1, factor2, EPSILON);
        assertSame(LengthUnit.FEET, LengthUnit.valueOf("FEET"));
    }

    // ─────────────────────────────────────────────
    // UC8: Architectural scalability
    // ─────────────────────────────────────────────

    @Test
    void testArchitecturalScalability_MultipleCategories() {
        // Verify that LengthUnit is a standalone top-level class,
        // not nested within Quantity<LengthUnit> — proves no circular dependency.
        assertTrue(LengthUnit.class.isEnum());
        assertNull(LengthUnit.class.getEnclosingClass(),
                "LengthUnit must be a standalone top-level enum, not nested inside another class");
    }

    // ═════════════════════════════════════════════
    // UC9: Weight Measurement Tests
    // ═════════════════════════════════════════════

    // ─────────────────────────────────────────────
    // UC9: WeightUnit enum constants
    // ─────────────────────────────────────────────

    @Test
    void testWeightUnitEnum_KilogramConstant() {
        assertEquals(1.0, WeightUnit.KILOGRAM.getConversionFactor(), EPSILON);
    }

    @Test
    void testWeightUnitEnum_GramConstant() {
        assertEquals(0.001, WeightUnit.GRAM.getConversionFactor(), EPSILON);
    }

    @Test
    void testWeightUnitEnum_PoundConstant() {
        assertEquals(0.453592, WeightUnit.POUND.getConversionFactor(), EPSILON);
    }

    // ─────────────────────────────────────────────
    // UC9: WeightUnit convertToBaseUnit / convertFromBaseUnit
    // ─────────────────────────────────────────────

    @Test
    void testWeightConvertToBaseUnit_KilogramToKilogram() {
        assertEquals(5.0, WeightUnit.KILOGRAM.convertToBaseUnit(5.0), EPSILON);
    }

    @Test
    void testWeightConvertToBaseUnit_GramToKilogram() {
        assertEquals(1.0, WeightUnit.GRAM.convertToBaseUnit(1000.0), EPSILON);
    }

    @Test
    void testWeightConvertToBaseUnit_PoundToKilogram() {
        assertEquals(0.453592, WeightUnit.POUND.convertToBaseUnit(1.0), EPSILON);
    }

    @Test
    void testWeightConvertFromBaseUnit_KilogramToKilogram() {
        assertEquals(2.0, WeightUnit.KILOGRAM.convertFromBaseUnit(2.0), EPSILON);
    }

    @Test
    void testWeightConvertFromBaseUnit_KilogramToGram() {
        assertEquals(1000.0, WeightUnit.GRAM.convertFromBaseUnit(1.0), EPSILON);
    }

    @Test
    void testWeightConvertFromBaseUnit_KilogramToPound() {
        assertEquals(2.20462, WeightUnit.POUND.convertFromBaseUnit(1.0), 1e-4);
    }

    // ─────────────────────────────────────────────
    // UC9: Weight equality tests
    // ─────────────────────────────────────────────

    @Test
    void testEquality_KilogramToKilogram_SameValue() {
        assertTrue(new Quantity<>(1.0, WeightUnit.KILOGRAM).equals(new Quantity<>(1.0, WeightUnit.KILOGRAM)));
    }

    @Test
    void testEquality_KilogramToKilogram_DifferentValue() {
        assertFalse(new Quantity<>(1.0, WeightUnit.KILOGRAM).equals(new Quantity<>(2.0, WeightUnit.KILOGRAM)));
    }

    @Test
    void testEquality_GramToGram_SameValue() {
        assertTrue(new Quantity<>(500.0, WeightUnit.GRAM).equals(new Quantity<>(500.0, WeightUnit.GRAM)));
    }

    @Test
    void testEquality_PoundToPound_SameValue() {
        assertTrue(new Quantity<>(2.0, WeightUnit.POUND).equals(new Quantity<>(2.0, WeightUnit.POUND)));
    }

    @Test
    void testEquality_KilogramToGram_EquivalentValue() {
        assertTrue(new Quantity<>(1.0, WeightUnit.KILOGRAM).equals(new Quantity<>(1000.0, WeightUnit.GRAM)));
    }

    @Test
    void testEquality_GramToKilogram_EquivalentValue() {
        assertTrue(new Quantity<>(1000.0, WeightUnit.GRAM).equals(new Quantity<>(1.0, WeightUnit.KILOGRAM)));
    }

    @Test
    void testEquality_KilogramToPound_EquivalentValue() {
        assertTrue(new Quantity<>(1.0, WeightUnit.KILOGRAM).equals(new Quantity<>(2.2046226218, WeightUnit.POUND)));
    }

    @Test
    void testEquality_GramToPound_EquivalentValue() {
        assertTrue(new Quantity<>(453.592, WeightUnit.GRAM).equals(new Quantity<>(1.0, WeightUnit.POUND)));
    }

    @Test
    void testEquality_Weight_SameReference() {
        Quantity<WeightUnit> w = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        assertTrue(w.equals(w));
    }

    @Test
    void testEquality_Weight_NullComparison() {
        assertFalse(new Quantity<>(1.0, WeightUnit.KILOGRAM).equals(null));
    }

    @Test
    void testEquality_Weight_TransitiveProperty() {
        Quantity<WeightUnit> a = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> b = new Quantity<>(1000.0, WeightUnit.GRAM);
        Quantity<WeightUnit> c = new Quantity<>(2.2046226218, WeightUnit.POUND);
        assertTrue(a.equals(b));
        assertTrue(b.equals(c));
        assertTrue(a.equals(c));
    }

    @Test
    void testEquality_Weight_ZeroValue() {
        assertTrue(new Quantity<>(0.0, WeightUnit.KILOGRAM).equals(new Quantity<>(0.0, WeightUnit.GRAM)));
    }

    @Test
    void testEquality_Weight_NegativeValue() {
        assertTrue(new Quantity<>(-1.0, WeightUnit.KILOGRAM).equals(new Quantity<>(-1000.0, WeightUnit.GRAM)));
    }

    @Test
    void testEquality_Weight_LargeValue() {
        assertTrue(new Quantity<>(1000000.0, WeightUnit.GRAM).equals(new Quantity<>(1000.0, WeightUnit.KILOGRAM)));
    }

    @Test
    void testEquality_Weight_SmallValue() {
        assertTrue(new Quantity<>(0.001, WeightUnit.KILOGRAM).equals(new Quantity<>(1.0, WeightUnit.GRAM)));
    }

    // ─────────────────────────────────────────────
    // UC9: Weight vs Length incompatibility
    // ─────────────────────────────────────────────

    @Test
    void testEquality_WeightVsLength_Incompatible() {
        assertFalse(new Quantity<>(1.0, WeightUnit.KILOGRAM).equals(new Quantity<>(1.0, LengthUnit.FEET)));
    }

    // ─────────────────────────────────────────────
    // UC9: Weight constructor validation
    // ─────────────────────────────────────────────

    @Test
    void testWeight_NullUnit_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(1.0, null));
    }

    @Test
    void testWeight_NaNValue_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(Double.NaN, WeightUnit.KILOGRAM));
    }

    @Test
    void testWeight_InfiniteValue_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(Double.POSITIVE_INFINITY, WeightUnit.KILOGRAM));
    }

    // ─────────────────────────────────────────────
    // UC9: Weight conversion tests
    // ─────────────────────────────────────────────

    @Test
    void testConversion_KilogramToGram() {
        Quantity<WeightUnit> result = new Quantity<>(1.0, WeightUnit.KILOGRAM).convertTo(WeightUnit.GRAM);
        assertTrue(result.equals(new Quantity<>(1000.0, WeightUnit.GRAM)));
    }

    @Test
    void testConversion_PoundToKilogram() {
        Quantity<WeightUnit> result = new Quantity<>(2.2046226218, WeightUnit.POUND).convertTo(WeightUnit.KILOGRAM);
        assertTrue(result.equals(new Quantity<>(1.0, WeightUnit.KILOGRAM)));
    }

    @Test
    void testConversion_KilogramToPound() {
        Quantity<WeightUnit> result = new Quantity<>(1.0, WeightUnit.KILOGRAM).convertTo(WeightUnit.POUND);
        assertTrue(result.equals(new Quantity<>(2.2046226218, WeightUnit.POUND)));
    }

    @Test
    void testConversion_WeightSameUnit() {
        assertEquals(5.0, Quantity.convert(5.0, WeightUnit.KILOGRAM, WeightUnit.KILOGRAM), EPSILON);
    }

    @Test
    void testConversion_WeightZeroValue() {
        assertEquals(0.0, Quantity.convert(0.0, WeightUnit.KILOGRAM, WeightUnit.GRAM), EPSILON);
    }

    @Test
    void testConversion_WeightNegativeValue() {
        assertEquals(-1000.0, Quantity.convert(-1.0, WeightUnit.KILOGRAM, WeightUnit.GRAM), EPSILON);
    }

    @Test
    void testConversion_WeightRoundTrip() {
        double original = 1.5;
        double toGram = Quantity.convert(original, WeightUnit.KILOGRAM, WeightUnit.GRAM);
        double backToKg = Quantity.convert(toGram, WeightUnit.GRAM, WeightUnit.KILOGRAM);
        assertEquals(original, backToKg, EPSILON);
    }

    // ─────────────────────────────────────────────
    // UC9: Weight addition tests
    // ─────────────────────────────────────────────

    @Test
    void testAddition_SameUnit_KilogramPlusKilogram() {
        Quantity<WeightUnit> q1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> q2 = new Quantity<>(2.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> result = q1.add(q2);
        assertTrue(result.equals(new Quantity<>(3.0, WeightUnit.KILOGRAM)));
    }

    @Test
    void testAddition_CrossUnit_KilogramPlusGram() {
        Quantity<WeightUnit> q1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> q2 = new Quantity<>(1000.0, WeightUnit.GRAM);
        Quantity<WeightUnit> result = q1.add(q2);
        assertTrue(result.equals(new Quantity<>(2.0, WeightUnit.KILOGRAM)));
    }

    @Test
    void testAddition_CrossUnit_GramPlusKilogram() {
        Quantity<WeightUnit> q1 = new Quantity<>(500.0, WeightUnit.GRAM);
        Quantity<WeightUnit> q2 = new Quantity<>(0.5, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> result = q1.add(q2);
        assertTrue(result.equals(new Quantity<>(1000.0, WeightUnit.GRAM)));
    }

    @Test
    void testAddition_CrossUnit_PoundPlusKilogram() {
        Quantity<WeightUnit> q1 = new Quantity<>(2.2046226218, WeightUnit.POUND);
        Quantity<WeightUnit> q2 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> result = q1.add(q2);
        assertTrue(result.equals(new Quantity<>(4.4092452436, WeightUnit.POUND)));
    }

    @Test
    void testAddition_ExplicitTargetUnit_Gram() {
        Quantity<WeightUnit> q1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> q2 = new Quantity<>(1000.0, WeightUnit.GRAM);
        Quantity<WeightUnit> result = Quantity.add(q1, q2, WeightUnit.GRAM);
        assertTrue(result.equals(new Quantity<>(2000.0, WeightUnit.GRAM)));
    }

    @Test
    void testAddition_Weight_Commutativity() {
        Quantity<WeightUnit> q1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> q2 = new Quantity<>(1000.0, WeightUnit.GRAM);
        Quantity<WeightUnit> result1 = Quantity.add(q1, q2, WeightUnit.GRAM);
        Quantity<WeightUnit> result2 = Quantity.add(q2, q1, WeightUnit.GRAM);
        assertTrue(result1.equals(result2));
    }

    @Test
    void testAddition_Weight_WithZero() {
        Quantity<WeightUnit> q1 = new Quantity<>(5.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> q2 = new Quantity<>(0.0, WeightUnit.GRAM);
        Quantity<WeightUnit> result = q1.add(q2);
        assertTrue(result.equals(new Quantity<>(5.0, WeightUnit.KILOGRAM)));
    }

    @Test
    void testAddition_Weight_NegativeValues() {
        Quantity<WeightUnit> q1 = new Quantity<>(5.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> q2 = new Quantity<>(-2000.0, WeightUnit.GRAM);
        Quantity<WeightUnit> result = q1.add(q2);
        assertTrue(result.equals(new Quantity<>(3.0, WeightUnit.KILOGRAM)));
    }

    @Test
    void testAddition_Weight_LargeValues() {
        Quantity<WeightUnit> q1 = new Quantity<>(1e6, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> q2 = new Quantity<>(1e6, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> result = q1.add(q2);
        assertTrue(result.equals(new Quantity<>(2e6, WeightUnit.KILOGRAM)));
    }

    @Test
    void testAddition_Weight_NullOperand() {
        Quantity<WeightUnit> q1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        assertThrows(IllegalArgumentException.class, () -> q1.add(null));
    }

    // ═════════════════════════════════════════════
    // UC12: Subtraction Tests
    // ═════════════════════════════════════════════

    // ─────────────────────────────────────────────
    // UC12: Subtraction with same units
    // ─────────────────────────────────────────────

    @Test
    void testSubtraction_SameUnit_FeetMinusFeet() {
        Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET).subtract(new Quantity<>(5.0, LengthUnit.FEET));
        assertTrue(result.equals(new Quantity<>(5.0, LengthUnit.FEET)));
    }

    @Test
    void testSubtraction_SameUnit_LitreMinusLitre() {
        Quantity<VolumeUnit> result = new Quantity<>(10.0, VolumeUnit.LITRE).subtract(new Quantity<>(3.0, VolumeUnit.LITRE));
        assertTrue(result.equals(new Quantity<>(7.0, VolumeUnit.LITRE)));
    }

    // ─────────────────────────────────────────────
    // UC12: Subtraction with different units (same category)
    // ─────────────────────────────────────────────

    @Test
    void testSubtraction_CrossUnit_FeetMinusInches() {
        // 10 feet - 6 inches = 9.5 feet
        Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET).subtract(new Quantity<>(6.0, LengthUnit.INCH));
        assertTrue(result.equals(new Quantity<>(9.5, LengthUnit.FEET)));
    }

    @Test
    void testSubtraction_CrossUnit_InchesMinusFeet() {
        // 120 inches - 5 feet = 60 inches
        Quantity<LengthUnit> result = new Quantity<>(120.0, LengthUnit.INCH).subtract(new Quantity<>(5.0, LengthUnit.FEET));
        assertTrue(result.equals(new Quantity<>(60.0, LengthUnit.INCH)));
    }

    @Test
    void testSubtraction_CrossUnit_KilogramMinusGram() {
        // 10 kg - 5000 g = 5 kg
        Quantity<WeightUnit> result = new Quantity<>(10.0, WeightUnit.KILOGRAM).subtract(new Quantity<>(5000.0, WeightUnit.GRAM));
        assertTrue(result.equals(new Quantity<>(5.0, WeightUnit.KILOGRAM)));
    }

    // ─────────────────────────────────────────────
    // UC12: Subtraction with explicit target unit
    // ─────────────────────────────────────────────

    @Test
    void testSubtraction_ExplicitTargetUnit_Feet() {
        // 10 feet - 6 inches → result in feet = 9.5
        Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET).subtract(new Quantity<>(6.0, LengthUnit.INCH), LengthUnit.FEET);
        assertTrue(result.equals(new Quantity<>(9.5, LengthUnit.FEET)));
    }

    @Test
    void testSubtraction_ExplicitTargetUnit_Inches() {
        // 10 feet - 6 inches → result in inches = 114.0
        Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET).subtract(new Quantity<>(6.0, LengthUnit.INCH), LengthUnit.INCH);
        assertTrue(result.equals(new Quantity<>(114.0, LengthUnit.INCH)));
    }

    @Test
    void testSubtraction_ExplicitTargetUnit_Millilitre() {
        // 5 litre - 2 litre → result in millilitre = 3000.0
        Quantity<VolumeUnit> result = new Quantity<>(5.0, VolumeUnit.LITRE).subtract(new Quantity<>(2.0, VolumeUnit.LITRE), VolumeUnit.MILLILITRE);
        assertTrue(result.equals(new Quantity<>(3000.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    void testSubtraction_ExplicitTargetUnit_Gram() {
        // 10 kg - 5000 g → result in grams = 5000.0
        Quantity<WeightUnit> result = new Quantity<>(10.0, WeightUnit.KILOGRAM).subtract(new Quantity<>(5000.0, WeightUnit.GRAM), WeightUnit.GRAM);
        assertTrue(result.equals(new Quantity<>(5000.0, WeightUnit.GRAM)));
    }

    // ─────────────────────────────────────────────
    // UC12: Subtraction resulting in negative values
    // ─────────────────────────────────────────────

    @Test
    void testSubtraction_ResultingInNegative() {
        // 5 feet - 10 feet = -5 feet
        Quantity<LengthUnit> result = new Quantity<>(5.0, LengthUnit.FEET).subtract(new Quantity<>(10.0, LengthUnit.FEET));
        assertTrue(result.equals(new Quantity<>(-5.0, LengthUnit.FEET)));
    }

    @Test
    void testSubtraction_ResultingInNegative_Weight() {
        // 2 kg - 5 kg = -3 kg
        Quantity<WeightUnit> result = new Quantity<>(2.0, WeightUnit.KILOGRAM).subtract(new Quantity<>(5.0, WeightUnit.KILOGRAM));
        assertTrue(result.equals(new Quantity<>(-3.0, WeightUnit.KILOGRAM)));
    }

    // ─────────────────────────────────────────────
    // UC12: Subtraction resulting in zero
    // ─────────────────────────────────────────────

    @Test
    void testSubtraction_ResultingInZero() {
        // 10 feet - 120 inches = 0 feet
        Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET).subtract(new Quantity<>(120.0, LengthUnit.INCH));
        assertTrue(result.equals(new Quantity<>(0.0, LengthUnit.FEET)));
    }

    @Test
    void testSubtraction_ResultingInZero_Volume() {
        // 1 litre - 1000 ml = 0 litre
        Quantity<VolumeUnit> result = new Quantity<>(1.0, VolumeUnit.LITRE).subtract(new Quantity<>(1000.0, VolumeUnit.MILLILITRE));
        assertTrue(result.equals(new Quantity<>(0.0, VolumeUnit.LITRE)));
    }

    // ─────────────────────────────────────────────
    // UC12: Subtraction with zero operand
    // ─────────────────────────────────────────────

    @Test
    void testSubtraction_WithZeroOperand() {
        // 5 feet - 0 inches = 5 feet (identity property)
        Quantity<LengthUnit> result = new Quantity<>(5.0, LengthUnit.FEET).subtract(new Quantity<>(0.0, LengthUnit.INCH));
        assertTrue(result.equals(new Quantity<>(5.0, LengthUnit.FEET)));
    }

    // ─────────────────────────────────────────────
    // UC12: Subtraction with negative values
    // ─────────────────────────────────────────────

    @Test
    void testSubtraction_WithNegativeValues() {
        // 5 feet - (-2 feet) = 7 feet (subtracting negative = adding)
        Quantity<LengthUnit> result = new Quantity<>(5.0, LengthUnit.FEET).subtract(new Quantity<>(-2.0, LengthUnit.FEET));
        assertTrue(result.equals(new Quantity<>(7.0, LengthUnit.FEET)));
    }

    // ─────────────────────────────────────────────
    // UC12: Subtraction non-commutativity
    // ─────────────────────────────────────────────

    @Test
    void testSubtraction_NonCommutative() {
        Quantity<LengthUnit> a = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(5.0, LengthUnit.FEET);
        Quantity<LengthUnit> aMb = a.subtract(b); // 5.0
        Quantity<LengthUnit> bMa = b.subtract(a); // -5.0
        assertTrue(aMb.equals(new Quantity<>(5.0, LengthUnit.FEET)));
        assertTrue(bMa.equals(new Quantity<>(-5.0, LengthUnit.FEET)));
        assertFalse(aMb.equals(bMa));
    }

    // ─────────────────────────────────────────────
    // UC12: Subtraction with large values
    // ─────────────────────────────────────────────

    @Test
    void testSubtraction_WithLargeValues() {
        Quantity<WeightUnit> result = new Quantity<>(1e6, WeightUnit.KILOGRAM).subtract(new Quantity<>(5e5, WeightUnit.KILOGRAM));
        assertTrue(result.equals(new Quantity<>(5e5, WeightUnit.KILOGRAM)));
    }

    // ─────────────────────────────────────────────
    // UC12: Subtraction with small values
    // ─────────────────────────────────────────────

    @Test
    void testSubtraction_WithSmallValues() {
        Quantity<LengthUnit> result = new Quantity<>(0.01, LengthUnit.FEET).subtract(new Quantity<>(0.005, LengthUnit.FEET));
        assertEquals(0.01, Math.round(result.toString().contains("0.01") ? 0.01 : 0.0), EPSILON);
        // Rounded to 2dp → 0.01 (or 0.0 depending on precision)
        assertTrue(result.equals(new Quantity<>(0.01, LengthUnit.FEET)));
    }

    // ─────────────────────────────────────────────
    // UC12: Subtraction null operand
    // ─────────────────────────────────────────────

    @Test
    void testSubtraction_NullOperand() {
        Quantity<LengthUnit> q = new Quantity<>(10.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> q.subtract(null));
    }

    // ─────────────────────────────────────────────
    // UC12: Subtraction null target unit
    // ─────────────────────────────────────────────

    @Test
    void testSubtraction_NullTargetUnit() {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(5.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> q1.subtract(q2, null));
    }

    // ─────────────────────────────────────────────
    // UC12: Subtraction all measurement categories
    // ─────────────────────────────────────────────

    @Test
    void testSubtraction_AllMeasurementCategories() {
        // Length
        Quantity<LengthUnit> lenResult = new Quantity<>(10.0, LengthUnit.FEET).subtract(new Quantity<>(3.0, LengthUnit.FEET));
        assertTrue(lenResult.equals(new Quantity<>(7.0, LengthUnit.FEET)));

        // Weight
        Quantity<WeightUnit> wgtResult = new Quantity<>(5.0, WeightUnit.KILOGRAM).subtract(new Quantity<>(2.0, WeightUnit.KILOGRAM));
        assertTrue(wgtResult.equals(new Quantity<>(3.0, WeightUnit.KILOGRAM)));

        // Volume
        Quantity<VolumeUnit> volResult = new Quantity<>(10.0, VolumeUnit.LITRE).subtract(new Quantity<>(4.0, VolumeUnit.LITRE));
        assertTrue(volResult.equals(new Quantity<>(6.0, VolumeUnit.LITRE)));
    }

    // ─────────────────────────────────────────────
    // UC12: Subtraction chained operations
    // ─────────────────────────────────────────────

    @Test
    void testSubtraction_ChainedOperations() {
        // 10 feet - 2 feet - 1 feet = 7 feet
        Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
                .subtract(new Quantity<>(2.0, LengthUnit.FEET))
                .subtract(new Quantity<>(1.0, LengthUnit.FEET));
        assertTrue(result.equals(new Quantity<>(7.0, LengthUnit.FEET)));
    }

    // ─────────────────────────────────────────────
    // UC12: Subtraction immutability
    // ─────────────────────────────────────────────

    @Test
    void testSubtraction_Immutability() {
        Quantity<LengthUnit> original = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> other = new Quantity<>(5.0, LengthUnit.FEET);
        Quantity<LengthUnit> result = original.subtract(other);
        // Original should be unchanged
        assertTrue(original.equals(new Quantity<>(10.0, LengthUnit.FEET)));
        assertTrue(other.equals(new Quantity<>(5.0, LengthUnit.FEET)));
        assertNotSame(original, result);
    }

    // ─────────────────────────────────────────────
    // UC12: Subtraction and addition inverse
    // ─────────────────────────────────────────────

    @Test
    void testSubtractionAddition_Inverse() {
        // A + B - B ≈ A
        Quantity<LengthUnit> a = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(5.0, LengthUnit.FEET);
        Quantity<LengthUnit> result = a.add(b).subtract(b);
        assertTrue(result.equals(a));
    }

    // ═════════════════════════════════════════════
    // UC12: Division Tests
    // ═════════════════════════════════════════════

    // ─────────────────────────────────────────────
    // UC12: Division with same units
    // ─────────────────────────────────────────────

    @Test
    void testDivision_SameUnit_FeetDividedByFeet() {
        double result = new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(2.0, LengthUnit.FEET));
        assertEquals(5.0, result, EPSILON);
    }

    @Test
    void testDivision_SameUnit_LitreDividedByLitre() {
        double result = new Quantity<>(10.0, VolumeUnit.LITRE).divide(new Quantity<>(5.0, VolumeUnit.LITRE));
        assertEquals(2.0, result, EPSILON);
    }

    // ─────────────────────────────────────────────
    // UC12: Division with different units (same category)
    // ─────────────────────────────────────────────

    @Test
    void testDivision_CrossUnit_FeetDividedByInches() {
        // 24 inches / 2 feet = 1.0 (both become 2 feet in base)
        double result = new Quantity<>(24.0, LengthUnit.INCH).divide(new Quantity<>(2.0, LengthUnit.FEET));
        assertEquals(1.0, result, EPSILON);
    }

    @Test
    void testDivision_CrossUnit_KilogramDividedByGram() {
        // 2 kg / 2000 g = 1.0
        double result = new Quantity<>(2.0, WeightUnit.KILOGRAM).divide(new Quantity<>(2000.0, WeightUnit.GRAM));
        assertEquals(1.0, result, EPSILON);
    }

    @Test
    void testDivision_CrossUnit_InchDividedByFeet() {
        // 12 inches / 1 foot = 1.0
        double result = new Quantity<>(12.0, LengthUnit.INCH).divide(new Quantity<>(1.0, LengthUnit.FEET));
        assertEquals(1.0, result, EPSILON);
    }

    @Test
    void testDivision_CrossUnit_GramDividedByKilogram() {
        // 2000 g / 1 kg = 2.0
        double result = new Quantity<>(2000.0, WeightUnit.GRAM).divide(new Quantity<>(1.0, WeightUnit.KILOGRAM));
        assertEquals(2.0, result, EPSILON);
    }

    @Test
    void testDivision_CrossUnit_MillilitreDividedByLitre() {
        // 1000 ml / 1 l = 1.0
        double result = new Quantity<>(1000.0, VolumeUnit.MILLILITRE).divide(new Quantity<>(1.0, VolumeUnit.LITRE));
        assertEquals(1.0, result, EPSILON);
    }

    // ─────────────────────────────────────────────
    // UC12: Division ratio > 1, < 1, = 1
    // ─────────────────────────────────────────────

    @Test
    void testDivision_RatioGreaterThanOne() {
        double result = new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(2.0, LengthUnit.FEET));
        assertEquals(5.0, result, EPSILON);
        assertTrue(result > 1.0);
    }

    @Test
    void testDivision_RatioLessThanOne() {
        double result = new Quantity<>(5.0, LengthUnit.FEET).divide(new Quantity<>(10.0, LengthUnit.FEET));
        assertEquals(0.5, result, EPSILON);
        assertTrue(result < 1.0);
    }

    @Test
    void testDivision_RatioEqualToOne() {
        double result = new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(10.0, LengthUnit.FEET));
        assertEquals(1.0, result, EPSILON);
    }

    // ─────────────────────────────────────────────
    // UC12: Division non-commutativity
    // ─────────────────────────────────────────────

    @Test
    void testDivision_NonCommutative() {
        Quantity<LengthUnit> a = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(5.0, LengthUnit.FEET);
        double aDb = a.divide(b); // 2.0
        double bDa = b.divide(a); // 0.5
        assertEquals(2.0, aDb, EPSILON);
        assertEquals(0.5, bDa, EPSILON);
        // Reciprocal relationship
        assertEquals(1.0, aDb * bDa, EPSILON);
    }

    // ─────────────────────────────────────────────
    // UC12: Division by zero
    // ─────────────────────────────────────────────

    @Test
    void testDivision_ByZero() {
        Quantity<LengthUnit> q = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> zero = new Quantity<>(0.0, LengthUnit.FEET);
        assertThrows(ArithmeticException.class, () -> q.divide(zero));
    }

    // ─────────────────────────────────────────────
    // UC12: Division with large/small ratios
    // ─────────────────────────────────────────────

    @Test
    void testDivision_WithLargeRatio() {
        double result = new Quantity<>(1e6, WeightUnit.KILOGRAM).divide(new Quantity<>(1.0, WeightUnit.KILOGRAM));
        assertEquals(1e6, result, EPSILON);
    }

    @Test
    void testDivision_WithSmallRatio() {
        double result = new Quantity<>(1.0, WeightUnit.KILOGRAM).divide(new Quantity<>(1e6, WeightUnit.KILOGRAM));
        assertEquals(1e-6, result, EPSILON);
    }

    // ─────────────────────────────────────────────
    // UC12: Division null operand
    // ─────────────────────────────────────────────

    @Test
    void testDivision_NullOperand() {
        Quantity<LengthUnit> q = new Quantity<>(10.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> q.divide(null));
    }

    // ─────────────────────────────────────────────
    // UC12: Division all measurement categories
    // ─────────────────────────────────────────────

    @Test
    void testDivision_AllMeasurementCategories() {
        // Length
        assertEquals(2.0, new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(5.0, LengthUnit.FEET)), EPSILON);

        // Weight
        assertEquals(2.0, new Quantity<>(10.0, WeightUnit.KILOGRAM).divide(new Quantity<>(5.0, WeightUnit.KILOGRAM)), EPSILON);

        // Volume
        assertEquals(2.0, new Quantity<>(10.0, VolumeUnit.LITRE).divide(new Quantity<>(5.0, VolumeUnit.LITRE)), EPSILON);
    }

    // ─────────────────────────────────────────────
    // UC12: Division associativity test
    // ─────────────────────────────────────────────

    @Test
    void testDivision_Associativity() {
        Quantity<LengthUnit> a = new Quantity<>(100.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(5.0, LengthUnit.FEET);
        Quantity<LengthUnit> c = new Quantity<>(2.0, LengthUnit.FEET);

        // (A ÷ B) ÷ C ≠ A ÷ (B ÷ C) — non-associative
        // (100/5)/2 = 10
        // 100/(5/2) = 100/2.5 = 40
        double leftAssoc = a.divide(b) / c.divide(new Quantity<>(1.0, LengthUnit.FEET));
        // Simplified: (A/B) / C_value
        double abResult = a.divide(b); // 20
        double abDivC = abResult / 2.0; // 10
        double bcResult = b.divide(c); // 2.5
        double aDivBc = 100.0 / bcResult; // 40

        assertNotEquals(abDivC, aDivBc, EPSILON, "Division should be non-associative");
    }

    // ─────────────────────────────────────────────
    // UC12: Division immutability
    // ─────────────────────────────────────────────

    @Test
    void testDivision_Immutability() {
        Quantity<LengthUnit> original = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> divisor = new Quantity<>(5.0, LengthUnit.FEET);
        double result = original.divide(divisor);
        assertEquals(2.0, result, EPSILON);
        // Originals unchanged
        assertTrue(original.equals(new Quantity<>(10.0, LengthUnit.FEET)));
        assertTrue(divisor.equals(new Quantity<>(5.0, LengthUnit.FEET)));
    }

    // ─────────────────────────────────────────────
    // UC12: Integration — subtraction and division coexist
    // ─────────────────────────────────────────────

    @Test
    void testSubtractionAndDivision_Integration() {
        // A.subtract(B).divide(C) should work seamlessly
        Quantity<LengthUnit> a = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(4.0, LengthUnit.FEET);
        Quantity<LengthUnit> c = new Quantity<>(3.0, LengthUnit.FEET);
        // (10 - 4) / 3 = 6 / 3 = 2.0
        double result = a.subtract(b).divide(c);
        assertEquals(2.0, result, EPSILON);
    }

    // ─────────────────────────────────────────────
    // UC12: Subtraction precision and rounding
    // ─────────────────────────────────────────────

    @Test
    void testSubtraction_PrecisionAndRounding() {
        // Verify result is rounded to two decimal places
        // 1/3 feet ≈ 0.333333... → should be rounded to 0.33
        Quantity<LengthUnit> result = new Quantity<>(1.0, LengthUnit.FEET).subtract(new Quantity<>(8.0, LengthUnit.INCH));
        // 1 foot - 8 inches = 1 - 0.666... = 0.333... feet → rounded to 0.33
        assertTrue(result.equals(new Quantity<>(0.33, LengthUnit.FEET)));
    }

    // ─────────────────────────────────────────────
    // UC12: Division precision handling
    // ─────────────────────────────────────────────

    @Test
    void testDivision_PrecisionHandling() {
        // Division returns raw double — no rounding
        double result = new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(3.0, LengthUnit.FEET));
        assertEquals(10.0 / 3.0, result, EPSILON);
    }

    // UC11: Volume Measurement Tests
    // ═════════════════════════════════════════════

    // ─────────────────────────────────────────────
    // UC11: VolumeUnit enum constants
    // ─────────────────────────────────────────────

    @Test
    void testVolumeUnitEnum_LitreConstant() {
        assertEquals(1.0, VolumeUnit.LITRE.getConversionFactor(), EPSILON);
    }

    @Test
    void testVolumeUnitEnum_MillilitreConstant() {
        assertEquals(0.001, VolumeUnit.MILLILITRE.getConversionFactor(), EPSILON);
    }

    @Test
    void testVolumeUnitEnum_GallonConstant() {
        assertEquals(3.78541, VolumeUnit.GALLON.getConversionFactor(), EPSILON);
    }

    // ─────────────────────────────────────────────
    // UC11: VolumeUnit convertToBaseUnit / convertFromBaseUnit
    // ─────────────────────────────────────────────

    @Test
    void testConvertToBaseUnit_LitreToLitre() {
        assertEquals(5.0, VolumeUnit.LITRE.convertToBaseUnit(5.0), EPSILON);
    }

    @Test
    void testConvertToBaseUnit_MillilitreToLitre() {
        assertEquals(1.0, VolumeUnit.MILLILITRE.convertToBaseUnit(1000.0), EPSILON);
    }

    @Test
    void testConvertToBaseUnit_GallonToLitre() {
        assertEquals(3.78541, VolumeUnit.GALLON.convertToBaseUnit(1.0), EPSILON);
    }

    @Test
    void testConvertFromBaseUnit_LitreToLitre() {
        assertEquals(2.0, VolumeUnit.LITRE.convertFromBaseUnit(2.0), EPSILON);
    }

    @Test
    void testConvertFromBaseUnit_LitreToMillilitre() {
        assertEquals(1000.0, VolumeUnit.MILLILITRE.convertFromBaseUnit(1.0), EPSILON);
    }

    @Test
    void testConvertFromBaseUnit_LitreToGallon() {
        assertEquals(1.0, VolumeUnit.GALLON.convertFromBaseUnit(3.78541), EPSILON);
    }

    // ─────────────────────────────────────────────
    // UC11: Volume equality tests
    // ─────────────────────────────────────────────

    @Test
    void testEquality_LitreToLitre_SameValue() {
        assertTrue(new Quantity<>(1.0, VolumeUnit.LITRE).equals(new Quantity<>(1.0, VolumeUnit.LITRE)));
    }

    @Test
    void testEquality_LitreToLitre_DifferentValue() {
        assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE).equals(new Quantity<>(2.0, VolumeUnit.LITRE)));
    }

    @Test
    void testEquality_LitreToMillilitre_EquivalentValue() {
        assertTrue(new Quantity<>(1.0, VolumeUnit.LITRE).equals(new Quantity<>(1000.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    void testEquality_MillilitreToLitre_EquivalentValue() {
        assertTrue(new Quantity<>(1000.0, VolumeUnit.MILLILITRE).equals(new Quantity<>(1.0, VolumeUnit.LITRE)));
    }

    @Test
    void testEquality_LitreToGallon_EquivalentValue() {
        assertTrue(new Quantity<>(1.0, VolumeUnit.LITRE).equals(new Quantity<>(1.0 / 3.78541, VolumeUnit.GALLON)));
    }

    @Test
    void testEquality_GallonToLitre_EquivalentValue() {
        assertTrue(new Quantity<>(1.0, VolumeUnit.GALLON).equals(new Quantity<>(3.78541, VolumeUnit.LITRE)));
    }

    @Test
    void testEquality_VolumeVsLength_Incompatible() {
        assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE).equals(new Quantity<>(1.0, LengthUnit.FEET)));
    }

    @Test
    void testEquality_VolumeVsWeight_Incompatible() {
        assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE).equals(new Quantity<>(1.0, WeightUnit.KILOGRAM)));
    }

    @Test
    void testEquality_Volume_NullComparison() {
        assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE).equals(null));
    }

    @Test
    void testEquality_Volume_SameReference() {
        Quantity<VolumeUnit> vol = new Quantity<>(1.0, VolumeUnit.LITRE);
        assertTrue(vol.equals(vol));
    }

    @Test
    void testEquality_Volume_NullUnit() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(1.0, null));
    }

    @Test
    void testEquality_Volume_TransitiveProperty() {
        Quantity<VolumeUnit> a = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> b = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        Quantity<VolumeUnit> c = new Quantity<>(1.0 / 3.78541, VolumeUnit.GALLON);
        assertTrue(a.equals(b));
        assertTrue(b.equals(c));
        assertTrue(a.equals(c));
    }

    @Test
    void testEquality_Volume_ZeroValue() {
        assertTrue(new Quantity<>(0.0, VolumeUnit.LITRE).equals(new Quantity<>(0.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    void testEquality_NegativeVolume() {
        assertTrue(new Quantity<>(-1.0, VolumeUnit.LITRE).equals(new Quantity<>(-1000.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    void testEquality_LargeVolumeValue() {
        assertTrue(new Quantity<>(1000000.0, VolumeUnit.MILLILITRE).equals(new Quantity<>(1000.0, VolumeUnit.LITRE)));
    }

    @Test
    void testEquality_SmallVolumeValue() {
        assertTrue(new Quantity<>(0.001, VolumeUnit.LITRE).equals(new Quantity<>(1.0, VolumeUnit.MILLILITRE)));
    }

    // ─────────────────────────────────────────────
    // UC11: Volume conversion tests
    // ─────────────────────────────────────────────

    @Test
    void testConversion_LitreToMillilitre() {
        Quantity<VolumeUnit> result = new Quantity<>(1.0, VolumeUnit.LITRE).convertTo(VolumeUnit.MILLILITRE);
        assertTrue(result.equals(new Quantity<>(1000.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    void testConversion_MillilitreToLitre() {
        Quantity<VolumeUnit> result = new Quantity<>(1000.0, VolumeUnit.MILLILITRE).convertTo(VolumeUnit.LITRE);
        assertTrue(result.equals(new Quantity<>(1.0, VolumeUnit.LITRE)));
    }

    @Test
    void testConversion_GallonToLitre() {
        Quantity<VolumeUnit> result = new Quantity<>(1.0, VolumeUnit.GALLON).convertTo(VolumeUnit.LITRE);
        assertTrue(result.equals(new Quantity<>(3.78541, VolumeUnit.LITRE)));
    }

    @Test
    void testConversion_LitreToGallon() {
        Quantity<VolumeUnit> result = new Quantity<>(3.78541, VolumeUnit.LITRE).convertTo(VolumeUnit.GALLON);
        assertTrue(result.equals(new Quantity<>(1.0, VolumeUnit.GALLON)));
    }

    @Test
    void testConversion_MillilitreToGallon() {
        Quantity<VolumeUnit> result = new Quantity<>(1000.0, VolumeUnit.MILLILITRE).convertTo(VolumeUnit.GALLON);
        assertTrue(result.equals(new Quantity<>(1.0 / 3.78541, VolumeUnit.GALLON)));
    }

    @Test
    void testConversion_Volume_SameUnit() {
        Quantity<VolumeUnit> result = new Quantity<>(5.0, VolumeUnit.LITRE).convertTo(VolumeUnit.LITRE);
        assertTrue(result.equals(new Quantity<>(5.0, VolumeUnit.LITRE)));
    }

    @Test
    void testConversion_Volume_ZeroValue() {
        Quantity<VolumeUnit> result = new Quantity<>(0.0, VolumeUnit.LITRE).convertTo(VolumeUnit.MILLILITRE);
        assertTrue(result.equals(new Quantity<>(0.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    void testConversion_Volume_NegativeValue() {
        Quantity<VolumeUnit> result = new Quantity<>(-1.0, VolumeUnit.LITRE).convertTo(VolumeUnit.MILLILITRE);
        assertTrue(result.equals(new Quantity<>(-1000.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    void testConversion_Volume_RoundTrip() {
        Quantity<VolumeUnit> q = new Quantity<>(1.5, VolumeUnit.LITRE);
        Quantity<VolumeUnit> roundTrip = q.convertTo(VolumeUnit.MILLILITRE).convertTo(VolumeUnit.LITRE);
        assertTrue(q.equals(roundTrip));
    }

    // ─────────────────────────────────────────────
    // UC11: Volume addition tests
    // ─────────────────────────────────────────────

    @Test
    void testAddition_SameUnit_LitrePlusLitre() {
        Quantity<VolumeUnit> q1 = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> q2 = new Quantity<>(2.0, VolumeUnit.LITRE);
        assertTrue(q1.add(q2).equals(new Quantity<>(3.0, VolumeUnit.LITRE)));
    }

    @Test
    void testAddition_SameUnit_MillilitrePlusMillilitre() {
        Quantity<VolumeUnit> q1 = new Quantity<>(500.0, VolumeUnit.MILLILITRE);
        Quantity<VolumeUnit> q2 = new Quantity<>(500.0, VolumeUnit.MILLILITRE);
        assertTrue(q1.add(q2).equals(new Quantity<>(1000.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    void testAddition_CrossUnit_LitrePlusMillilitre() {
        Quantity<VolumeUnit> q1 = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> q2 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        assertTrue(q1.add(q2).equals(new Quantity<>(2.0, VolumeUnit.LITRE)));
    }

    @Test
    void testAddition_CrossUnit_MillilitrePlusLitre() {
        Quantity<VolumeUnit> q1 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        Quantity<VolumeUnit> q2 = new Quantity<>(1.0, VolumeUnit.LITRE);
        assertTrue(q1.add(q2).equals(new Quantity<>(2000.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    void testAddition_CrossUnit_GallonPlusLitre() {
        Quantity<VolumeUnit> q1 = new Quantity<>(1.0, VolumeUnit.GALLON);
        Quantity<VolumeUnit> q2 = new Quantity<>(3.78541, VolumeUnit.LITRE);
        assertTrue(q1.add(q2).equals(new Quantity<>(2.0, VolumeUnit.GALLON)));
    }

    @Test
    void testAddition_ExplicitTargetUnit_Litre() {
        Quantity<VolumeUnit> q1 = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> q2 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        assertTrue(Quantity.add(q1, q2, VolumeUnit.LITRE).equals(new Quantity<>(2.0, VolumeUnit.LITRE)));
    }

    @Test
    void testAddition_ExplicitTargetUnit_Millilitre() {
        Quantity<VolumeUnit> q1 = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> q2 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        assertTrue(Quantity.add(q1, q2, VolumeUnit.MILLILITRE).equals(new Quantity<>(2000.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    void testAddition_ExplicitTargetUnit_Gallon() {
        Quantity<VolumeUnit> q1 = new Quantity<>(3.78541, VolumeUnit.LITRE);
        Quantity<VolumeUnit> q2 = new Quantity<>(3.78541, VolumeUnit.LITRE);
        assertTrue(Quantity.add(q1, q2, VolumeUnit.GALLON).equals(new Quantity<>(2.0, VolumeUnit.GALLON)));
    }

    @Test
    void testAddition_Volume_Commutativity() {
        Quantity<VolumeUnit> q1 = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> q2 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        Quantity<VolumeUnit> result1 = Quantity.add(q1, q2, VolumeUnit.MILLILITRE);
        Quantity<VolumeUnit> result2 = Quantity.add(q2, q1, VolumeUnit.MILLILITRE);
        assertTrue(result1.equals(result2));
    }

    @Test
    void testAddition_Volume_WithZero() {
        Quantity<VolumeUnit> q1 = new Quantity<>(5.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> q2 = new Quantity<>(0.0, VolumeUnit.MILLILITRE);
        assertTrue(q1.add(q2).equals(new Quantity<>(5.0, VolumeUnit.LITRE)));
    }

    @Test
    void testAddition_Volume_NegativeValues() {
        Quantity<VolumeUnit> q1 = new Quantity<>(5.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> q2 = new Quantity<>(-2000.0, VolumeUnit.MILLILITRE);
        assertTrue(q1.add(q2).equals(new Quantity<>(3.0, VolumeUnit.LITRE)));
    }

    @Test
    void testAddition_Volume_LargeValues() {
        Quantity<VolumeUnit> q1 = new Quantity<>(1e6, VolumeUnit.LITRE);
        Quantity<VolumeUnit> q2 = new Quantity<>(1e6, VolumeUnit.LITRE);
        assertTrue(q1.add(q2).equals(new Quantity<>(2e6, VolumeUnit.LITRE)));
    }

    @Test
    void testAddition_Volume_SmallValues() {
        Quantity<VolumeUnit> q1 = new Quantity<>(0.001, VolumeUnit.LITRE);
        Quantity<VolumeUnit> q2 = new Quantity<>(0.002, VolumeUnit.LITRE);
        assertTrue(q1.add(q2).equals(new Quantity<>(0.003, VolumeUnit.LITRE)));
    }

    // ─────────────────────────────────────────────
    // UC11: Scalability test validation
    // ─────────────────────────────────────────────

    @Test
    void testGenericQuantity_VolumeOperations_Consistency() {
        // Just verify no exceptions are thrown when using generic methods with VolumeUnit
        Quantity<VolumeUnit> q = new Quantity<>(1.0, VolumeUnit.LITRE);
        assertNotNull(q.toString());
        assertTrue(q.toString().contains("1.0"));
        assertTrue(q.toString().contains("LITRE"));
    }

    @Test
    void testScalability_VolumeIntegration() {
        // Assert that the three independent enums implement IMeasurable
        assertTrue(IMeasurable.class.isAssignableFrom(LengthUnit.class));
        assertTrue(IMeasurable.class.isAssignableFrom(WeightUnit.class));
        assertTrue(IMeasurable.class.isAssignableFrom(VolumeUnit.class));
    }
}
