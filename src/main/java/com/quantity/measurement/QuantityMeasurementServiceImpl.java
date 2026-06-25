package com.quantity.measurement;

/**
 * Standard implementation of the IQuantityMeasurementService.
 * Maps DTOs to internal models, delegates operations to Quantity logic,
 * and saves records to IQuantityMeasurementRepository.
 */
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private final IQuantityMeasurementRepository repository;

    /**
     * Dependency Injection via constructor.
     *
     * @param repository the persistence repository
     */
    public QuantityMeasurementServiceImpl(IQuantityMeasurementRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository cannot be null");
        }
        this.repository = repository;
    }

    @Override
    public boolean compare(QuantityDTO op1, QuantityDTO op2) {
        String operationType = "comparison";
        try {
            if (op1 == null || op2 == null) {
                throw new IllegalArgumentException("Operands cannot be null");
            }
            IMeasurable dom1 = mapToDomainUnit(op1.getUnit());
            IMeasurable dom2 = mapToDomainUnit(op2.getUnit());

            // Check if units are of different categories.
            // If they are different, they are not equal, but we do not throw exceptions.
            if (dom1.getClass() != dom2.getClass()) {
                boolean result = false;
                QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                        op1.getValue(), op1.getUnit().name(), op1.getUnit().getMeasurementType(),
                        op2.getValue(), op2.getUnit().name(), op2.getUnit().getMeasurementType(),
                        result, operationType
                );
                repository.save(entity);
                return result;
            }

            @SuppressWarnings({"rawtypes", "unchecked"})
            Quantity q1 = new Quantity(op1.getValue(), dom1);
            @SuppressWarnings({"rawtypes", "unchecked"})
            Quantity q2 = new Quantity(op2.getValue(), dom2);
            boolean result = q1.equals(q2);

            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                    op1.getValue(), op1.getUnit().name(), op1.getUnit().getMeasurementType(),
                    op2.getValue(), op2.getUnit().name(), op2.getUnit().getMeasurementType(),
                    result, operationType
            );
            repository.save(entity);
            return result;
        } catch (Exception e) {
            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(e.getMessage(), operationType);
            repository.save(entity);
            throw new QuantityMeasurementException(e.getMessage(), e);
        }
    }

    @Override
    public QuantityDTO convert(QuantityDTO op, QuantityDTO.IMeasurableUnit targetUnit) {
        String operationType = "conversion";
        try {
            if (op == null || targetUnit == null) {
                throw new IllegalArgumentException("Operand and target unit cannot be null");
            }
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

            QuantityDTO resultDto = new QuantityDTO(converted.getValue(), mapToDtoUnit(converted.getUnit()));

            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                    op.getValue(), op.getUnit().name(), op.getUnit().getMeasurementType(),
                    converted.getValue(), ((Enum<?>) converted.getUnit()).name(), operationType
            );
            repository.save(entity);
            return resultDto;
        } catch (Exception e) {
            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(e.getMessage(), operationType);
            repository.save(entity);
            throw new QuantityMeasurementException(e.getMessage(), e);
        }
    }

    @Override
    public QuantityDTO add(QuantityDTO op1, QuantityDTO op2, QuantityDTO.IMeasurableUnit targetUnit) {
        String operationType = "addition";
        try {
            if (op1 == null || op2 == null) {
                throw new IllegalArgumentException("Operands cannot be null");
            }
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
            QuantityDTO resultDto = new QuantityDTO(sum.getValue(), mapToDtoUnit(sum.getUnit()));

            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                    op1.getValue(), op1.getUnit().name(), op1.getUnit().getMeasurementType(),
                    op2.getValue(), op2.getUnit().name(), op2.getUnit().getMeasurementType(),
                    sum.getValue(), ((Enum<?>) sum.getUnit()).name(), operationType
            );
            repository.save(entity);
            return resultDto;
        } catch (Exception e) {
            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(e.getMessage(), operationType);
            repository.save(entity);
            throw new QuantityMeasurementException(e.getMessage(), e);
        }
    }

    @Override
    public QuantityDTO subtract(QuantityDTO op1, QuantityDTO op2, QuantityDTO.IMeasurableUnit targetUnit) {
        String operationType = "subtraction";
        try {
            if (op1 == null || op2 == null) {
                throw new IllegalArgumentException("Operands cannot be null");
            }
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
            QuantityDTO resultDto = new QuantityDTO(diff.getValue(), mapToDtoUnit(diff.getUnit()));

            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                    op1.getValue(), op1.getUnit().name(), op1.getUnit().getMeasurementType(),
                    op2.getValue(), op2.getUnit().name(), op2.getUnit().getMeasurementType(),
                    diff.getValue(), ((Enum<?>) diff.getUnit()).name(), operationType
            );
            repository.save(entity);
            return resultDto;
        } catch (Exception e) {
            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(e.getMessage(), operationType);
            repository.save(entity);
            throw new QuantityMeasurementException(e.getMessage(), e);
        }
    }

    @Override
    public double divide(QuantityDTO op1, QuantityDTO op2) {
        String operationType = "division";
        try {
            if (op1 == null || op2 == null) {
                throw new IllegalArgumentException("Operands cannot be null");
            }
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
                    op1.getValue(), op1.getUnit().name(), op1.getUnit().getMeasurementType(),
                    op2.getValue(), op2.getUnit().name(), op2.getUnit().getMeasurementType(),
                    ratio, "Dimensionless", operationType
            );
            repository.save(entity);
            return ratio;
        } catch (Exception e) {
            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(e.getMessage(), operationType);
            repository.save(entity);
            throw new QuantityMeasurementException(e.getMessage(), e);
        }
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

    private QuantityDTO.IMeasurableUnit mapToDtoUnit(IMeasurable domainUnit) {
        if (domainUnit == null) {
            return null;
        }
        String name = ((Enum<?>) domainUnit).name();
        if (domainUnit instanceof LengthUnit) {
            return QuantityDTO.LengthUnit.valueOf(name);
        } else if (domainUnit instanceof WeightUnit) {
            return QuantityDTO.WeightUnit.valueOf(name);
        } else if (domainUnit instanceof VolumeUnit) {
            return QuantityDTO.VolumeUnit.valueOf(name);
        } else if (domainUnit instanceof TemperatureUnit) {
            return QuantityDTO.TemperatureUnit.valueOf(name);
        }
        throw new IllegalArgumentException("Unsupported domain unit: " + domainUnit.getClass().getName());
    }
}
