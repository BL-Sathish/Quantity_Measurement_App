# UC2: Feet and Inches Measurement Equality

## Description

UC2 extends UC1 by adding **equality comparison for Inches** alongside Feet. Both units are treated independently — there is no cross-unit comparison here. This use case introduces the `Inches` inner class (mirroring the `Feet` class) and refactors the `main` method to delegate equality checks into separate static methods (`compareFeet` and `compareInches`), reducing dependency on the `main` method.

---

## Preconditions

- The `QuantityMeasurementApp` class is instantiated.
- Two numerical values in **feet** and two in **inches** are hard-coded for comparison.
- The `Feet` class from UC1 is already implemented.

---

## Main Flow

1. `main` calls `compareFeet(val1, val2)` — internally creates two `Feet` objects and calls `equals()`.
2. `main` calls `compareInches(val1, val2)` — internally creates two `Inches` objects and calls `equals()`.
3. Both classes validate that input values are numeric.
4. Both classes compare the two values for equality using `Double.compare()`.
5. The result (`true` / `false`) is returned and printed.

---

## Postconditions

- Equality result is returned for both feet-to-feet and inch-to-inch comparisons.
- Logic is separated from `main` via dedicated static methods.
- All UC1 test cases continue to pass (backward compatibility).

---

## Example Output

```
Input: 1.0 inch and 1.0 inch
Output: Equal (true)

Input: 1.0 ft and 1.0 ft
Output: Equal (true)
```

---

## Class Design

```
QuantityMeasurementApp
├── static compareFeet(double, double): boolean
├── static compareInches(double, double): boolean
├── static inner class: Feet
│       ├── double value
│       ├── equals(Object)
│       └── hashCode()
└── static inner class: Inches
        ├── double value
        ├── equals(Object)
        └── hashCode()
```

---

## Key Concepts Learned

| Concept | Description |
|---------|-------------|
| **Object Equality** | Same `equals()` contract applied to a second unit type |
| **Floating-point Comparison** | `Double.compare()` used for precision-safe equality |
| **Null Checking** | `equals(null)` returns `false` for both classes |
| **Type Checking** | `getClass()` prevents incorrect type comparisons |
| **DRY Violation (Identified)** | Same code duplicated across `Feet` and `Inches` — addressed in UC3 |

---

## Disadvantage Identified

> Both `Feet` and `Inches` contain **nearly identical code** — same constructor, same `equals()`, same `hashCode()`. This violates the **DRY (Don't Repeat Yourself)** principle.  
> Any change must be applied to both classes, increasing maintenance risk. This is resolved in **UC3** using a generic `QuantityLength` class.

---

## Test Cases

### Feet Tests

| Test Method | Description | Expected |
|-------------|-------------|----------|
| `testEquality_SameValue()` | `Feet(1.0) == Feet(1.0)` | `true` |
| `testEquality_DifferentValue()` | `Feet(1.0) != Feet(2.0)` | `false` |
| `testEquality_NullComparison()` | `Feet(1.0).equals(null)` | `false` |
| `testEquality_NonNumericInput()` | `Feet(1.0).equals("1.0 ft")` | `false` |
| `testEquality_SameReference()` | `feet1.equals(feet1)` | `true` |

### Inches Tests

| Test Method | Description | Expected |
|-------------|-------------|----------|
| `testInchesEquality_SameValue()` | `Inches(1.0) == Inches(1.0)` | `true` |
| `testInchesEquality_DifferentValue()` | `Inches(1.0) != Inches(2.0)` | `false` |
| `testInchesEquality_NullComparison()` | `Inches(1.0).equals(null)` | `false` |
| `testInchesEquality_NonNumericInput()` | `Inches(1.0).equals("1.0 inch")` | `false` |
| `testInchesEquality_SameReference()` | `inches1.equals(inches1)` | `true` |

---

## Branch Info

- **Branch:** `feature/uc2-inches-equality`
- **Merged into:** `dev`
