package com.quantity.measurement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import static com.quantity.measurement.TemperatureUnit.*;

@SpringBootApplication
public class QuantityMeasurementApp {

    private static final IQuantityMeasurementRepository repository = QuantityMeasurementCacheRepository.getInstance();
    private static final IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(repository);
    private static final QuantityMeasurementController controller = new QuantityMeasurementController(service);

    public static void main(String[] args) {
        SpringApplication.run(QuantityMeasurementApp.class, args);

        // UC6: Addition demonstration
        System.out.println("\n=== UC6: Addition Demo ===");
        demonstrateAddition(1.0, LengthUnit.FEET, 2.0, LengthUnit.FEET);
        demonstrateAddition(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCH);
        demonstrateAddition(12.0, LengthUnit.INCH, 1.0, LengthUnit.FEET);
        demonstrateAddition(1.0, LengthUnit.YARD, 3.0, LengthUnit.FEET);
        demonstrateAddition(36.0, LengthUnit.INCH, 1.0, LengthUnit.YARD);
        demonstrateAddition(2.54, LengthUnit.CENTIMETER, 1.0, LengthUnit.INCH);
        demonstrateAddition(5.0, LengthUnit.FEET, 0.0, LengthUnit.INCH);
        demonstrateAddition(5.0, LengthUnit.FEET, -2.0, LengthUnit.FEET);

        // UC7: Addition with explicit target unit demonstration
        System.out.println("\n=== UC7: Addition with Target Unit Demo ===");
        demonstrateAddition(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCH, LengthUnit.FEET);
        demonstrateAddition(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCH, LengthUnit.INCH);
        demonstrateAddition(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCH, LengthUnit.YARD);

        // UC8: Refactored LengthUnit conversion demo
        System.out.println("\n=== UC8: Standalone LengthUnit Conversion Demo ===");
        demonstrateUnitConversion(LengthUnit.FEET, 12.0);
        demonstrateUnitConversion(LengthUnit.INCH, 12.0);
        demonstrateUnitConversion(LengthUnit.YARD, 1.0);
        demonstrateUnitConversion(LengthUnit.CENTIMETER, 30.48);

        // ─────────────────────────────────────────────
        // UC9: Weight Measurement Demonstrations
        // ─────────────────────────────────────────────

        System.out.println("\n=== UC9: Weight Equality Demo ===");
        demonstrateWeightEquality(1.0, WeightUnit.KILOGRAM, 1.0, WeightUnit.KILOGRAM);
        demonstrateWeightEquality(1.0, WeightUnit.KILOGRAM, 1000.0, WeightUnit.GRAM);
        demonstrateWeightEquality(2.0, WeightUnit.POUND, 2.0, WeightUnit.POUND);
        demonstrateWeightEquality(1.0, WeightUnit.KILOGRAM, 2.20462, WeightUnit.POUND);
        demonstrateWeightEquality(500.0, WeightUnit.GRAM, 0.5, WeightUnit.KILOGRAM);
        demonstrateWeightEquality(1.0, WeightUnit.POUND, 453.592, WeightUnit.GRAM);

        System.out.println("\n=== UC9: Weight Conversion Demo ===");
        demonstrateWeightConversion(1.0, WeightUnit.KILOGRAM, WeightUnit.GRAM);
        demonstrateWeightConversion(2.0, WeightUnit.POUND, WeightUnit.KILOGRAM);
        demonstrateWeightConversion(500.0, WeightUnit.GRAM, WeightUnit.POUND);
        demonstrateWeightConversion(0.0, WeightUnit.KILOGRAM, WeightUnit.GRAM);

        System.out.println("\n=== UC9: Weight Addition Demo ===");
        demonstrateWeightAddition(1.0, WeightUnit.KILOGRAM, 2.0, WeightUnit.KILOGRAM);
        demonstrateWeightAddition(1.0, WeightUnit.KILOGRAM, 1000.0, WeightUnit.GRAM);
        demonstrateWeightAddition(500.0, WeightUnit.GRAM, 0.5, WeightUnit.KILOGRAM);

        System.out.println("\n=== UC9: Weight Addition with Target Unit Demo ===");
        demonstrateWeightAddition(1.0, WeightUnit.KILOGRAM, 1000.0, WeightUnit.GRAM, WeightUnit.GRAM);
        demonstrateWeightAddition(1.0, WeightUnit.POUND, 453.592, WeightUnit.GRAM, WeightUnit.POUND);
        demonstrateWeightAddition(2.0, WeightUnit.KILOGRAM, 4.0, WeightUnit.POUND, WeightUnit.KILOGRAM);

        System.out.println("\n=== UC9: Category Incompatibility Demo ===");
        System.out.printf("Quantity(1.0, KILOGRAM).equals(Quantity(1.0, FEET)) → %b%n",
                controller.performComparison(
                        new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM),
                        new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET)
                ));

        // UC11: Volume Measurement Demonstrations
        // ─────────────────────────────────────────────

        System.out.println("\n=== UC11: Volume Equality Demo ===");
        demonstrateVolumeEquality(1.0, VolumeUnit.LITRE, 1.0, VolumeUnit.LITRE);
        demonstrateVolumeEquality(1.0, VolumeUnit.LITRE, 1000.0, VolumeUnit.MILLILITRE);
        demonstrateVolumeEquality(1.0, VolumeUnit.GALLON, 1.0, VolumeUnit.GALLON);
        demonstrateVolumeEquality(1.0, VolumeUnit.LITRE, 0.264172, VolumeUnit.GALLON);
        demonstrateVolumeEquality(500.0, VolumeUnit.MILLILITRE, 0.5, VolumeUnit.LITRE);
        demonstrateVolumeEquality(3.78541, VolumeUnit.LITRE, 1.0, VolumeUnit.GALLON);

        System.out.println("\n=== UC11: Volume Conversion Demo ===");
        demonstrateVolumeConversion(1.0, VolumeUnit.LITRE, VolumeUnit.MILLILITRE);
        demonstrateVolumeConversion(2.0, VolumeUnit.GALLON, VolumeUnit.LITRE);
        demonstrateVolumeConversion(500.0, VolumeUnit.MILLILITRE, VolumeUnit.GALLON);
        demonstrateVolumeConversion(0.0, VolumeUnit.LITRE, VolumeUnit.MILLILITRE);
        demonstrateVolumeConversion(1.0, VolumeUnit.LITRE, VolumeUnit.LITRE);

        System.out.println("\n=== UC11: Volume Addition Demo ===");
        demonstrateVolumeAddition(1.0, VolumeUnit.LITRE, 2.0, VolumeUnit.LITRE);
        demonstrateVolumeAddition(1.0, VolumeUnit.LITRE, 1000.0, VolumeUnit.MILLILITRE);
        demonstrateVolumeAddition(500.0, VolumeUnit.MILLILITRE, 0.5, VolumeUnit.LITRE);
        demonstrateVolumeAddition(2.0, VolumeUnit.GALLON, 3.78541, VolumeUnit.LITRE);

        System.out.println("\n=== UC11: Volume Addition with Target Unit Demo ===");
        demonstrateVolumeAddition(1.0, VolumeUnit.LITRE, 1000.0, VolumeUnit.MILLILITRE, VolumeUnit.MILLILITRE);
        demonstrateVolumeAddition(1.0, VolumeUnit.GALLON, 3.78541, VolumeUnit.LITRE, VolumeUnit.GALLON);
        demonstrateVolumeAddition(500.0, VolumeUnit.MILLILITRE, 1.0, VolumeUnit.LITRE, VolumeUnit.GALLON);
        demonstrateVolumeAddition(2.0, VolumeUnit.LITRE, 4.0, VolumeUnit.GALLON, VolumeUnit.LITRE);

        System.out.println("\n=== UC11: Category Incompatibility Demo ===");
        System.out.printf("Quantity(1.0, LITRE).equals(Quantity(1.0, FEET)) → %b%n",
                controller.performComparison(
                        new QuantityDTO(1.0, QuantityDTO.VolumeUnit.LITRE),
                        new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET)
                ));
        System.out.printf("Quantity(1.0, LITRE).equals(Quantity(1.0, KILOGRAM)) → %b%n",
                controller.performComparison(
                        new QuantityDTO(1.0, QuantityDTO.VolumeUnit.LITRE),
                        new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM)
                ));

        // UC12: Subtraction and Division Demonstrations
        // ─────────────────────────────────────────────

        System.out.println("\n=== UC12: Subtraction with Implicit Target Unit ===");
        demonstrateSubtraction(10.0, LengthUnit.FEET, 6.0, LengthUnit.INCH);
        demonstrateSubtraction(10.0, WeightUnit.KILOGRAM, 5000.0, WeightUnit.GRAM);
        demonstrateSubtraction(10.0, VolumeUnit.LITRE, 500.0, VolumeUnit.MILLILITRE);

        System.out.println("\n=== UC12: Subtraction with Explicit Target Unit ===");
        demonstrateSubtraction(10.0, LengthUnit.FEET, 6.0, LengthUnit.INCH, LengthUnit.INCH);
        demonstrateSubtraction(10.0, WeightUnit.KILOGRAM, 5000.0, WeightUnit.GRAM, WeightUnit.GRAM);
        demonstrateSubtraction(10.0, VolumeUnit.LITRE, 500.0, VolumeUnit.MILLILITRE, VolumeUnit.MILLILITRE);

        System.out.println("\n=== UC12: Subtraction Resulting in Negative Values ===");
        demonstrateSubtraction(5.0, LengthUnit.FEET, 10.0, LengthUnit.FEET);
        demonstrateSubtraction(2.0, WeightUnit.KILOGRAM, 5.0, WeightUnit.KILOGRAM);
        demonstrateSubtraction(2.0, VolumeUnit.LITRE, 5.0, VolumeUnit.LITRE);

        System.out.println("\n=== UC12: Subtraction Resulting in Zero ===");
        demonstrateSubtraction(10.0, LengthUnit.FEET, 120.0, LengthUnit.INCH);
        demonstrateSubtraction(1.0, VolumeUnit.LITRE, 1000.0, VolumeUnit.MILLILITRE);

        System.out.println("\n=== UC12: Division Operations ===");
        demonstrateDivision(10.0, LengthUnit.FEET, 2.0, LengthUnit.FEET);
        demonstrateDivision(10.0, LengthUnit.FEET, 5.0, LengthUnit.FEET);
        demonstrateDivision(24.0, LengthUnit.INCH, 2.0, LengthUnit.FEET);
        demonstrateDivision(10.0, WeightUnit.KILOGRAM, 5.0, WeightUnit.KILOGRAM);
        demonstrateDivision(10.0, VolumeUnit.LITRE, 2.0, VolumeUnit.LITRE);

        System.out.println("\n=== UC12: Division with Different Units (Same Category) ===");
        demonstrateDivision(12.0, LengthUnit.INCH, 1.0, LengthUnit.FEET);
        demonstrateDivision(2000.0, WeightUnit.GRAM, 1.0, WeightUnit.KILOGRAM);
        demonstrateDivision(1000.0, VolumeUnit.MILLILITRE, 1.0, VolumeUnit.LITRE);

        // UC14: Temperature Measurement demonstrations
        System.out.println("\n=== UC14: Temperature Equality Comparisons ===");
        System.out.printf("Input: new Quantity<>(0.0, CELSIUS).equals(new Quantity<>(32.0, FAHRENHEIT)) → Output: %b%n",
                controller.performComparison(
                        new QuantityDTO(0.0, QuantityDTO.TemperatureUnit.CELSIUS),
                        new QuantityDTO(32.0, QuantityDTO.TemperatureUnit.FAHRENHEIT)
                ));
        System.out.printf("Input: new Quantity<>(273.15, KELVIN).equals(new Quantity<>(0.0, CELSIUS)) → Output: %b%n",
                controller.performComparison(
                        new QuantityDTO(273.15, QuantityDTO.TemperatureUnit.KELVIN),
                        new QuantityDTO(0.0, QuantityDTO.TemperatureUnit.CELSIUS)
                ));
        System.out.printf("Input: new Quantity<>(212.0, FAHRENHEIT).equals(new Quantity<>(100.0, CELSIUS)) → Output: %b%n",
                controller.performComparison(
                        new QuantityDTO(212.0, QuantityDTO.TemperatureUnit.FAHRENHEIT),
                        new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS)
                ));
        System.out.printf("Input: new Quantity<>(100.0, CELSIUS).equals(new Quantity<>(373.15, KELVIN)) → Output: %b%n",
                controller.performComparison(
                        new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS),
                        new QuantityDTO(373.15, QuantityDTO.TemperatureUnit.KELVIN)
                ));
        System.out.printf("Input: new Quantity<>(50.0, CELSIUS).equals(new Quantity<>(122.0, FAHRENHEIT)) → Output: %b%n",
                controller.performComparison(
                        new QuantityDTO(50.0, QuantityDTO.TemperatureUnit.CELSIUS),
                        new QuantityDTO(122.0, QuantityDTO.TemperatureUnit.FAHRENHEIT)
                ));

        System.out.println("\n=== UC14: Temperature Conversions ===");
        System.out.printf("Input: new Quantity<>(100.0, CELSIUS).convertTo(FAHRENHEIT) → Output: %s%n",
                controller.performConversion(
                        new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS),
                        QuantityDTO.TemperatureUnit.FAHRENHEIT
                ));
        System.out.printf("Input: new Quantity<>(32.0, FAHRENHEIT).convertTo(CELSIUS) → Output: %s%n",
                controller.performConversion(
                        new QuantityDTO(32.0, QuantityDTO.TemperatureUnit.FAHRENHEIT),
                        QuantityDTO.TemperatureUnit.CELSIUS
                ));
        System.out.printf("Input: new Quantity<>(273.15, KELVIN).convertTo(CELSIUS) → Output: %s%n",
                controller.performConversion(
                        new QuantityDTO(273.15, QuantityDTO.TemperatureUnit.KELVIN),
                        QuantityDTO.TemperatureUnit.CELSIUS
                ));
        System.out.printf("Input: new Quantity<>(0.0, CELSIUS).convertTo(KELVIN) → Output: %s%n",
                controller.performConversion(
                        new QuantityDTO(0.0, QuantityDTO.TemperatureUnit.CELSIUS),
                        QuantityDTO.TemperatureUnit.KELVIN
                ));
        System.out.printf("Input: new Quantity<>(-40.0, CELSIUS).convertTo(FAHRENHEIT) → Output: %s%n",
                controller.performConversion(
                        new QuantityDTO(-40.0, QuantityDTO.TemperatureUnit.CELSIUS),
                        QuantityDTO.TemperatureUnit.FAHRENHEIT
                ));

        System.out.println("\n=== UC14: Unsupported Operations (Error Handling) ===");
        try {
            controller.performAddition(
                    new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS),
                    new QuantityDTO(50.0, QuantityDTO.TemperatureUnit.CELSIUS),
                    null
            );
        } catch (QuantityMeasurementException e) {
            System.out.printf("Input: new Quantity<>(100.0, CELSIUS).add(...) → Throws Exception: %s%n", e.getMessage());
        }
        try {
            controller.performSubtraction(
                    new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS),
                    new QuantityDTO(50.0, QuantityDTO.TemperatureUnit.CELSIUS),
                    null
            );
        } catch (QuantityMeasurementException e) {
            System.out.printf("Input: new Quantity<>(100.0, CELSIUS).subtract(...) → Throws Exception: %s%n", e.getMessage());
        }
        try {
            controller.performDivision(
                    new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS),
                    new QuantityDTO(50.0, QuantityDTO.TemperatureUnit.CELSIUS)
            );
        } catch (QuantityMeasurementException e) {
            System.out.printf("Input: new Quantity<>(100.0, CELSIUS).divide(...) → Throws Exception: %s%n", e.getMessage());
        }

        System.out.println("\n=== UC14: Cross-Category Prevention ===");
        System.out.printf("Input: new Quantity<>(100.0, CELSIUS).equals(new Quantity<>(100.0, LengthUnit.FEET)) → Output: %b%n",
                controller.performComparison(
                        new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS),
                        new QuantityDTO(100.0, QuantityDTO.LengthUnit.FEET)
                ));
        System.out.printf("Input: new Quantity<>(50.0, CELSIUS).equals(new Quantity<>(50.0, WeightUnit.KILOGRAM)) → Output: %b%n",
                controller.performComparison(
                        new QuantityDTO(50.0, QuantityDTO.TemperatureUnit.CELSIUS),
                        new QuantityDTO(50.0, QuantityDTO.WeightUnit.KILOGRAM)
                ));

        // Repository History serialization display
        System.out.println("\n=== UC15: Repository History Cache Demo ===");
        System.out.println("Total saved operations: " + repository.getAllMeasurements().size());
        repository.getAllMeasurements().stream().limit(10).forEach(System.out::println);
        if (repository.getAllMeasurements().size() > 10) {
            System.out.println("... and " + (repository.getAllMeasurements().size() - 10) + " more.");
        }
    }

    // Helper to map Domain IMeasurable to QuantityDTO.IMeasurableUnit
    private static QuantityDTO.IMeasurableUnit mapToDtoUnit(IMeasurable domainUnit) {
        if (domainUnit == null) return null;
        String name = ((Enum<?>) domainUnit).name();
        if (domainUnit instanceof LengthUnit) {
            return QuantityDTO.LengthUnit.valueOf(name);
        } else if (domainUnit instanceof WeightUnit) {
            return QuantityDTO.WeightUnit.valueOf(name);
        } else if (domainUnit instanceof VolumeUnit) {
            return QuantityDTO.VolumeUnit.valueOf(name);
        } else if (domainUnit instanceof TemperatureUnit) {
            return QuantityDTO.TemperatureUnit.valueOf(name);
        }
        throw new IllegalArgumentException("Unsupported domain unit: " + domainUnit.getClass().getName());
    }

    // ─────────────────────────────────────────────
    // UC8: LengthUnit direct conversion demo
    // ─────────────────────────────────────────────
    public static void demonstrateUnitConversion(LengthUnit unit, double value) {
        double toBase   = unit.convertToBaseUnit(value);
        double fromBase = unit.convertFromBaseUnit(toBase);
        System.out.printf("Input: LengthUnit.%s.convertToBaseUnit(%.4f) → Output: %.4f%n", unit, value, toBase);
        System.out.printf("Input: LengthUnit.%s.convertFromBaseUnit(%.4f) → Output: %.4f%n%n", unit, toBase, fromBase);
    }

    // ─────────────────────────────────────────────
    // Length: Conversion, Equality, Addition
    // ─────────────────────────────────────────────
    public static void demonstrateLengthConversion(double value, LengthUnit fromUnit, LengthUnit toUnit) {
        QuantityDTO op = new QuantityDTO(value, mapToDtoUnit(fromUnit));
        QuantityDTO result = controller.performConversion(op, mapToDtoUnit(toUnit));
        System.out.printf("Input: convert(%.4f, %s, %s) → Output: %.4f%n", value, fromUnit, toUnit, result.getValue());
    }

    public static void demonstrateLengthConversion(Quantity<LengthUnit> length, LengthUnit toUnit) {
        QuantityDTO op = new QuantityDTO(length.getValue(), mapToDtoUnit(length.getUnit()));
        QuantityDTO converted = controller.performConversion(op, mapToDtoUnit(toUnit));
        System.out.printf("Input: %s.convertTo(%s) → Output: %s%n", length, toUnit, converted);
    }

    public static void demonstrateAddition(double val1, LengthUnit unit1, double val2, LengthUnit unit2) {
        QuantityDTO op1 = new QuantityDTO(val1, mapToDtoUnit(unit1));
        QuantityDTO op2 = new QuantityDTO(val2, mapToDtoUnit(unit2));
        QuantityDTO sum = controller.performAddition(op1, op2, null);
        System.out.printf("Input: add(%s, %s)\nOutput: %s\n\n", op1, op2, sum);
    }

    public static void demonstrateAddition(double val1, LengthUnit unit1, double val2, LengthUnit unit2, LengthUnit targetUnit) {
        QuantityDTO op1 = new QuantityDTO(val1, mapToDtoUnit(unit1));
        QuantityDTO op2 = new QuantityDTO(val2, mapToDtoUnit(unit2));
        QuantityDTO sum = controller.performAddition(op1, op2, mapToDtoUnit(targetUnit));
        System.out.printf("Input: add(%s, %s, %s)\nOutput: %s\n\n", op1, op2, targetUnit, sum);
    }

    public static void demonstrateLengthEquality(Quantity<LengthUnit> a, Quantity<LengthUnit> b) {
        QuantityDTO op1 = new QuantityDTO(a.getValue(), mapToDtoUnit(a.getUnit()));
        QuantityDTO op2 = new QuantityDTO(b.getValue(), mapToDtoUnit(b.getUnit()));
        boolean result = controller.performComparison(op1, op2);
        System.out.printf("Equality: %s == %s → %b%n", a, b, result);
    }

    public static void demonstrateLengthComparison(double val1, LengthUnit unit1, double val2, LengthUnit unit2) {
        Quantity<LengthUnit> a = new Quantity<>(val1, unit1);
        Quantity<LengthUnit> b = new Quantity<>(val2, unit2);
        demonstrateLengthEquality(a, b);
    }

    // ─────────────────────────────────────────────
    // UC9: Weight demos
    // ─────────────────────────────────────────────
    public static void demonstrateWeightEquality(double val1, WeightUnit unit1, double val2, WeightUnit unit2) {
        QuantityDTO op1 = new QuantityDTO(val1, mapToDtoUnit(unit1));
        QuantityDTO op2 = new QuantityDTO(val2, mapToDtoUnit(unit2));
        boolean result = controller.performComparison(op1, op2);
        System.out.printf("Equality: Quantity{%s %s} == Quantity{%s %s} → %b%n", val1, unit1, val2, unit2, result);
    }

    public static void demonstrateWeightConversion(double value, WeightUnit fromUnit, WeightUnit toUnit) {
        QuantityDTO op = new QuantityDTO(value, mapToDtoUnit(fromUnit));
        QuantityDTO converted = controller.performConversion(op, mapToDtoUnit(toUnit));
        System.out.printf("Input: Quantity{%s %s}.convertTo(%s) → Output: %s%n", value, fromUnit, toUnit, converted);
    }

    public static void demonstrateWeightAddition(double val1, WeightUnit unit1, double val2, WeightUnit unit2) {
        QuantityDTO op1 = new QuantityDTO(val1, mapToDtoUnit(unit1));
        QuantityDTO op2 = new QuantityDTO(val2, mapToDtoUnit(unit2));
        QuantityDTO sum = controller.performAddition(op1, op2, null);
        System.out.printf("Input: add(%s, %s)\nOutput: %s\n\n", op1, op2, sum);
    }

    public static void demonstrateWeightAddition(double val1, WeightUnit unit1, double val2, WeightUnit unit2, WeightUnit targetUnit) {
        QuantityDTO op1 = new QuantityDTO(val1, mapToDtoUnit(unit1));
        QuantityDTO op2 = new QuantityDTO(val2, mapToDtoUnit(unit2));
        QuantityDTO sum = controller.performAddition(op1, op2, mapToDtoUnit(targetUnit));
        System.out.printf("Input: add(%s, %s, %s)\nOutput: %s\n\n", op1, op2, targetUnit, sum);
    }

    // ─────────────────────────────────────────────
    // UC11: Volume demos
    // ─────────────────────────────────────────────
    public static void demonstrateVolumeEquality(double val1, VolumeUnit unit1, double val2, VolumeUnit unit2) {
        QuantityDTO op1 = new QuantityDTO(val1, mapToDtoUnit(unit1));
        QuantityDTO op2 = new QuantityDTO(val2, mapToDtoUnit(unit2));
        boolean result = controller.performComparison(op1, op2);
        System.out.printf("Equality: Quantity{%s %s} == Quantity{%s %s} → %b%n", val1, unit1, val2, unit2, result);
    }

    public static void demonstrateVolumeConversion(double value, VolumeUnit fromUnit, VolumeUnit toUnit) {
        QuantityDTO op = new QuantityDTO(value, mapToDtoUnit(fromUnit));
        QuantityDTO converted = controller.performConversion(op, mapToDtoUnit(toUnit));
        System.out.printf("Input: Quantity{%s %s}.convertTo(%s) → Output: %s%n", value, fromUnit, toUnit, converted);
    }

    public static void demonstrateVolumeAddition(double val1, VolumeUnit unit1, double val2, VolumeUnit unit2) {
        QuantityDTO op1 = new QuantityDTO(val1, mapToDtoUnit(unit1));
        QuantityDTO op2 = new QuantityDTO(val2, mapToDtoUnit(unit2));
        QuantityDTO sum = controller.performAddition(op1, op2, null);
        System.out.printf("Input: add(%s, %s)\nOutput: %s\n\n", op1, op2, sum);
    }

    public static void demonstrateVolumeAddition(double val1, VolumeUnit unit1, double val2, VolumeUnit unit2, VolumeUnit targetUnit) {
        QuantityDTO op1 = new QuantityDTO(val1, mapToDtoUnit(unit1));
        QuantityDTO op2 = new QuantityDTO(val2, mapToDtoUnit(unit2));
        QuantityDTO sum = controller.performAddition(op1, op2, mapToDtoUnit(targetUnit));
        System.out.printf("Input: add(%s, %s, %s)\nOutput: %s\n\n", op1, op2, targetUnit, sum);
    }

    // ─────────────────────────────────────────────
    // UC12: Generic Subtraction demos
    // ─────────────────────────────────────────────
    public static <U extends IMeasurable> void demonstrateSubtraction(double val1, U unit1, double val2, U unit2) {
        QuantityDTO op1 = new QuantityDTO(val1, mapToDtoUnit(unit1));
        QuantityDTO op2 = new QuantityDTO(val2, mapToDtoUnit(unit2));
        QuantityDTO diff = controller.performSubtraction(op1, op2, null);
        System.out.printf("Input: %s.subtract(%s)%nOutput: %s%n%n", op1, op2, diff);
    }

    public static <U extends IMeasurable> void demonstrateSubtraction(double val1, U unit1, double val2, U unit2, U targetUnit) {
        QuantityDTO op1 = new QuantityDTO(val1, mapToDtoUnit(unit1));
        QuantityDTO op2 = new QuantityDTO(val2, mapToDtoUnit(unit2));
        QuantityDTO diff = controller.performSubtraction(op1, op2, mapToDtoUnit(targetUnit));
        System.out.printf("Input: %s.subtract(%s, %s)%nOutput: %s%n%n", op1, op2, targetUnit, diff);
    }

    // ─────────────────────────────────────────────
    // UC12: Generic Division demos
    // ─────────────────────────────────────────────
    public static <U extends IMeasurable> void demonstrateDivision(double val1, U unit1, double val2, U unit2) {
        QuantityDTO op1 = new QuantityDTO(val1, mapToDtoUnit(unit1));
        QuantityDTO op2 = new QuantityDTO(val2, mapToDtoUnit(unit2));
        double ratio = controller.performDivision(op1, op2);
        System.out.printf("Input: %s.divide(%s)%nOutput: %.6f%n%n", op1, op2, ratio);
    }
}
