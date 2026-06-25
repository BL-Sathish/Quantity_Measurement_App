package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.exception.DatabaseException;
import com.app.quantitymeasurement.util.ConnectionPool;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * JDBC-based repository implementation for persisting quantity measurement entities
 * to an H2 database. Uses parameterized PreparedStatements and the custom ConnectionPool.
 */
public class QuantityMeasurementDatabaseRepository implements IQuantityMeasurementRepository {

    private static final Logger LOGGER = Logger.getLogger(QuantityMeasurementDatabaseRepository.class.getName());
    private static final String SCHEMA_FILE = "db/schema.sql";

    private final ConnectionPool connectionPool;

    /**
     * Creates a new database repository with the given connection pool.
     * Automatically initializes the database schema.
     *
     * @param connectionPool the connection pool to use
     */
    public QuantityMeasurementDatabaseRepository(ConnectionPool connectionPool) {
        if (connectionPool == null) {
            throw new IllegalArgumentException("ConnectionPool cannot be null");
        }
        this.connectionPool = connectionPool;
        initializeSchema();
    }

    /**
     * Initializes the database schema by executing the SQL script from resources.
     */
    private void initializeSchema() {
        String sql = loadSchemaScript();
        if (sql == null || sql.trim().isEmpty()) {
            LOGGER.warning("Schema SQL is empty. Skipping schema initialization.");
            return;
        }

        Connection conn = null;
        try {
            conn = connectionPool.borrowConnection();
            Statement stmt = conn.createStatement();
            // Split by semicolons and execute each statement
            String[] statements = sql.split(";");
            for (String s : statements) {
                String trimmed = s.trim();
                if (!trimmed.isEmpty()) {
                    stmt.execute(trimmed);
                }
            }
            stmt.close();
            LOGGER.info("Database schema initialized successfully.");
        } catch (SQLException e) {
            throw new DatabaseException("Failed to initialize database schema: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                connectionPool.returnConnection(conn);
            }
        }
    }

