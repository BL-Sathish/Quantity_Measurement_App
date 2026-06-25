package com.app.quantitymeasurement.services;

import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.repository.IQuantityMeasurementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuantityMeasurementServiceTest {

    private IQuantityMeasurementRepository mockRepository;
    private IQuantityMeasurementService service;

    @BeforeEach
    void setUp() {
        mockRepository = mock(IQuantityMeasurementRepository.class);
        service = new QuantityMeasurementServiceImpl(mockRepository);
    }

    @Test
    void testCompare_Success() {
        QuantityDTO feet = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO inch = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCH);

        boolean result = service.compare(feet, inch);
        assertTrue(result);

        ArgumentCaptor<QuantityMeasurementEntity> captor = ArgumentCaptor.forClass(QuantityMeasurementEntity.class);
        verify(mockRepository, times(1)).save(captor.capture());

        QuantityMeasurementEntity saved = captor.getValue();
        assertNotNull(saved.getId());
        assertEquals(1.0, saved.getOperand1Value());
        assertEquals("FEET", saved.getOperand1Unit());
        assertEquals(12.0, saved.getOperand2Value());
        assertEquals("INCH", saved.getOperand2Unit());
        assertTrue(saved.getResultBoolean());
        assertEquals("comparison", saved.getOperationType());
        assertFalse(saved.getHasError());
    }

    @Test
    void testCompare_IncompatibleCategories() {
        QuantityDTO feet = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO kg = new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM);

        boolean result = service.compare(feet, kg);
        assertFalse(result);

        ArgumentCaptor<QuantityMeasurementEntity> captor = ArgumentCaptor.forClass(QuantityMeasurementEntity.class);
        verify(mockRepository, times(1)).save(captor.capture());

        QuantityMeasurementEntity saved = captor.getValue();
        assertFalse(saved.getResultBoolean());
        assertFalse(saved.getHasError());
    }

    @Test
    void testConvert_Success() {
        QuantityDTO yard = new QuantityDTO(1.0, QuantityDTO.LengthUnit.YARD);
        QuantityDTO result = service.convert(yard, QuantityDTO.LengthUnit.FEET);

        assertEquals(3.0, result.getValue());
        assertEquals(QuantityDTO.LengthUnit.FEET, result.getUnit());

        ArgumentCaptor<QuantityMeasurementEntity> captor = ArgumentCaptor.forClass(QuantityMeasurementEntity.class);
        verify(mockRepository, times(1)).save(captor.capture());

        QuantityMeasurementEntity saved = captor.getValue();
        assertEquals(1.0, saved.getOperand1Value());
        assertEquals("YARD", saved.getOperand1Unit());
        assertEquals(3.0, saved.getResultValue());
        assertEquals("FEET", saved.getResultUnit());
        assertEquals("conversion", saved.getOperationType());
    }

    @Test
    void testAdd_Success() {
        QuantityDTO feet = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO inch = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCH);

        QuantityDTO result = service.add(feet, inch, QuantityDTO.LengthUnit.YARD);
        // 1 foot + 12 inches = 2 feet = 2/3 yards = 0.666666...
        assertEquals(2.0 / 3.0, result.getValue(), 1e-6);
        assertEquals(QuantityDTO.LengthUnit.YARD, result.getUnit());

        ArgumentCaptor<QuantityMeasurementEntity> captor = ArgumentCaptor.forClass(QuantityMeasurementEntity.class);
        verify(mockRepository).save(captor.capture());

        QuantityMeasurementEntity saved = captor.getValue();
        assertEquals(1.0, saved.getOperand1Value());
        assertEquals(12.0, saved.getOperand2Value());
        assertEquals(2.0 / 3.0, saved.getResultValue(), 1e-6);
        assertEquals("YARD", saved.getResultUnit());
        assertEquals("addition", saved.getOperationType());
    }

    @Test
    void testSubtract_Success() {
        QuantityDTO feet = new QuantityDTO(2.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO inch = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCH);

        QuantityDTO result = service.subtract(feet, inch, QuantityDTO.LengthUnit.INCH);
        // 2 feet - 12 inches = 24 inches - 12 inches = 12 inches
        assertEquals(12.0, result.getValue());
        assertEquals(QuantityDTO.LengthUnit.INCH, result.getUnit());
    }

    @Test
    void testDivide_Success() {
        QuantityDTO feet = new QuantityDTO(6.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO yard = new QuantityDTO(1.0, QuantityDTO.LengthUnit.YARD);

        double ratio = service.divide(feet, yard);
        assertEquals(2.0, ratio, 1e-6);
    }

    @Test
    void testTemperatureArithmetic_ThrowsException_AndSavesErrorEntity() {
        QuantityDTO c1 = new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS);
        QuantityDTO c2 = new QuantityDTO(50.0, QuantityDTO.TemperatureUnit.CELSIUS);

        assertThrows(QuantityMeasurementException.class, () -> service.add(c1, c2, null));

        ArgumentCaptor<QuantityMeasurementEntity> captor = ArgumentCaptor.forClass(QuantityMeasurementEntity.class);
        verify(mockRepository).save(captor.capture());

        QuantityMeasurementEntity saved = captor.getValue();
        assertTrue(saved.getHasError());
        assertNotNull(saved.getErrorMessage());
        assertEquals("addition", saved.getOperationType());
    }
}
