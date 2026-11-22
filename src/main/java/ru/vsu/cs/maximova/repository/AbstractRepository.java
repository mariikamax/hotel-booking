package ru.vsu.cs.maximova.repository;

import ru.vsu.cs.maximova.entity.AbstractEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AbstractRepository<T extends AbstractEntity> {

    T save(T entity);

    Optional<T> findById(UUID id);

    List<T> findAll();

    boolean deleteById(UUID id);

    boolean existsById(UUID id);
}