    private String loadSchemaScript() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(SCHEMA_FILE)) {
            if (is == null) {
                LOGGER.warning("Schema file not found: " + SCHEMA_FILE);
                return null;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            throw new DatabaseException("Failed to load schema script: " + e.getMessage(), e);
        }
    }

    // ─────────────────────────────────────────────
    // CRUD Operations
    // ─────────────────────────────────────────────

    @Override
    public void save(QuantityMeasurementEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        String sql = "INSERT INTO quantity_measurement_entity " +
                "(id, operand1_value, operand1_unit, operand1_type, operand2_value, operand2_unit, operand2_type, " +
                "operation_type, result_value, result_boolean, result_unit, has_error, error_message) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = connectionPool.borrowConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, entity.getId());
                setNullableDouble(ps, 2, entity.getOperand1Value());
                ps.setString(3, entity.getOperand1Unit());
                ps.setString(4, entity.getOperand1Type());
                setNullableDouble(ps, 5, entity.getOperand2Value());
                ps.setString(6, entity.getOperand2Unit());
                ps.setString(7, entity.getOperand2Type());
                ps.setString(8, entity.getOperationType());
                setNullableDouble(ps, 9, entity.getResultValue());
                setNullableBoolean(ps, 10, entity.getResultBoolean());
                ps.setString(11, entity.getResultUnit());
                setNullableBoolean(ps, 12, entity.getHasError());
                ps.setString(13, entity.getErrorMessage());

                ps.executeUpdate();
                LOGGER.fine("Saved entity: " + entity.getId());
            }

            // Also insert audit history
            saveHistory(conn, entity.getId(), "INSERT");

        } catch (SQLException e) {
            throw new DatabaseException("Failed to save entity: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                connectionPool.returnConnection(conn);
            }
        }
    }

    private void saveHistory(Connection conn, String entityId, String actionType) throws SQLException {
        String sql = "INSERT INTO quantity_measurement_history (entity_id, action_type) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entityId);
            ps.setString(2, actionType);
            ps.executeUpdate();
        }
    }

    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {
        String sql = "SELECT * FROM quantity_measurement_entity ORDER BY created_at DESC";
        List<QuantityMeasurementEntity> results = new ArrayList<>();

        Connection conn = null;
        try {
            conn = connectionPool.borrowConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRowToEntity(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve all measurements: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                connectionPool.returnConnection(conn);
            }
        }
        return results;
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByOperationType(String operationType) {
        if (operationType == null) {
            return new ArrayList<>();
        }
        String sql = "SELECT * FROM quantity_measurement_entity WHERE operation_type = ? ORDER BY created_at DESC";
        List<QuantityMeasurementEntity> results = new ArrayList<>();

        Connection conn = null;
        try {
            conn = connectionPool.borrowConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, operationType);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        results.add(mapRowToEntity(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve measurements by type: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                connectionPool.returnConnection(conn);
            }
        }
        return results;
    }

    @Override
    public QuantityMeasurementEntity getMeasurementById(String id) {
        if (id == null) {
            return null;
        }
        String sql = "SELECT * FROM quantity_measurement_entity WHERE id = ?";

        Connection conn = null;
        try {
            conn = connectionPool.borrowConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapRowToEntity(rs);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve measurement by ID: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                connectionPool.returnConnection(conn);
            }
        }
        return null;
    }

    @Override
    public long getCount() {
        String sql = "SELECT COUNT(*) FROM quantity_measurement_entity";
        Connection conn = null;
        try {
            conn = connectionPool.borrowConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to get count: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                connectionPool.returnConnection(conn);
            }
        }
        return 0;
    }

    @Override
    public void clear() {
        Connection conn = null;
        try {
            conn = connectionPool.borrowConnection();
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("DELETE FROM quantity_measurement_history");
                stmt.execute("DELETE FROM quantity_measurement_entity");
            }
            LOGGER.info("All measurements cleared from database.");
        } catch (SQLException e) {
            throw new DatabaseException("Failed to clear measurements: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                connectionPool.returnConnection(conn);
            }
        }
    }

    // ─────────────────────────────────────────────
    // Result mapping helpers
    // ─────────────────────────────────────────────

    private QuantityMeasurementEntity mapRowToEntity(ResultSet rs) throws SQLException {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setId(rs.getString("id"));
        entity.setOperand1Value(getNullableDouble(rs, "operand1_value"));
        entity.setOperand1Unit(rs.getString("operand1_unit"));
        entity.setOperand1Type(rs.getString("operand1_type"));
        entity.setOperand2Value(getNullableDouble(rs, "operand2_value"));
        entity.setOperand2Unit(rs.getString("operand2_unit"));
        entity.setOperand2Type(rs.getString("operand2_type"));
        entity.setOperationType(rs.getString("operation_type"));
        entity.setResultValue(getNullableDouble(rs, "result_value"));
        entity.setResultBoolean(getNullableBoolean(rs, "result_boolean"));
        entity.setResultUnit(rs.getString("result_unit"));
        entity.setHasError(getNullableBoolean(rs, "has_error"));
        entity.setErrorMessage(rs.getString("error_message"));
        return entity;
    }

    private void setNullableDouble(PreparedStatement ps, int index, Double value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.DOUBLE);
        } else {
            ps.setDouble(index, value);
        }
    }

    private void setNullableBoolean(PreparedStatement ps, int index, Boolean value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.BOOLEAN);
        } else {
            ps.setBoolean(index, value);
        }
    }

    private Double getNullableDouble(ResultSet rs, String column) throws SQLException {
        double value = rs.getDouble(column);
        return rs.wasNull() ? null : value;
    }

    private Boolean getNullableBoolean(ResultSet rs, String column) throws SQLException {
        boolean value = rs.getBoolean(column);
        return rs.wasNull() ? null : value;
    }
}
