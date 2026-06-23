# UC6: Addition of Two Length Units

## Description

UC6 extends UC5 by introducing **addition operations** between length measurements. This use case enables the `QuantityLength` API to add two lengths of potentially different units (but same category—length) and return the result in the unit of the first operand, or a specified target unit. 

For example, adding `1 foot` and `12 inches` yields `2 feet` (based on the unit of the first operand).

---

## Preconditions

- `QuantityLength` and `LengthUnit` (from UC3/UC4/UC5) exist with FEET, INCHES, YARDS, CENTIMETERS.
- The conversion factor for each `LengthUnit` is defined relative to a consistent base unit (INCH).
- Two `QuantityLength` objects or raw values with their respective units are provided.

---

## Main Flow

1. Client calls `QuantityLength.add(length1, length2)` (static) or uses an instance method `length1.add(length2)`.
2. The method validates:
   - Both `length1` and `length2` are non-null.
3. Both lengths are converted to a common base unit (INCH).
4. The converted values are added together.
5. The sum is converted from the base unit to the target unit (e.g., the unit of the first operand).
6. A new `QuantityLength` object representing the result is returned.

---

## Postconditions

- A new `QuantityLength` object is returned with the sum of the two measurements.
- The original `QuantityLength` objects remain unchanged (immutability).
- Invalid inputs result in `IllegalArgumentException`.
- Addition is mathematically accurate within floating-point precision limits.
- Addition respects the commutative property.

---

## New Methods added to QuantityLength

```java
// Instance method
public QuantityLength add(QuantityLength other)

// Static method
public static QuantityLength add(QuantityLength q1, QuantityLength q2)
public static QuantityLength add(QuantityLength q1, QuantityLength q2, LengthUnit targetUnit)
public static QuantityLength add(double val1, LengthUnit unit1, double val2, LengthUnit unit2, LengthUnit targetUnit)
```

---

## Precision and Equals Modification

Floating-point additions can introduce rounding errors (e.g. adding centimeters and inches). The `equals` and `hashCode` methods of `QuantityLength` were updated to use an `EPSILON = 1e-6` when comparing the normalized base unit values.

---

## Example Output

```
=== UC6: Addition Demo ===
Input: add(QuantityLength{1.0 FEET}, QuantityLength{2.0 FEET})
Output: QuantityLength{3.0 FEET}

Input: add(QuantityLength{1.0 FEET}, QuantityLength{12.0 INCH})
Output: QuantityLength{2.0 FEET}

Input: add(QuantityLength{12.0 INCH}, QuantityLength{1.0 FEET})
Output: QuantityLength{24.0 INCH}

Input: add(QuantityLength{1.0 YARD}, QuantityLength{3.0 FEET})
Output: QuantityLength{2.0 YARD}

Input: add(QuantityLength{36.0 INCH}, QuantityLength{1.0 YARD})
Output: QuantityLength{72.0 INCH}

Input: add(QuantityLength{2.54 CENTIMETER}, QuantityLength{1.0 INCH})
Output: QuantityLength{5.08 CENTIMETER}

Input: add(QuantityLength{5.0 FEET}, QuantityLength{0.0 INCH})
Output: QuantityLength{5.0 FEET}

Input: add(QuantityLength{5.0 FEET}, QuantityLength{-2.0 FEET})
Output: QuantityLength{3.0 FEET}
```

---

## Key Concepts Learned

| Concept | Description |
|---------|-------------|
| **Arithmetic on Value Objects** | Encapsulating domain operations beyond simple comparison. |
| **Immutability** | Operations return new instances instead of mutating state. |
| **Normalization to Base Unit** | Simplifying cross-unit arithmetic by converting to a common base. |
| **Precision and Rounding** | Using an epsilon-based `equals` to handle floating-point anomalies. |
| **Method Overloading** | Providing flexibility through multiple parameter combinations for `add`. |

---

## Test Cases

| Test Method | Description | Expected |
|-------------|-------------|----------|
| `testAddition_SameUnit_FeetPlusFeet` | `1.0 FT + 2.0 FT` | `3.0 FT` |
| `testAddition_SameUnit_InchPlusInch` | `6.0 IN + 6.0 IN` | `12.0 IN` |
| `testAddition_CrossUnit_FeetPlusInches` | `1.0 FT + 12.0 IN` | `2.0 FT` |
| `testAddition_CrossUnit_InchPlusFeet` | `12.0 IN + 1.0 FT` | `24.0 IN` |
| `testAddition_CrossUnit_YardPlusFeet` | `1.0 YD + 3.0 FT` | `2.0 YD` |
| `testAddition_CrossUnit_CentimeterPlusInch`| `2.54 CM + 1.0 IN` | `5.08 CM` |
| `testAddition_Commutativity` | `A + B == B + A` | `true` |
| `testAddition_WithZero` | `5.0 FT + 0.0 IN` | `5.0 FT` |
| `testAddition_NegativeValues` | `5.0 FT + -2.0 FT` | `3.0 FT` |
| `testAddition_NullSecondOperand` | `1.0 FT + null` | throws Exception |
| `testAddition_LargeValues` | `1e6 FT + 1e6 FT` | `2e6 FT` |
| `testAddition_SmallValues` | `0.001 FT + 0.002 FT` | `0.003 FT` |

---

## Branch Info

- **Branch:** `feature/uc6-length-addition`
- **Merged into:** `dev`
