package ru.vsu.cs.maximova.entity;

import java.math.BigDecimal;

public class Room extends AbstractEntity {

    private final int number;
    private final RoomType type;
    private final BigDecimal pricePerNight;
    private final int capacity;

    public Room(
            int number,
            RoomType type,
            BigDecimal pricePerNight,
            int capacity
    ) {
        this.number = number;
        this.type = type;
        this.pricePerNight = pricePerNight;
        this.capacity = capacity;
    }

    public int getNumber() {
        return number;
    }

    public RoomType getType() {
        return type;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public int getCapacity() {
        return capacity;
    }
}