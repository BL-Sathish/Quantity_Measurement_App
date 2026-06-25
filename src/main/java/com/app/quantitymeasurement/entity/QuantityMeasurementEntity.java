package com.app.quantitymeasurement.entity;

import java.io.Serializable;
import java.util.UUID;

/**
 * Immutable-like entity to represent a measurement record in the repository.
 * Non-final fields are used to support standard Java serialization.
 */
public class QuantityMeasurementEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private Double operand1Value;
    private String operand1Unit;
    private String operand1Type;
    private Double operand2Value;
    private String operand2Unit;
    private String operand2Type;
    private String operationType;
    private Double resultValue;
    private Boolean resultBoolean;
    private String resultUnit;
    private Boolean hasError;
    private String errorMessage;

    public QuantityMeasurementEntity() {
        this.id = UUID.randomUUID().toString();
    }

    // Constructor for Conversion operations
    public QuantityMeasurementEntity(double operand1Value, String operand1Unit, String operand1Type, double resultValue, String resultUnit, String operationType) {
        this();
        this.operand1Value = operand1Value;
        this.operand1Unit = operand1Unit;
        this.operand1Type = operand1Type;
        this.resultValue = resultValue;
        this.resultUnit = resultUnit;
        this.operationType = operationType;
        this.hasError = false;
    }

    // Constructor for Comparison operations
    public QuantityMeasurementEntity(double operand1Value, String operand1Unit, String operand1Type, double operand2Value, String operand2Unit, String operand2Type, boolean resultBoolean, String operationType) {
        this();
        this.operand1Value = operand1Value;
        this.operand1Unit = operand1Unit;
        this.operand1Type = operand1Type;
        this.operand2Value = operand2Value;
        this.operand2Unit = operand2Unit;
        this.operand2Type = operand2Type;
        this.resultBoolean = resultBoolean;
        this.operationType = operationType;
        this.hasError = false;
    }

    // Constructor for Arithmetic operations
    public QuantityMeasurementEntity(double operand1Value, String operand1Unit, String operand1Type, double operand2Value, String operand2Unit, String operand2Type, double resultValue, String resultUnit, String operationType) {
        this();
        this.operand1Value = operand1Value;
        this.operand1Unit = operand1Unit;
        this.operand1Type = operand1Type;
        this.operand2Value = operand2Value;
        this.operand2Unit = operand2Unit;
        this.operand2Type = operand2Type;
        this.resultValue = resultValue;
        this.resultUnit = resultUnit;
        this.operationType = operationType;
        this.hasError = false;
    }

    // Constructor for Error scenario
    public QuantityMeasurementEntity(String errorMessage, String operationType) {
        this();
        this.errorMessage = errorMessage;
        this.operationType = operationType;
        this.hasError = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getOperand1Value() {
        return operand1Value;
    }

    public void setOperand1Value(Double operand1Value) {
        this.operand1Value = operand1Value;
    }

    public String getOperand1Unit() {
        return operand1Unit;
    }

    public void setOperand1Unit(String operand1Unit) {
        this.operand1Unit = operand1Unit;
    }

    public String getOperand1Type() {
        return operand1Type;
    }

    public void setOperand1Type(String operand1Type) {
        this.operand1Type = operand1Type;
    }

    public Double getOperand2Value() {
        return operand2Value;
    }

    public void setOperand2Value(Double operand2Value) {
        this.operand2Value = operand2Value;
    }

    public String getOperand2Unit() {
        return operand2Unit;
    }

    public void setOperand2Unit(String operand2Unit) {
        this.operand2Unit = operand2Unit;
    }

    public String getOperand2Type() {
        return operand2Type;
    }

    public void setOperand2Type(String operand2Type) {
        this.operand2Type = operand2Type;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Double getResultValue() {
        return resultValue;
    }

    public void setResultValue(Double resultValue) {
        this.resultValue = resultValue;
    }

    public Boolean getResultBoolean() {
        return resultBoolean;
    }

    public void setResultBoolean(Boolean resultBoolean) {
        this.resultBoolean = resultBoolean;
    }

    public String getResultUnit() {
        return resultUnit;
    }

    public void setResultUnit(String resultUnit) {
        this.resultUnit = resultUnit;
    }

    public Boolean getHasError() {
        return hasError;
    }

    public void setHasError(Boolean hasError) {
        this.hasError = hasError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        if (hasError != null && hasError) {
            return "QuantityMeasurementEntity{id='" + id + "', operation='" + operationType + "', error='" + errorMessage + "'}";
        }
        if ("comparison".equals(operationType)) {
            return "QuantityMeasurementEntity{id='" + id + "', operation='" + operationType + "', op1=" + operand1Value + " " + operand1Unit + ", op2=" + operand2Value + " " + operand2Unit + ", result=" + resultBoolean + "}";
        }
        if ("conversion".equals(operationType)) {
            return "QuantityMeasurementEntity{id='" + id + "', operation='" + operationType + "', input=" + operand1Value + " " + operand1Unit + ", result=" + resultValue + " " + resultUnit + "}";
        }
        return "QuantityMeasurementEntity{id='" + id + "', operation='" + operationType + "', op1=" + operand1Value + " " + operand1Unit + ", op2=" + operand2Value + " " + operand2Unit + ", result=" + resultValue + " " + resultUnit + "}";
    }
}
