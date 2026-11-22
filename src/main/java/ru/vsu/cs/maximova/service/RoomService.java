package ru.vsu.cs.maximova.service;

import ru.vsu.cs.maximova.entity.Room;
import ru.vsu.cs.maximova.entity.RoomType;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface RoomService {

    Room createRoom(
            int number,
            RoomType type,
            BigDecimal pricePerNight,
            int capacity
    );

    Room getRoom(UUID id);
    List<Room> getAllRooms();
    void deleteRoom(UUID id);
}