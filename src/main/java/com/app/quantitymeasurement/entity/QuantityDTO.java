package com.app.quantitymeasurement.entity;

/**
 * Data Transfer Object (DTO) class for transferring measurement data.
 */
public class QuantityDTO {
    private double value;
    private IMeasurableUnit unit;

    public interface IMeasurableUnit {
        String getMeasurementType();
        String name();
    }

    public enum LengthUnit implements IMeasurableUnit {
        FEET, INCH, YARD, CENTIMETER;

        @Override
        public String getMeasurementType() {
            return "Length";
        }
    }

    public enum WeightUnit implements IMeasurableUnit {
        KILOGRAM, GRAM, POUND;

        @Override
        public String getMeasurementType() {
            return "Weight";
        }
    }

    public enum VolumeUnit implements IMeasurableUnit {
        LITRE, MILLILITRE, GALLON;

        @Override
        public String getMeasurementType() {
            return "Volume";
        }
    }

    public enum TemperatureUnit implements IMeasurableUnit {
        CELSIUS, FAHRENHEIT, KELVIN;

        @Override
        public String getMeasurementType() {
            return "Temperature";
        }
    }

    public QuantityDTO() {}

    public QuantityDTO(double value, IMeasurableUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public IMeasurableUnit getUnit() {
        return unit;
    }

    public void setUnit(IMeasurableUnit unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "QuantityDTO{" +
                "value=" + value +
                ", unit=" + (unit != null ? unit.name() : "null") +
                '}';
    }
}
