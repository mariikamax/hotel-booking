package ru.vsu.cs.maximova.entity;

import java.util.UUID;

public abstract class AbstractEntity {

    private final UUID id;

    protected AbstractEntity() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }
}