package com.quantity.measurement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QuantityMeasurementApp {

    public static void main(String[] args) {
        SpringApplication.run(QuantityMeasurementApp.class, args);

        // UC2 Example logic execution
        System.out.println("Input: 1.0 inch and 1.0 inch");
        System.out.println("Output: Equal (" + compareInches(1.0, 1.0) + ")");
        
        System.out.println("Input: 1.0 ft and 1.0 ft");
        System.out.println("Output: Equal (" + compareFeet(1.0, 1.0) + ")");
    }

    public static boolean compareFeet(double val1, double val2) {
        Feet feet1 = new Feet(val1);
        Feet feet2 = new Feet(val2);
        return feet1.equals(feet2);
    }

    public static boolean compareInches(double val1, double val2) {
        Inches inches1 = new Inches(val1);
        Inches inches2 = new Inches(val2);
        return inches1.equals(inches2);
    }

    /**
     * Inner class representing a Feet measurement.
     */
    public static class Feet {
        private final double value;

        public Feet(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Feet feet = (Feet) obj;
            return Double.compare(feet.value, value) == 0;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(value);
        }
    }

    /**
     * Inner class representing an Inches measurement.
     */
    public static class Inches {
        private final double value;

        public Inches(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Inches inches = (Inches) obj;
            return Double.compare(inches.value, value) == 0;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(value);
        }
    }
}
