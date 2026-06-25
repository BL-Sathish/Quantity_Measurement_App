package com.app.quantitymeasurement;

import com.app.quantitymeasurement.controller.QuantityMeasurementController;
import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.app.quantitymeasurement.repository.QuantityMeasurementDatabaseRepository;
import com.app.quantitymeasurement.services.IQuantityMeasurementService;
import com.app.quantitymeasurement.services.QuantityMeasurementServiceImpl;
import com.app.quantitymeasurement.util.ConnectionPool;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementIntegrationTest {

    private ConnectionPool connectionPool;
    private IQuantityMeasurementRepository repository;
    private IQuantityMeasurementService service;
    private QuantityMeasurementController controller;

    @BeforeEach
    void setUp() {
        // Create dedicated connection pool and repository for integration tests
        connectionPool = new ConnectionPool(
                "jdbc:h2:mem:integration_db;DB_CLOSE_DELAY=-1;MODE=MySQL",
                "sa",
                "",
                2,
                5
        );
        repository = new QuantityMeasurementDatabaseRepository(connectionPool);
        service = new QuantityMeasurementServiceImpl(repository);
        controller = new QuantityMeasurementController(service);
    }

    @AfterEach
    void tearDown() {
        repository.clear();
        connectionPool.shutdown();
    }

    @Test
    void testFullLifecyle_ComparisonConversionAddition() {
        // 1. Comparison
        QuantityDTO feet1 = new QuantityDTO(2.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO inch24 = new QuantityDTO(24.0, QuantityDTO.LengthUnit.INCH);

        boolean comparisonResult = controller.performComparison(feet1, inch24);
        assertTrue(comparisonResult);

        // Verify save occurred
        assertEquals(1, repository.getCount());

        // 2. Conversion
        QuantityDTO kg5 = new QuantityDTO(5.0, QuantityDTO.WeightUnit.KILOGRAM);
        QuantityDTO conversionResult = controller.performConversion(kg5, QuantityDTO.WeightUnit.GRAM);
        assertEquals(5000.0, conversionResult.getValue());

        // Verify save occurred
        assertEquals(2, repository.getCount());

        // 3. Addition
        QuantityDTO litre2 = new QuantityDTO(2.0, QuantityDTO.VolumeUnit.LITRE);
        QuantityDTO ml500 = new QuantityDTO(500.0, QuantityDTO.VolumeUnit.MILLILITRE);
        QuantityDTO additionResult = controller.performAddition(litre2, ml500, QuantityDTO.VolumeUnit.LITRE);
        assertEquals(2.5, additionResult.getValue());

        // Verify save occurred
        assertEquals(3, repository.getCount());

        // Verify database list and filter features
        List<QuantityMeasurementEntity> all = repository.getAllMeasurements();
        assertEquals(3, all.size());

        List<QuantityMeasurementEntity> additions = repository.getMeasurementsByOperationType("addition");
        assertEquals(1, additions.size());
        assertEquals("addition", additions.get(0).getOperationType());
        assertEquals(2.0, additions.get(0).getOperand1Value());
        assertEquals(500.0, additions.get(0).getOperand2Value());
        assertEquals(2.5, additions.get(0).getResultValue());
        assertEquals("LITRE", additions.get(0).getResultUnit());
    }
}
