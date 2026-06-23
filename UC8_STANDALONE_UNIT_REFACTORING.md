# UC8: Refactoring Unit Enum to Standalone with Conversion Responsibility

## Description

UC8 refactors the design from UC1–UC7 to overcome the disadvantage of embedding conversion logic within `QuantityLength`. This refactoring extracts `LengthUnit` as a **standalone, top-level enum** and assigns it the full responsibility of managing conversions to and from the base unit (FEET). `QuantityLength` is simplified to delegate all conversion logic to the unit itself, improving cohesion, reducing coupling, and establishing a scalable pattern for additional measurement categories.

## Key Architectural Changes

### LengthUnit.java (Standalone Enum)

| Change | Details |
|---|---|
| **Base unit changed** | INCH → FEET (factor = 1.0) |
| **New conversion factors** | FEET(1.0), INCH(1/12), YARD(3.0), CENTIMETER(1/30.48) |
| **`convertToBaseUnit(double value)`** | Converts a value in this unit to FEET |
| **`convertFromBaseUnit(double baseValue)`** | Converts a FEET value to this unit |
| **`getConversionFactor()`** | Returns the factor relative to FEET |

### QuantityLength.java (Simplified)

| Change | Details |
|---|---|
| **Removed** `convertValue()` | Private helper eliminated |
| **Removed** `toBaseUnit()` | Private helper eliminated |
| **Delegation** | All conversion calls now use `unit.convertToBaseUnit()` / `unit.convertFromBaseUnit()` |
| **Focus** | Pure value comparison and arithmetic — no conversion math |

## Design Principles Applied

| Principle | How It's Applied |
|---|---|
| **Single Responsibility (SRP)** | `LengthUnit` handles conversions; `QuantityLength` handles comparison/arithmetic |
| **Separation of Concerns** | Unit-specific logic isolated in the enum |
| **Dependency Inversion** | `QuantityLength` depends on the abstraction (`LengthUnit` API), not concrete implementation |
| **Delegation Pattern** | `QuantityLength` delegates `unit.convertToBaseUnit()` instead of inline math |
| **Encapsulation** | Conversion formulas hidden within `LengthUnit` methods |
| **Circular Dependency Elimination** | No nested enums; future `WeightUnit`, `VolumeUnit` etc. coexist cleanly |

## Conversion Examples

```
LengthUnit.FEET.convertToBaseUnit(12.0)    → 12.0   (already base)
LengthUnit.INCH.convertToBaseUnit(12.0)    → 1.0    (12 inches = 1 foot)
LengthUnit.YARD.convertToBaseUnit(1.0)     → 3.0    (1 yard = 3 feet)
LengthUnit.CENTIMETER.convertToBaseUnit(30.48) → 1.0 (30.48 cm = 1 foot)

LengthUnit.FEET.convertFromBaseUnit(2.0)   → 2.0
LengthUnit.INCH.convertFromBaseUnit(1.0)   → 12.0
LengthUnit.YARD.convertFromBaseUnit(3.0)   → 1.0
LengthUnit.CENTIMETER.convertFromBaseUnit(1.0) → 30.48
```

## Backward Compatibility

All **82 tests** pass (57 from UC1–UC7 + 25 new UC8 tests). No changes to the public API — client code works without modification.

## UC8-Specific Test Cases

| Test | Description |
|---|---|
| `testLengthUnitEnum_FeetConstant` | FEET factor = 1.0 |
| `testLengthUnitEnum_InchesConstant` | INCH factor = 1/12 |
| `testLengthUnitEnum_YardsConstant` | YARD factor = 3.0 |
| `testLengthUnitEnum_CentimetersConstant` | CM factor = 1/30.48 |
| `testConvertToBaseUnit_FeetToFeet` | 5.0 ft → 5.0 ft |
| `testConvertToBaseUnit_InchesToFeet` | 12.0 in → 1.0 ft |
| `testConvertToBaseUnit_YardsToFeet` | 1.0 yd → 3.0 ft |
| `testConvertToBaseUnit_CentimetersToFeet` | 30.48 cm → 1.0 ft |
| `testConvertFromBaseUnit_FeetToFeet` | 2.0 ft → 2.0 ft |
| `testConvertFromBaseUnit_FeetToInches` | 1.0 ft → 12.0 in |
| `testConvertFromBaseUnit_FeetToYards` | 3.0 ft → 1.0 yd |
| `testConvertFromBaseUnit_FeetToCentimeters` | 1.0 ft → 30.48 cm |
| `testQuantityLengthRefactored_Equality` | Delegation-based equality |
| `testQuantityLengthRefactored_ConvertTo` | Delegation-based conversion |
| `testQuantityLengthRefactored_Add` | Delegation-based addition |
| `testQuantityLengthRefactored_AddWithTargetUnit` | Addition with target unit |
| `testQuantityLengthRefactored_NullUnit` | Null unit validation |
| `testQuantityLengthRefactored_InvalidValue` | NaN validation |
| `testBackwardCompatibility_UC1EqualityTests` | UC1 equality preserved |
| `testBackwardCompatibility_UC5ConversionTests` | UC5 conversions preserved |
| `testBackwardCompatibility_UC6AdditionTests` | UC6 addition preserved |
| `testBackwardCompatibility_UC7AdditionWithTargetUnitTests` | UC7 target-unit addition preserved |
| `testRoundTripConversion_RefactoredDesign` | Round-trip precision |
| `testUnitImmutability` | Enum immutability verified |
| `testArchitecturalScalability_MultipleCategories` | Standalone top-level enum verified |

## Scalability Pattern

The refactored design serves as a template for future measurement categories:

```
LengthUnit     → QuantityLength
WeightUnit     → QuantityWeight
VolumeUnit     → QuantityVolume
TemperatureUnit → QuantityTemperature
```

Each unit enum is standalone with `convertToBaseUnit()` / `convertFromBaseUnit()`. No circular dependencies between categories.
