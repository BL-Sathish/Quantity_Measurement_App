package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.services.IQuantityMeasurementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuantityMeasurementControllerTest {

    private IQuantityMeasurementService mockService;
    private QuantityMeasurementController controller;

    @BeforeEach
    void setUp() {
        mockService = mock(IQuantityMeasurementService.class);
        controller = new QuantityMeasurementController(mockService);
    }

    @Test
    void testPerformComparison() {
        QuantityDTO op1 = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO op2 = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCH);

        when(mockService.compare(op1, op2)).thenReturn(true);

        boolean result = controller.performComparison(op1, op2);
        assertTrue(result);
        verify(mockService, times(1)).compare(op1, op2);
    }

    @Test
    void testPerformConversion() {
        QuantityDTO op = new QuantityDTO(1.0, QuantityDTO.LengthUnit.YARD);
        QuantityDTO expected = new QuantityDTO(3.0, QuantityDTO.LengthUnit.FEET);

        when(mockService.convert(op, QuantityDTO.LengthUnit.FEET)).thenReturn(expected);

        QuantityDTO result = controller.performConversion(op, QuantityDTO.LengthUnit.FEET);
        assertEquals(expected, result);
        verify(mockService, times(1)).convert(op, QuantityDTO.LengthUnit.FEET);
    }

    @Test
    void testPerformAddition() {
        QuantityDTO op1 = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO op2 = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCH);
        QuantityDTO expected = new QuantityDTO(2.0, QuantityDTO.LengthUnit.FEET);

        when(mockService.add(op1, op2, QuantityDTO.LengthUnit.FEET)).thenReturn(expected);

        QuantityDTO result = controller.performAddition(op1, op2, QuantityDTO.LengthUnit.FEET);
        assertEquals(expected, result);
        verify(mockService, times(1)).add(op1, op2, QuantityDTO.LengthUnit.FEET);
    }

    @Test
    void testPerformSubtraction() {
        QuantityDTO op1 = new QuantityDTO(2.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO op2 = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCH);
        QuantityDTO expected = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);

        when(mockService.subtract(op1, op2, QuantityDTO.LengthUnit.FEET)).thenReturn(expected);

        QuantityDTO result = controller.performSubtraction(op1, op2, QuantityDTO.LengthUnit.FEET);
        assertEquals(expected, result);
        verify(mockService, times(1)).subtract(op1, op2, QuantityDTO.LengthUnit.FEET);
    }

    @Test
    void testPerformDivision() {
        QuantityDTO op1 = new QuantityDTO(6.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO op2 = new QuantityDTO(3.0, QuantityDTO.LengthUnit.FEET);

        when(mockService.divide(op1, op2)).thenReturn(2.0);

        double result = controller.performDivision(op1, op2);
        assertEquals(2.0, result);
        verify(mockService, times(1)).divide(op1, op2);
    }
}
