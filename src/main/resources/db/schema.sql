-- Table for storing quantity measurement entities
CREATE TABLE IF NOT EXISTS quantity_measurement_entity (
    id VARCHAR(255) PRIMARY KEY,
    operand1_value DOUBLE,
    operand1_unit VARCHAR(55),
    operand1_type VARCHAR(55),
    operand2_value DOUBLE,
    operand2_unit VARCHAR(55),
    operand2_type VARCHAR(55),
    operation_type VARCHAR(55),
    result_value DOUBLE,
    result_boolean BOOLEAN,
    result_unit VARCHAR(55),
    has_error BOOLEAN,
    error_message VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table for storing audit logs/history
CREATE TABLE IF NOT EXISTS quantity_measurement_history (
    history_id INT AUTO_INCREMENT PRIMARY KEY,
    entity_id VARCHAR(255),
    action_type VARCHAR(55),
    performed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (entity_id) REFERENCES quantity_measurement_entity(id) ON DELETE CASCADE
);
