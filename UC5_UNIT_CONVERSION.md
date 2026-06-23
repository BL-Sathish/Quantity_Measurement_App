# UC5: Unit-to-Unit Conversion (Same Measurement Type)

## Description

UC5 extends UC4 by providing **explicit conversion operations** between length units. Instead of only checking equality, the `QuantityLength` API now exposes:

- A **static `convert()` method** for raw numeric conversion
- An **instance `convertTo()` method** that returns a new `QuantityLength` object in the target unit

Input validation is also strengthened to reject `NaN`, infinite, and `null` values. This use case also demonstrates **method overloading** in `QuantityMeasurementApp` via two variants of `demonstrateLengthConversion()`.

---

## Preconditions

- `QuantityLength` and `LengthUnit` from UC3/UC4 exist with FEET, INCH, YARD, CENTIMETER.
- Conversion factors for each `LengthUnit` are defined relative to INCH as the base unit.
- Input: a numeric value, a valid source `LengthUnit`, and a valid target `LengthUnit`.

---

## Main Flow

1. Client calls `QuantityLength.convert(value, sourceUnit, targetUnit)` (static) or `instance.convertTo(targetUnit)`.
2. The method validates:
   - `value` is a finite number (`Double.isFinite()`).
   - `sourceUnit` and `targetUnit` are non-null.
3. The value is converted to the base unit (INCH) using `sourceUnit.conversionFactor`.
4. The base value is divided by `targetUnit.conversionFactor` to get the result.
5. The converted numeric result is returned (or wrapped in a new `QuantityLength` for `convertTo()`).

---

## Postconditions

- A numeric value representing the original measurement in the target unit is returned.
- Invalid inputs (null unit, NaN, Infinite) throw `IllegalArgumentException`.
- Conversion preserves mathematical equivalence within floating-point precision.
- All UC1–UC4 tests continue to pass (backward compatibility).

---

## Conversion Formula

```
result = value × (sourceUnit.factor / targetUnit.factor)
```

Where `INCH = 1.0` is the base. Examples:

```
convert(1.0, FEET, INCH)   = 1.0 × (12.0 / 1.0)  = 12.0  ✅
convert(3.0, YARD, FEET)   = 3.0 × (36.0 / 12.0) = 9.0   ✅
convert(36.0, INCH, YARD)  = 36.0 × (1.0 / 36.0)  = 1.0   ✅
convert(1.0, CM, INCH)     = 1.0 × (0.393701/1.0) ≈ 0.3937 ✅
```

---

## New / Updated Class Design

### `QuantityLength` (updated)

```
QuantityLength
├── QuantityLength(double value, LengthUnit unit)   ← validates NaN, Infinite, null
├── static convert(double, LengthUnit, LengthUnit)  ← static API
├── convertTo(LengthUnit targetUnit)                ← instance API, returns new object
├── equals(Object)                                  ← cross-unit equality (unchanged)
├── hashCode()
└── toString()                                      ← "QuantityLength{1.0 FEET}"
```

### `QuantityMeasurementApp` (updated — method overloading)

```
QuantityMeasurementApp
├── demonstrateLengthConversion(double, LengthUnit, LengthUnit) ← Method 1
├── demonstrateLengthConversion(QuantityLength, LengthUnit)     ← Method 2 (overloaded)
├── demonstrateLengthEquality(QuantityLength, QuantityLength)
└── demonstrateLengthComparison(double, LengthUnit, double, LengthUnit)
```

---

## Example Output

