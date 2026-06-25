package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Singleton repository implementing IQuantityMeasurementRepository using an in-memory ArrayList.
 * Backing store is serialized to measurements_cache.ser.
 * Enhanced for UC16 with additional query methods.
 */
public class QuantityMeasurementCacheRepository implements IQuantityMeasurementRepository {
    private static final String FILE_NAME = "measurements_cache.ser";
    private static QuantityMeasurementCacheRepository instance;
    private final List<QuantityMeasurementEntity> cache;

    private QuantityMeasurementCacheRepository() {
        this.cache = Collections.synchronizedList(new ArrayList<>());
        loadFromDisk();
    }

    public static synchronized QuantityMeasurementCacheRepository getInstance() {
        if (instance == null) {
            instance = new QuantityMeasurementCacheRepository();
        }
        return instance;
    }

    public static synchronized void resetInstance() {
        instance = null;
    }

    @Override
    public synchronized void save(QuantityMeasurementEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        cache.add(entity);
        saveToDisk(entity);
    }

    @Override
    public synchronized List<QuantityMeasurementEntity> getAllMeasurements() {
        return new ArrayList<>(cache);
    }

    @Override
    public synchronized List<QuantityMeasurementEntity> getMeasurementsByOperationType(String operationType) {
        if (operationType == null) {
            return new ArrayList<>();
        }
        return cache.stream()
                .filter(e -> operationType.equals(e.getOperationType()))
                .collect(Collectors.toList());
    }

    @Override
    public synchronized QuantityMeasurementEntity getMeasurementById(String id) {
        if (id == null) {
            return null;
        }
        return cache.stream()
                .filter(e -> id.equals(e.getId()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public synchronized long getCount() {
        return cache.size();
    }

    private void loadFromDisk() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            while (true) {
                try {
                    QuantityMeasurementEntity entity = (QuantityMeasurementEntity) ois.readObject();
                    cache.add(entity);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load measurements from disk cache: " + e.getMessage());
        }
    }

    private void saveToDisk(QuantityMeasurementEntity entity) {
        File file = new File(FILE_NAME);
        boolean append = file.exists() && file.length() > 0;
        
        try (FileOutputStream fos = new FileOutputStream(file, append)) {
            if (append) {
                try (AppendableObjectOutputStream aoos = new AppendableObjectOutputStream(fos)) {
                    aoos.writeObject(entity);
                    aoos.flush();
                }
            } else {
                try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                    oos.writeObject(entity);
                    oos.flush();
                }
            }
        } catch (IOException e) {
            throw new QuantityMeasurementException("Failed to save measurement to disk: " + e.getMessage(), e);
        }
    }

    @Override
    public synchronized void clear() {
        cache.clear();
        File file = new File(FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
    }
}
