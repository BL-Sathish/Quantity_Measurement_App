# UC3: Generic Quantity Class for DRY Principle

## Description

UC3 **refactors** the duplicate `Feet` and `Inches` inner classes from UC1 and UC2 into a single, generic **`QuantityLength`** class. A `LengthUnit` enum encapsulates all supported units with their respective conversion factors (relative to INCH as the base unit). This eliminates code duplication, applies the **DRY (Don't Repeat Yourself)** principle, and enables cross-unit equality comparison (e.g., `1 foot == 12 inches`).

---

## Preconditions

- The `QuantityMeasurementApp` class from UC2 is available.
- The `QuantityLength` class and `LengthUnit` enum are to be created.
- Conversion factors between supported units are defined as constants.

---

## Main Flow

1. User provides two numerical values with their respective unit types.
2. `QuantityLength` validates the input values (non-null unit).
3. Both values are converted to a common base unit (INCH) using conversion factors.
4. The converted values are compared for equality using `Double.compare()`.
5. The result is returned to the user.

---

## Postconditions

- Equality result is returned based on comparison of converted values.
- All UC1 and UC2 functionality is preserved.
- Code duplication is eliminated — maintenance is simplified.

---

## New Files Introduced

| File | Purpose |
|------|---------|
| `LengthUnit.java` | Enum defining supported units and their INCH-based conversion factors |
| `QuantityLength.java` | Generic, immutable length class supporting cross-unit equality |

---

## LengthUnit Enum Design

```java
public enum LengthUnit {
    FEET(12.0),   // 1 foot = 12 inches
    INCH(1.0);    // base unit

    private final double baseUnitConversionFactor;
}
```

---

## QuantityLength Class Design

```
QuantityLength
├── double value         (immutable)
├── LengthUnit unit      (immutable)
├── equals(Object)       → converts both to base unit before comparing
└── hashCode()
```

---

## Example Output

```
Input: Quantity(1.0, "feet") and Quantity(12.0, "inches")
Output: Equal (true)

Input: Quantity(1.0, "inch") and Quantity(1.0, "inch")
Output: Equal (true)
```

---

## Key Concepts Learned

| Concept | Description |
|---------|-------------|
| **DRY Principle** | Single `QuantityLength` class replaces duplicate `Feet` and `Inches` classes |
| **Polymorphism** | One class handles multiple unit types via enum |
| **Enum Usage** | `LengthUnit` provides type-safe constants with embedded data |
| **Abstraction** | Conversion logic is hidden inside the class; callers just use `equals()` |
| **Encapsulation** | Value and unit are bundled together with controlled access |
| **Single Responsibility** | `QuantityLength` handles all length comparison logic |
| **Scalability** | Adding a new unit = one new enum constant, no new class needed |

---

## Conversion Formula

```
base_value = value × unit.conversionFactor
```

Where `INCH = 1.0` is the base. Two objects are equal when their base values are equal:

```
1.0 FEET → 1.0 × 12.0 = 12.0 inches
12.0 INCH → 12.0 × 1.0 = 12.0 inches
→ Equal ✅
```

---

## Test Cases

| Test Method | Description | Expected |
|-------------|-------------|----------|
| `testEquality_FeetToFeet_SameValue()` | `Qty(1.0, FEET) == Qty(1.0, FEET)` | `true` |
| `testEquality_InchToInch_SameValue()` | `Qty(1.0, INCH) == Qty(1.0, INCH)` | `true` |
| `testEquality_FeetToInch_EquivalentValue()` | `Qty(1.0, FEET) == Qty(12.0, INCH)` | `true` |
| `testEquality_InchToFeet_EquivalentValue()` | `Qty(12.0, INCH) == Qty(1.0, FEET)` | `true` |
| `testEquality_FeetToFeet_DifferentValue()` | `Qty(1.0, FEET) != Qty(2.0, FEET)` | `false` |
| `testEquality_InchToInch_DifferentValue()` | `Qty(1.0, INCH) != Qty(2.0, INCH)` | `false` |
| `testEquality_NullUnit()` | `new QuantityLength(1.0, null)` | throws `IllegalArgumentException` |
| `testEquality_SameReference()` | `qty.equals(qty)` | `true` |
| `testEquality_NullComparison()` | `qty.equals(null)` | `false` |
| `testEquality_NonNumericInput()` | `qty.equals("string")` | `false` |

---

## Branch Info

- **Branch:** `feature/uc3-generic-quantity`
- **Merged into:** `dev`
