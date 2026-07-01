package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;
import com.app.quantitymeasurement.services.IQuantityMeasurementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for QuantityMeasurementController.
 * Uses Mockito to isolate the controller from the service layer.
 */
@ExtendWith(MockitoExtension.class)
class QuantityMeasurementControllerTest {

    @Mock
    private IQuantityMeasurementService service;

    @InjectMocks
    private QuantityMeasurementController controller;

    @Test
    void testCompareQuantities() {
        QuantityDTO op1 = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO op2 = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCH);
        QuantityInputDTO input = new QuantityInputDTO(op1, op2);

        QuantityMeasurementDTO expected = new QuantityMeasurementDTO();
        expected.setOperation("compare");
        expected.setResultString("true");
        expected.setError(false);

        when(service.compare(op1, op2)).thenReturn(expected);

        QuantityMeasurementDTO result = controller.compareQuantities(input);
        assertEquals("compare", result.getOperation());
        assertEquals("true", result.getResultString());
        assertFalse(result.getError());
        verify(service, times(1)).compare(op1, op2);
    }

    @Test
    void testConvertQuantity() {
        QuantityDTO op1 = new QuantityDTO(1.0, QuantityDTO.LengthUnit.YARD);
        QuantityDTO op2 = new QuantityDTO(0.0, QuantityDTO.LengthUnit.FEET);
        QuantityInputDTO input = new QuantityInputDTO(op1, op2);

        QuantityMeasurementDTO expected = new QuantityMeasurementDTO();
        expected.setOperation("convert");
        expected.setResultValue(3.0);
        expected.setResultUnit("FEET");
        expected.setError(false);

        when(service.convert(eq(op1), eq(QuantityDTO.LengthUnit.FEET))).thenReturn(expected);

        QuantityMeasurementDTO result = controller.convertQuantity(input);
        assertEquals("convert", result.getOperation());
        assertEquals(3.0, result.getResultValue());
        assertEquals("FEET", result.getResultUnit());
        verify(service, times(1)).convert(eq(op1), eq(QuantityDTO.LengthUnit.FEET));
    }

    @Test
    void testAddQuantities() {
        QuantityDTO op1 = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO op2 = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCH);
        QuantityInputDTO input = new QuantityInputDTO(op1, op2);

        QuantityMeasurementDTO expected = new QuantityMeasurementDTO();
        expected.setOperation("add");
        expected.setResultValue(2.0);
        expected.setResultUnit("FEET");
        expected.setError(false);

        when(service.add(eq(op1), eq(op2), any())).thenReturn(expected);

        QuantityMeasurementDTO result = controller.addQuantities(input);
        assertEquals("add", result.getOperation());
        assertEquals(2.0, result.getResultValue());
        verify(service, times(1)).add(eq(op1), eq(op2), any());
    }

    @Test
    void testGetOperationHistory() {
        QuantityMeasurementDTO dto1 = new QuantityMeasurementDTO();
        dto1.setOperation("compare");
        QuantityMeasurementDTO dto2 = new QuantityMeasurementDTO();
        dto2.setOperation("compare");
        List<QuantityMeasurementDTO> expected = Arrays.asList(dto1, dto2);

        when(service.getHistoryByOperation("compare")).thenReturn(expected);

        List<QuantityMeasurementDTO> result = controller.getOperationHistory("compare");
        assertEquals(2, result.size());
        verify(service, times(1)).getHistoryByOperation("compare");
    }

    @Test
    void testGetMeasurementTypeHistory() {
        QuantityMeasurementDTO dto1 = new QuantityMeasurementDTO();
        dto1.setThisMeasurementType("LengthUnit");
        List<QuantityMeasurementDTO> expected = List.of(dto1);

        when(service.getHistoryByMeasurementType("LengthUnit")).thenReturn(expected);

        List<QuantityMeasurementDTO> result = controller.getMeasurementTypeHistory("LengthUnit");
        assertEquals(1, result.size());
        verify(service, times(1)).getHistoryByMeasurementType("LengthUnit");
    }

    @Test
    void testGetOperationCount() {
        when(service.getCountByOperation("compare")).thenReturn(5L);

        long count = controller.getOperationCount("compare");
        assertEquals(5L, count);
        verify(service, times(1)).getCountByOperation("compare");
    }
}
