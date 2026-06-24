# Quantity Measurement App - UC1 to UC11

This branch implements **UC1 to UC11**.

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