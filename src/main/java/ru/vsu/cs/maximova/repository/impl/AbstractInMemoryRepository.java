package ru.vsu.cs.maximova.repository.impl;

import ru.vsu.cs.maximova.entity.AbstractEntity;
import ru.vsu.cs.maximova.ex.ValidationException;
import ru.vsu.cs.maximova.repository.AbstractRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractInMemoryRepository<T extends AbstractEntity>
        implements AbstractRepository<T> {

    private final Map<UUID, T> entities = new HashMap<>();

    @Override
    public T save(T entity) {
        if (entity == null) {
            throw new ValidationException("Нельзя сохранить пустую сущность");
        }

        entities.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<T> findById(UUID id) {
        if (id == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(entities.values());
    }

    @Override
    public boolean deleteById(UUID id) {
        if (id == null) {
            return false;
        }

        return entities.remove(id) != null;
    }

    @Override
    public boolean existsById(UUID id) {
        return id != null && entities.containsKey(id);
    }
}