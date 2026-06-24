# Quantity Measurement App - UC1: Feet Measurement Equality

This branch implements **UC1**.

## Implementation Details
- **QuantityMeasurementApp**: Main class for the application.
- **Feet**: Inner class representing a length in feet. It uses a `private final double value` for immutability.
- **Equality Logic**: Overridden `equals` method safely checks type, null references, and uses `Double.compare()` for safe floating-point comparison.

## Tests
- `testEquality_SameValue`: Verifies 1.0 ft equals 1.0 ft.
- `testEquality_DifferentValue`: Verifies 1.0 ft does not equal 2.0 ft.
- `testEquality_NullComparison`: Verifies equality handles `null` securely.
- `testEquality_NonNumericInput`: Verifies equality handles different object types (e.g. `String`).
- `testEquality_SameReference`: Verifies reflective property of equality.

## UC12: Subtraction and Division Operations
- Implemented `subtract` (implicit and explicit target unit) and `divide` methods in `Quantity`.
- Added generic demonstration methods `demonstrateSubtraction` and `demonstrateDivision` in `QuantityMeasurementApp` with example calls in `main`.
- Updated test suite with subtraction and division test cases covering same units, cross‑unit, explicit target unit, negative/zero results, division by zero, and cross‑category safety.
- Preserved immutability, performed rounding to two decimal places for subtraction results, and ensured comprehensive validation and error handling.