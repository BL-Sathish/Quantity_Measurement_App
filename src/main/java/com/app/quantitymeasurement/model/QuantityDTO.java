package com.app.quantitymeasurement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

/**
 * Data Transfer Object (DTO) class for transferring measurement data.
 * Compatible with JSON web requests and existing domain test suites.
 */
public class QuantityDTO {

    @NotNull(message = "Value cannot be null")
    private Double value;

    @NotEmpty(message = "Unit cannot be null or empty")
    private String unitName;

    @NotEmpty(message = "Measurement type cannot be null or empty")
    @Pattern(regexp = "^(LengthUnit|VolumeUnit|WeightUnit|TemperatureUnit)$", message = "Invalid measurement type")
    private String measurementType;

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

    public QuantityDTO(Double value, IMeasurableUnit unit) {
        this.value = value;
        this.unitName = unit != null ? unit.name() : null;
        this.measurementType = unit != null ? unit.getMeasurementType() + "Unit" : null;
    }

    public QuantityDTO(Double value, String unitName, String measurementType) {
        this.value = value;
        this.unitName = unitName;
        this.measurementType = measurementType;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @JsonProperty("unit")
    public String getUnitName() {
        return unitName;
    }

    @JsonProperty("unit")
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @JsonProperty("measurementType")
    public String getMeasurementType() {
        return measurementType;
    }

    @JsonProperty("measurementType")
    public void setMeasurementType(String measurementType) {
        this.measurementType = measurementType;
    }

    @JsonIgnore
    public IMeasurableUnit getUnit() {
        if (unitName == null || measurementType == null) {
            return null;
        }
        String type = measurementType.replace("Unit", "");
        try {
            if ("Length".equalsIgnoreCase(type)) {
                String normalizedUnit = unitName.toUpperCase();
                if (normalizedUnit.endsWith("ES")) {
                    normalizedUnit = normalizedUnit.substring(0, normalizedUnit.length() - 2); // INCHES -> INCH
                } else if (normalizedUnit.endsWith("S") && !normalizedUnit.equals("CELSIUS")) {
                    normalizedUnit = normalizedUnit.substring(0, normalizedUnit.length() - 1);
                }
                return LengthUnit.valueOf(normalizedUnit);
            } else if ("Weight".equalsIgnoreCase(type)) {
                String normalizedUnit = unitName.toUpperCase();
                if (normalizedUnit.endsWith("S")) {
                    normalizedUnit = normalizedUnit.substring(0, normalizedUnit.length() - 1);
                }
                return WeightUnit.valueOf(normalizedUnit);
            } else if ("Volume".equalsIgnoreCase(type)) {
                String normalizedUnit = unitName.toUpperCase();
                if (normalizedUnit.endsWith("S")) {
                    normalizedUnit = normalizedUnit.substring(0, normalizedUnit.length() - 1);
                }
                return VolumeUnit.valueOf(normalizedUnit);
            } else if ("Temperature".equalsIgnoreCase(type)) {
                String normalizedUnit = unitName.toUpperCase();
                if (normalizedUnit.endsWith("S")) {
                    normalizedUnit = normalizedUnit.substring(0, normalizedUnit.length() - 1);
                }
                return TemperatureUnit.valueOf(normalizedUnit);
            }
        } catch (IllegalArgumentException e) {
            // Ignore mapping error
        }
        return null;
    }

    @JsonIgnore
    public void setUnit(IMeasurableUnit unit) {
        this.unitName = unit != null ? unit.name() : null;
        this.measurementType = unit != null ? unit.getMeasurementType() + "Unit" : null;
    }

    @Override
    public String toString() {
        return "QuantityDTO{" +
                "value=" + value +
                ", unit='" + unitName + '\'' +
                ", measurementType='" + measurementType + '\'' +
                '}';
    }
}
