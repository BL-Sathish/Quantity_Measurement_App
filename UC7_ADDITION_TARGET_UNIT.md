# UC7: Addition with Target Unit Specification

## Description

UC7 extends UC6 by providing flexibility in specifying the unit for the addition result. Instead of defaulting to the unit of the first operand, this use case allows the caller to explicitly specify any supported unit as the target unit for the result. This provides greater flexibility in use cases where the result must be expressed in a specific unit regardless of the operands' units.

## Key Changes

### QuantityLength.java

- **`add(QuantityLength q1, QuantityLength q2, LengthUnit targetUnit)`** — Static method that adds two lengths and returns the result in the specified `targetUnit`.
- **`add(QuantityLength q1, QuantityLength q2)`** — Static convenience overload that defaults to the first operand's unit.
- **`add(double val1, LengthUnit unit1, double val2, LengthUnit unit2, LengthUnit targetUnit)`** — Static raw-value overload for direct numeric inputs.

### Conversion Logic

All operands are normalised to the common base unit (INCH), summed, then converted to the specified target unit:

```java
double sumInBase = q1.toBaseUnit() + q2.toBaseUnit();
double sumInTargetUnit = sumInBase / targetUnit.getBaseUnitConversionFactor();
return new QuantityLength(sumInTargetUnit, targetUnit);
```

## Example Output

| Expression | Target Unit | Result |
|---|---|---|
| `add(1.0 FEET, 12.0 INCH, FEET)` | FEET | `2.0 FEET` |
| `add(1.0 FEET, 12.0 INCH, INCH)` | INCH | `24.0 INCH` |
| `add(1.0 FEET, 12.0 INCH, YARD)` | YARD | `≈0.667 YARD` |
| `add(1.0 YARD, 3.0 FEET, YARD)` | YARD | `2.0 YARD` |
| `add(36.0 INCH, 1.0 YARD, FEET)` | FEET | `6.0 FEET` |

## Concepts Learned

- **Method Overloading** — Multiple `add()` signatures provide flexible API entry points.
- **Static Factory Methods** — Static methods create new immutable instances without requiring the caller to manage internals.
- **Target-Unit Flexibility** — Decouples the result unit from the operand units.

## Test Cases (UC7-Specific)

| Test | Description |
|---|---|
| `testAddition_WithTargetUnit_FeetAndInches_ToYards` | 1 ft + 12 in → 0.667 yd |
| `testAddition_StaticRaw_WithTargetUnit_ToYards` | Raw-value static overload |
| `testAddition_WithTargetUnit_NullOperand` | Null validation |
