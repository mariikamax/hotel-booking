package ru.vsu.cs.maximova.entity;

import java.util.UUID;

public abstract class AbstractEntity {

    private final UUID id;

    protected AbstractEntity() {
        this(UUID.randomUUID());
    }

    protected AbstractEntity(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}