```
=== UC5: Unit Conversion Demo ===
Input: convert(1.0000, FEET, INCH) → Output: 12.0000
Input: convert(3.0000, YARD, FEET) → Output: 9.0000
Input: convert(36.0000, INCH, YARD) → Output: 1.0000
Input: convert(1.0000, CENTIMETER, INCH) → Output: 0.3937
Input: convert(0.0000, FEET, INCH) → Output: 0.0000

=== Overloaded demonstrateLengthConversion ===
Input: QuantityLength{1.0 YARD}.convertTo(INCH) → Output: QuantityLength{36.0 INCH}

=== Equality Demo ===
Equality: QuantityLength{1.0 FEET} == QuantityLength{12.0 INCH} → true
Equality: QuantityLength{1.0 YARD} == QuantityLength{3.0 FEET} → true
```

---

## Key Concepts Learned

| Concept | Description |
|---------|-------------|
| **Enum with conversion factors** | Type-safe constants with embedded data; easy to extend |
| **Immutability** | `convertTo()` returns a new instance; original is unchanged |
| **Value Object Semantics** | `QuantityLength` instances represent immutable values |
| **Method Overloading** | Two `demonstrateLengthConversion()` variants with different signatures |
| **Private Helper Methods** | `convertValue()` and `toBaseUnit()` — encapsulated conversion logic |
| **Method Overriding** | `equals()` and `toString()` overridden from `Object` |
| **Input Validation** | NaN / Infinite / null rejected with `IllegalArgumentException` |
| **JavaDoc Documentation** | All public methods fully documented |

---

## Test Cases

| Test Method | Description | Expected |
|-------------|-------------|----------|
| `testConversion_FeetToInches()` | `convert(1.0, FEET, INCH)` | `12.0` |
| `testConversion_InchesToFeet()` | `convert(24.0, INCH, FEET)` | `2.0` |
| `testConversion_YardsToInches()` | `convert(1.0, YARD, INCH)` | `36.0` |
| `testConversion_InchesToYards()` | `convert(72.0, INCH, YARD)` | `2.0` |
| `testConversion_FeetToYards()` | `convert(6.0, FEET, YARD)` | `2.0` |
| `testConversion_YardsToFeet()` | `convert(3.0, YARD, FEET)` | `9.0` |
| `testConversion_CentimetersToInches()` | `convert(2.54, CM, INCH)` | `≈1.0` |
| `testConversion_ZeroValue()` | `convert(0.0, FEET, INCH)` | `0.0` |
| `testConversion_NegativeValue()` | `convert(-1.0, FEET, INCH)` | `-12.0` |
| `testConversion_SameUnit()` | `convert(5.0, FEET, FEET)` | `5.0` |
| `testConversion_LargeValue()` | `convert(1000.0, YARD, INCH)` | `36000.0` |
| `testConversion_NullSourceUnit_ThrowsException()` | `convert(1.0, null, INCH)` | throws |
| `testConversion_NullTargetUnit_ThrowsException()` | `convert(1.0, FEET, null)` | throws |
| `testConversion_NaNValue_ThrowsException()` | `convert(NaN, FEET, INCH)` | throws |
| `testConversion_InfiniteValue_ThrowsException()` | `convert(∞, FEET, INCH)` | throws |
| `testConvertTo_FeetToInches_ReturnsNewInstance()` | Immutability check | new object |
| `testConvertTo_NullTarget_ThrowsException()` | `feet.convertTo(null)` | throws |
| `testConversion_RoundTrip_FeetToInchesAndBack()` | `ft → in → ft ≈ original` | within ε |
| `testConversion_RoundTrip_YardsToFeetToInchesAndBack()` | 3-step round trip | within ε |
| `testToString_ContainsValueAndUnit()` | `toString()` check | contains value & unit |
| `testConstructor_NaNValue_ThrowsException()` | `new QuantityLength(NaN, FEET)` | throws |
| `testConstructor_PositiveInfinity_ThrowsException()` | `new QuantityLength(∞, FEET)` | throws |

---

## Precision

All conversion results are compared using `EPSILON = 1e-6` (or `1e-4` for cm conversions) to handle floating-point rounding.

---

## Branch Info

- **Branch:** `feature/uc5-unit-conversion`
- **Merged into:** `dev`
