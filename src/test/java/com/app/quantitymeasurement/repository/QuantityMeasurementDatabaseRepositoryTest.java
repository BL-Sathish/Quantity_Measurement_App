package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.util.ConnectionPool;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementDatabaseRepositoryTest {

    private ConnectionPool connectionPool;
    private QuantityMeasurementDatabaseRepository repository;

    @BeforeEach
    void setUp() {
        // Use an in-memory database specific to this test
        connectionPool = new ConnectionPool(
                "jdbc:h2:mem:test_repo_db;DB_CLOSE_DELAY=-1;MODE=MySQL",
                "sa",
                "",
                2,
                5
        );
        repository = new QuantityMeasurementDatabaseRepository(connectionPool);
    }

    @AfterEach
    void tearDown() {
        repository.clear();
        connectionPool.shutdown();
    }

    @Test
    void testSaveAndRetrieveAll() {
        QuantityMeasurementEntity entity1 = new QuantityMeasurementEntity(
                12.0, "INCH", "Length",
                1.0, "FEET", "Length",
                true, "comparison"
        );
        entity1.setId("test-uuid-1");

        QuantityMeasurementEntity entity2 = new QuantityMeasurementEntity(
                1.0, "FEET", "Length",
                12.0, "INCH", "Length",
                12.0, "INCH", "conversion"
        );
        entity2.setId("test-uuid-2");

        repository.save(entity1);
        repository.save(entity2);

        List<QuantityMeasurementEntity> all = repository.getAllMeasurements();
        assertEquals(2, all.size());
        assertEquals(2, repository.getCount());

        QuantityMeasurementEntity retrieved1 = repository.getMeasurementById("test-uuid-1");
        assertNotNull(retrieved1);
        assertEquals("INCH", retrieved1.getOperand1Unit());
        assertEquals("FEET", retrieved1.getOperand2Unit());
        assertTrue(retrieved1.getResultBoolean());
        assertEquals("comparison", retrieved1.getOperationType());

        QuantityMeasurementEntity retrieved2 = repository.getMeasurementById("test-uuid-2");
        assertNotNull(retrieved2);
        assertEquals(12.0, retrieved2.getResultValue());
        assertEquals("conversion", retrieved2.getOperationType());
    }

    @Test
    void testGetMeasurementsByOperationType() {
        QuantityMeasurementEntity entity1 = new QuantityMeasurementEntity(
                12.0, "INCH", "Length",
                1.0, "FEET", "Length",
                true, "comparison"
        );
        entity1.setId("uuid-c1");

        QuantityMeasurementEntity entity2 = new QuantityMeasurementEntity(
                1.0, "FEET", "Length",
                12.0, "INCH", "Length",
                12.0, "INCH", "conversion"
        );
        entity2.setId("uuid-c2");

        repository.save(entity1);
        repository.save(entity2);

        List<QuantityMeasurementEntity> comparisons = repository.getMeasurementsByOperationType("comparison");
        assertEquals(1, comparisons.size());
        assertEquals("uuid-c1", comparisons.get(0).getId());

        List<QuantityMeasurementEntity> conversions = repository.getMeasurementsByOperationType("conversion");
        assertEquals(1, conversions.size());
        assertEquals("uuid-c2", conversions.get(0).getId());
    }

    @Test
    void testAuditHistoryTriggeredOnSave() throws Exception {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                100.0, "CELSIUS", "Temperature",
                212.0, "FAHRENHEIT", "Temperature",
                true, "comparison"
        );
        entity.setId("uuid-audit");

        repository.save(entity);

        // Directly query quantity_measurement_history to verify record insertion
        Connection conn = connectionPool.borrowConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM quantity_measurement_history WHERE entity_id = 'uuid-audit'")) {
            assertTrue(rs.next());
            assertEquals("INSERT", rs.getString("action_type"));
            assertNotNull(rs.getTimestamp("performed_at"));
        } finally {
            connectionPool.returnConnection(conn);
        }
    }

    @Test
    void testClearAll() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                1.0, "FEET", "Length",
                12.0, "INCH", "Length",
                true, "comparison"
        );
        entity.setId("uuid-clear");
        repository.save(entity);
        assertEquals(1, repository.getCount());

        repository.clear();
        assertEquals(0, repository.getCount());
        assertTrue(repository.getAllMeasurements().isEmpty());
    }

    @Test
    void testGetMeasurementById_NotFound() {
        assertNull(repository.getMeasurementById("invalid-id"));
    }
}
