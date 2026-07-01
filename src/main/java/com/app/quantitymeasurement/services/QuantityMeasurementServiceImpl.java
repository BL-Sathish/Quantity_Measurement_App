package com.app.quantitymeasurement.services;

import com.app.quantitymeasurement.model.*;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;
import com.app.quantitymeasurement.unit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Modernized Spring Service implementing IQuantityMeasurementService.
 * Integrates with Spring Data JPA repository and performs core calculations.
 */
@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private static final Logger LOGGER = Logger.getLogger(QuantityMeasurementServiceImpl.class.getName());

    @Autowired
    private QuantityMeasurementRepository repository;

    @Override
    public QuantityMeasurementDTO compare(QuantityDTO op1, QuantityDTO op2) {
        String operation = "compare";
        try {
            if (op1 == null || op2 == null) {
                throw new IllegalArgumentException("Operands cannot be null");
            }
            LOGGER.fine("Service: comparing " + op1 + " and " + op2);

            IMeasurable dom1 = mapToDomainUnit(op1.getUnit());
            IMeasurable dom2 = mapToDomainUnit(op2.getUnit());

            // Check if units are of different categories
            if (dom1.getClass() != dom2.getClass()) {
                QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                        op1.getValue(), op1.getUnitName(), op1.getMeasurementType(),
                        op2.getValue(), op2.getUnitName(), op2.getMeasurementType(),
                        operation, "false", false
                );
                QuantityMeasurementEntity saved = repository.save(entity);
                return QuantityMeasurementDTO.fromEntity(saved);
            }

            @SuppressWarnings({"rawtypes", "unchecked"})
            Quantity q1 = new Quantity(op1.getValue(), dom1);
            @SuppressWarnings({"rawtypes", "unchecked"})
            Quantity q2 = new Quantity(op2.getValue(), dom2);
            boolean result = q1.equals(q2);

            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                    op1.getValue(), op1.getUnitName(), op1.getMeasurementType(),
                    op2.getValue(), op2.getUnitName(), op2.getMeasurementType(),
                    operation, String.valueOf(result), false
            );
            QuantityMeasurementEntity saved = repository.save(entity);
            return QuantityMeasurementDTO.fromEntity(saved);

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Comparison failed: " + e.getMessage(), e);
            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(e.getMessage(), operation, true);
            repository.save(entity);
            throw new QuantityMeasurementException(e.getMessage(), e);
        }
    }

    @Override
    public QuantityMeasurementDTO convert(QuantityDTO op, QuantityDTO.IMeasurableUnit targetUnit) {
        String operation = "convert";
        try {
            if (op == null || targetUnit == null) {
                throw new IllegalArgumentException("Operand and target unit cannot be null");
            }
            LOGGER.fine("Service: converting " + op + " to " + targetUnit.name());

            IMeasurable sourceDom = mapToDomainUnit(op.getUnit());
            IMeasurable targetDom = mapToDomainUnit(targetUnit);

            if (sourceDom.getClass() != targetDom.getClass()) {
                throw new IllegalArgumentException("Cannot convert between different unit types: "
                        + sourceDom.getMeasurementType() + " to " + targetDom.getMeasurementType());
            }

            @SuppressWarnings({"rawtypes", "unchecked"})
            Quantity q = new Quantity(op.getValue(), sourceDom);
            @SuppressWarnings("unchecked")
            Quantity converted = q.convertTo(targetDom);

            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                    op.getValue(), op.getUnitName(), op.getMeasurementType(),
                    0.0, targetUnit.name(), targetUnit.getMeasurementType() + "Unit",
                    operation, converted.getValue(), targetUnit.name(), targetUnit.getMeasurementType() + "Unit", false
            );
            QuantityMeasurementEntity saved = repository.save(entity);
            return QuantityMeasurementDTO.fromEntity(saved);

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Conversion failed: " + e.getMessage(), e);
            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(e.getMessage(), operation, true);
            repository.save(entity);
            throw new QuantityMeasurementException(e.getMessage(), e);
        }
    }

    @Override
    public QuantityMeasurementDTO add(QuantityDTO op1, QuantityDTO op2, QuantityDTO.IMeasurableUnit targetUnit) {
        String operation = "add";
        try {
            if (op1 == null || op2 == null) {
                throw new IllegalArgumentException("Operands cannot be null");
            }
            LOGGER.fine("Service: adding " + op1 + " + " + op2);

            IMeasurable dom1 = mapToDomainUnit(op1.getUnit());
            IMeasurable dom2 = mapToDomainUnit(op2.getUnit());
            IMeasurable targetDom = targetUnit != null ? mapToDomainUnit(targetUnit) : dom1;

            if (dom1.getClass() != dom2.getClass()) {
                throw new IllegalArgumentException("Cannot perform addition on different unit types: "
                        + dom1.getMeasurementType() + " and " + dom2.getMeasurementType());
            }
            if (targetDom.getClass() != dom1.getClass()) {
                throw new IllegalArgumentException("Target unit must match operand unit type");
            }

            @SuppressWarnings({"rawtypes", "unchecked"})
            Quantity q1 = new Quantity(op1.getValue(), dom1);
            @SuppressWarnings({"rawtypes", "unchecked"})
            Quantity q2 = new Quantity(op2.getValue(), dom2);

            @SuppressWarnings("unchecked")
            Quantity sum = Quantity.add(q1, q2, targetDom);

            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                    op1.getValue(), op1.getUnitName(), op1.getMeasurementType(),
                    op2.getValue(), op2.getUnitName(), op2.getMeasurementType(),
                    operation, sum.getValue(), ((Enum<?>) sum.getUnit()).name(),
                    sum.getUnit().getMeasurementType() + "Unit", null, false
            );
            QuantityMeasurementEntity saved = repository.save(entity);
            return QuantityMeasurementDTO.fromEntity(saved);

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Addition failed: " + e.getMessage(), e);
            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(e.getMessage(), operation, true);
            repository.save(entity);
            throw new QuantityMeasurementException(e.getMessage(), e);
        }
    }

    @Override
    public QuantityMeasurementDTO subtract(QuantityDTO op1, QuantityDTO op2, QuantityDTO.IMeasurableUnit targetUnit) {
        String operation = "subtract";
        try {
            if (op1 == null || op2 == null) {
                throw new IllegalArgumentException("Operands cannot be null");
            }
            LOGGER.fine("Service: subtracting " + op1 + " - " + op2);

            IMeasurable dom1 = mapToDomainUnit(op1.getUnit());
            IMeasurable dom2 = mapToDomainUnit(op2.getUnit());
            IMeasurable targetDom = targetUnit != null ? mapToDomainUnit(targetUnit) : dom1;

            if (dom1.getClass() != dom2.getClass()) {
                throw new IllegalArgumentException("Cannot perform subtraction on different unit types: "
                        + dom1.getMeasurementType() + " and " + dom2.getMeasurementType());
            }
            if (targetDom.getClass() != dom1.getClass()) {
                throw new IllegalArgumentException("Target unit must match operand unit type");
            }

            @SuppressWarnings({"rawtypes", "unchecked"})
            Quantity q1 = new Quantity(op1.getValue(), dom1);
            @SuppressWarnings({"rawtypes", "unchecked"})
            Quantity q2 = new Quantity(op2.getValue(), dom2);

            @SuppressWarnings("unchecked")
            Quantity diff = q1.subtract(q2, targetDom);

            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                    op1.getValue(), op1.getUnitName(), op1.getMeasurementType(),
                    op2.getValue(), op2.getUnitName(), op2.getMeasurementType(),
                    operation, diff.getValue(), ((Enum<?>) diff.getUnit()).name(),
                    diff.getUnit().getMeasurementType() + "Unit", null, false
            );
            QuantityMeasurementEntity saved = repository.save(entity);
            return QuantityMeasurementDTO.fromEntity(saved);

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Subtraction failed: " + e.getMessage(), e);
            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(e.getMessage(), operation, true);
            repository.save(entity);
            throw new QuantityMeasurementException(e.getMessage(), e);
        }
    }

    @Override
    public QuantityMeasurementDTO divide(QuantityDTO op1, QuantityDTO op2) {
        String operation = "divide";
        try {
            if (op1 == null || op2 == null) {
                throw new IllegalArgumentException("Operands cannot be null");
            }
            LOGGER.fine("Service: dividing " + op1 + " / " + op2);

            IMeasurable dom1 = mapToDomainUnit(op1.getUnit());
            IMeasurable dom2 = mapToDomainUnit(op2.getUnit());

            if (dom1.getClass() != dom2.getClass()) {
                throw new IllegalArgumentException("Cannot perform division on different unit types: "
                        + dom1.getMeasurementType() + " and " + dom2.getMeasurementType());
            }

            @SuppressWarnings({"rawtypes", "unchecked"})
            Quantity q1 = new Quantity(op1.getValue(), dom1);
            @SuppressWarnings({"rawtypes", "unchecked"})
            Quantity q2 = new Quantity(op2.getValue(), dom2);

            double ratio = q1.divide(q2);

            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                    op1.getValue(), op1.getUnitName(), op1.getMeasurementType(),
                    op2.getValue(), op2.getUnitName(), op2.getMeasurementType(),
                    operation, ratio, "Dimensionless", "Dimensionless", null, false
            );
            QuantityMeasurementEntity saved = repository.save(entity);
            return QuantityMeasurementDTO.fromEntity(saved);

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Division failed: " + e.getMessage(), e);
            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(e.getMessage(), operation, true);
            repository.save(entity);
            throw new QuantityMeasurementException(e.getMessage(), e);
        }
    }

    @Override
    public List<QuantityMeasurementDTO> getHistoryByOperation(String operation) {
        List<QuantityMeasurementEntity> list = repository.findByOperation(operation.toLowerCase());
        return QuantityMeasurementDTO.fromEntityList(list);
    }

    @Override
    public List<QuantityMeasurementDTO> getHistoryByMeasurementType(String measurementType) {
        List<QuantityMeasurementEntity> list = repository.findByThisMeasurementType(measurementType);
        return QuantityMeasurementDTO.fromEntityList(list);
    }

    @Override
    public long getCountByOperation(String operation) {
        return repository.countByOperationAndErrorFalse(operation.toLowerCase());
    }

    private IMeasurable mapToDomainUnit(QuantityDTO.IMeasurableUnit dtoUnit) {
        if (dtoUnit == null) {
            throw new IllegalArgumentException("DTO Unit cannot be null");
        }
        if (dtoUnit instanceof QuantityDTO.LengthUnit) {
            return LengthUnit.valueOf(dtoUnit.name());
        } else if (dtoUnit instanceof QuantityDTO.WeightUnit) {
            return WeightUnit.valueOf(dtoUnit.name());
        } else if (dtoUnit instanceof QuantityDTO.VolumeUnit) {
            return VolumeUnit.valueOf(dtoUnit.name());
        } else if (dtoUnit instanceof QuantityDTO.TemperatureUnit) {
            return TemperatureUnit.valueOf(dtoUnit.name());
        }
        throw new IllegalArgumentException("Unsupported unit type: " + dtoUnit.getClass().getName());
    }
}
