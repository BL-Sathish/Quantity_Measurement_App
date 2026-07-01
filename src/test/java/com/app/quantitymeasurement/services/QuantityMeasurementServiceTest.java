package com.app.quantitymeasurement.services;

import com.app.quantitymeasurement.model.*;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuantityMeasurementServiceTest {

    @Mock
    private QuantityMeasurementRepository repository;

    @InjectMocks
    private QuantityMeasurementServiceImpl service;

    @Test
    void testCompare_Success() {
        QuantityDTO feet = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO inch = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCH);

        when(repository.save(any(QuantityMeasurementEntity.class))).thenAnswer(invocation -> {
            QuantityMeasurementEntity entity = invocation.getArgument(0);
            entity.setId(1L);
            return entity;
        });

        QuantityMeasurementDTO result = service.compare(feet, inch);
        assertNotNull(result);
        assertEquals("true", result.getResultString());
        assertEquals("compare", result.getOperation());

        ArgumentCaptor<QuantityMeasurementEntity> captor = ArgumentCaptor.forClass(QuantityMeasurementEntity.class);
        verify(repository, times(1)).save(captor.capture());

        QuantityMeasurementEntity saved = captor.getValue();
        assertEquals(1.0, saved.getThisValue());
        assertEquals("FEET", saved.getThisUnit());
        assertEquals(12.0, saved.getThatValue());
        assertEquals("INCH", saved.getThatUnit());
        assertEquals("true", saved.getResultString());
        assertFalse(saved.getError());
    }

    @Test
    void testCompare_IncompatibleCategories() {
        QuantityDTO feet = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO kg = new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM);

        when(repository.save(any(QuantityMeasurementEntity.class))).thenAnswer(invocation -> {
            QuantityMeasurementEntity entity = invocation.getArgument(0);
            entity.setId(1L);
            return entity;
        });

        QuantityMeasurementDTO result = service.compare(feet, kg);
        assertNotNull(result);
        assertEquals("false", result.getResultString());
        assertFalse(result.getError());
    }

    @Test
    void testConvert_Success() {
        QuantityDTO yard = new QuantityDTO(1.0, QuantityDTO.LengthUnit.YARD);

        when(repository.save(any(QuantityMeasurementEntity.class))).thenAnswer(invocation -> {
            QuantityMeasurementEntity entity = invocation.getArgument(0);
            entity.setId(1L);
            return entity;
        });

        QuantityMeasurementDTO result = service.convert(yard, QuantityDTO.LengthUnit.FEET);
        assertNotNull(result);
        assertEquals(3.0, result.getResultValue());
        assertEquals("FEET", result.getResultUnit());
        assertEquals("convert", result.getOperation());
    }

    @Test
    void testAdd_Success() {
        QuantityDTO feet = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO inch = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCH);

        when(repository.save(any(QuantityMeasurementEntity.class))).thenAnswer(invocation -> {
            QuantityMeasurementEntity entity = invocation.getArgument(0);
            entity.setId(1L);
            return entity;
        });

        QuantityMeasurementDTO result = service.add(feet, inch, QuantityDTO.LengthUnit.YARD);
        assertNotNull(result);
        assertEquals(2.0 / 3.0, result.getResultValue(), 1e-6);
        assertEquals("YARD", result.getResultUnit());
        assertEquals("add", result.getOperation());
    }

    @Test
    void testSubtract_Success() {
        QuantityDTO feet = new QuantityDTO(2.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO inch = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCH);

        when(repository.save(any(QuantityMeasurementEntity.class))).thenAnswer(invocation -> {
            QuantityMeasurementEntity entity = invocation.getArgument(0);
            entity.setId(1L);
            return entity;
        });

        QuantityMeasurementDTO result = service.subtract(feet, inch, QuantityDTO.LengthUnit.INCH);
        assertNotNull(result);
        assertEquals(12.0, result.getResultValue());
        assertEquals("INCH", result.getResultUnit());
    }

    @Test
    void testDivide_Success() {
        QuantityDTO feet = new QuantityDTO(6.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO yard = new QuantityDTO(1.0, QuantityDTO.LengthUnit.YARD);

        when(repository.save(any(QuantityMeasurementEntity.class))).thenAnswer(invocation -> {
            QuantityMeasurementEntity entity = invocation.getArgument(0);
            entity.setId(1L);
            return entity;
        });

        QuantityMeasurementDTO result = service.divide(feet, yard);
        assertNotNull(result);
        assertEquals(2.0, result.getResultValue(), 1e-6);
    }

    @Test
    void testTemperatureArithmetic_ThrowsException_AndSavesErrorEntity() {
        QuantityDTO c1 = new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS);
        QuantityDTO c2 = new QuantityDTO(50.0, QuantityDTO.TemperatureUnit.CELSIUS);

        assertThrows(QuantityMeasurementException.class, () -> service.add(c1, c2, null));

        ArgumentCaptor<QuantityMeasurementEntity> captor = ArgumentCaptor.forClass(QuantityMeasurementEntity.class);
        verify(repository).save(captor.capture());

        QuantityMeasurementEntity saved = captor.getValue();
        assertTrue(saved.getError());
        assertNotNull(saved.getErrorMessage());
        assertEquals("add", saved.getOperation());
    }
}
