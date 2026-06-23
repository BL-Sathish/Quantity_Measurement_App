# UC4: Extended Unit Support — Yards and Centimeters

## Description

UC4 extends UC3 by introducing **Yards** and **Centimeters** as additional length units into the `LengthUnit` enum. This use case demonstrates how the generic `QuantityLength` class design **scales effortlessly** to accommodate new units without any code duplication. Only the enum needs to be updated — the `QuantityLength` class itself requires **zero changes**.

---

## Preconditions

- `QuantityLength` and `LengthUnit` from UC3 are available.
- Conversion factors for the new units are defined:
  - `1 Yard = 3 Feet = 36 Inches`
  - `1 Centimeter = 0.393701 Inches`
- INCH remains the common base unit for all conversions.

---

## Main Flow

1. Users input two numerical values with their respective unit types (FEET, INCH, YARD, or CENTIMETER).
2. `QuantityLength` validates the unit (non-null via constructor).
3. Both values are normalised to the base unit (INCH) using the enum's conversion factor.
4. The normalised values are compared for equality.
5. The result is returned to the user.

---

## Postconditions

- All previous UC1, UC2, and UC3 functionality is preserved.
- Yard-to-yard, yard-to-feet, yard-to-inches comparisons are fully supported.
- Centimeter-to-centimeter, cm-to-inch, cm-to-feet comparisons are fully supported.
- Code remains free of duplication — adding new units requires only enum modification.

---

## Updated LengthUnit Enum

```java
public enum LengthUnit {
    FEET(12.0),           // 1 foot = 12 inches
    INCH(1.0),            // base unit
    YARD(36.0),           // 1 yard = 36 inches
    CENTIMETER(0.393701); // 1 cm ≈ 0.393701 inches
}
```

---

## Conversion Reference Table

| Unit | Inches | Feet | Yards |
|------|--------|------|-------|
| 1 Foot | 12 | 1 | 0.333 |
| 1 Inch | 1 | 0.0833 | 0.0278 |
| 1 Yard | 36 | 3 | 1 |
| 1 Centimeter | 0.393701 | 0.0328 | 0.0109 |

---

## Example Output

```
Input: Quantity(1.0, YARDS) and Quantity(3.0, FEET)
Output: Equal (true)

Input: Quantity(1.0, YARDS) and Quantity(36.0, INCHES)
Output: Equal (true)

Input: Quantity(2.0, YARDS) and Quantity(2.0, YARDS)
Output: Equal (true)

Input: Quantity(2.0, CENTIMETERS) and Quantity(2.0, CENTIMETERS)
Output: Equal (true)

Input: Quantity(1.0, CENTIMETERS) and Quantity(0.393701, INCHES)
Output: Equal (true)
```

---

## Key Concepts Learned

| Concept | Description |
|---------|-------------|
| **Scalability of Generic Design** | New units only require adding an enum constant — no new class |
| **Enum Extensibility** | Enums can grow without breaking existing code |
| **Conversion Factor Management** | Centralized definition ensures consistency across all comparisons |
| **Unit Relationships** | Understanding how all units relate to a common base unit |
| **Mathematical Accuracy** | Cross-unit comparisons depend on precise conversion factors |
| **DRY Validation** | Proven: adding Yards/CM required zero changes to `QuantityLength` |
| **Backward Compatibility** | All UC1, UC2, UC3 test cases continue passing unchanged |

---

## Test Cases

| Test Method | Description | Expected |
|-------------|-------------|----------|
| `testEquality_YardToYard_SameValue()` | `Qty(1.0, YARD) == Qty(1.0, YARD)` | `true` |
| `testEquality_YardToYard_DifferentValue()` | `Qty(1.0, YARD) != Qty(2.0, YARD)` | `false` |
| `testEquality_YardToFeet_EquivalentValue()` | `Qty(1.0, YARD) == Qty(3.0, FEET)` | `true` |
| `testEquality_FeetToYard_EquivalentValue()` | `Qty(3.0, FEET) == Qty(1.0, YARD)` | `true` |
| `testEquality_YardToInches_EquivalentValue()` | `Qty(1.0, YARD) == Qty(36.0, INCH)` | `true` |
| `testEquality_InchesToYard_EquivalentValue()` | `Qty(36.0, INCH) == Qty(1.0, YARD)` | `true` |
| `testEquality_YardToFeet_NonEquivalentValue()` | `Qty(1.0, YARD) != Qty(2.0, FEET)` | `false` |
| `testEquality_CentimeterToCentimeter_SameValue()` | `Qty(2.0, CM) == Qty(2.0, CM)` | `true` |
| `testEquality_CentimetersToInches_EquivalentValue()` | `Qty(1.0, CM) == Qty(0.393701, INCH)` | `true` |
| `testEquality_CentimetersToFeet_NonEquivalentValue()` | `Qty(1.0, CM) != Qty(1.0, FEET)` | `false` |
| `testEquality_MultiUnit_TransitiveProperty()` | Yard == Feet == Inches (transitive) | `true` |
| `testEquality_AllUnits_ComplexScenario()` | `2 YARD == 6 FEET == 72 INCH` | `true` |

---

## Branch Info

- **Branch:** `feature/uc4-extended-unit-support`
- **Merged into:** `dev`
