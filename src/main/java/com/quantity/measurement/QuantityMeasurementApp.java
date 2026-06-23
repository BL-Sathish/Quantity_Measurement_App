package com.quantity.measurement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QuantityMeasurementApp {

    public static void main(String[] args) {
        SpringApplication.run(QuantityMeasurementApp.class, args);

        // UC4 Example logic execution
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.YARD);
        QuantityLength q2 = new QuantityLength(3.0, LengthUnit.FEET);
        System.out.println("Input: Quantity(1.0, YARDS) and Quantity(3.0, FEET)");
        System.out.println("Output: Equal (" + q1.equals(q2) + ")");

        QuantityLength q3 = new QuantityLength(1.0, LengthUnit.YARD);
        QuantityLength q4 = new QuantityLength(36.0, LengthUnit.INCH);
        System.out.println("Input: Quantity(1.0, YARDS) and Quantity(36.0, INCHES)");
        System.out.println("Output: Equal (" + q3.equals(q4) + ")");

        QuantityLength q5 = new QuantityLength(2.0, LengthUnit.YARD);
        QuantityLength q6 = new QuantityLength(2.0, LengthUnit.YARD);
        System.out.println("Input: Quantity(2.0, YARDS) and Quantity(2.0, YARDS)");
        System.out.println("Output: Equal (" + q5.equals(q6) + ")");

        QuantityLength q7 = new QuantityLength(2.0, LengthUnit.CENTIMETER);
        QuantityLength q8 = new QuantityLength(2.0, LengthUnit.CENTIMETER);
        System.out.println("Input: Quantity(2.0, CENTIMETERS) and Quantity(2.0, CENTIMETERS)");
        System.out.println("Output: Equal (" + q7.equals(q8) + ")");

        QuantityLength q9 = new QuantityLength(1.0, LengthUnit.CENTIMETER);
        QuantityLength q10 = new QuantityLength(0.393701, LengthUnit.INCH);
        System.out.println("Input: Quantity(1.0, CENTIMETERS) and Quantity(0.393701, INCHES)");
        System.out.println("Output: Equal (" + q9.equals(q10) + ")");
    }
}
