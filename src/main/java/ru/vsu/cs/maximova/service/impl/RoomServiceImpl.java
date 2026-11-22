package ru.vsu.cs.maximova.service.impl;

import ru.vsu.cs.maximova.entity.Room;
import ru.vsu.cs.maximova.entity.RoomType;
import ru.vsu.cs.maximova.ex.EntityNotFoundException;
import ru.vsu.cs.maximova.ex.ValidationException;
import ru.vsu.cs.maximova.repository.AbstractRepository;
import ru.vsu.cs.maximova.service.RoomService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class RoomServiceImpl implements RoomService {

    private final AbstractRepository<Room> roomRepository;
    public RoomServiceImpl(AbstractRepository<Room> roomRepository) {
        if (roomRepository == null) {
            throw new IllegalArgumentException(
                    "Репозиторий номеров не может быть null"
            );
        }

        this.roomRepository = roomRepository;
    }

    @Override
    public Room createRoom(
            int number,
            RoomType type,
            BigDecimal pricePerNight,
            int capacity
    ) {
        validateRoom(number, type, pricePerNight, capacity);

        boolean roomExists = roomRepository.findAll()
                .stream()
                .anyMatch(room -> room.getNumber() == number);

        if (roomExists) {
            throw new ValidationException(
                    "Номер комнаты " + number + " уже существует"
            );
        }

        Room room = new Room(number, type, pricePerNight, capacity);

        return roomRepository.save(room);
    }

    @Override
    public Room getRoom(UUID id) {
        if (id == null) {
            throw new ValidationException("ID номера не может быть null");
        }

        return roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Номер с ID " + id + " не найден"
                ));
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public void deleteRoom(UUID id) {
        if (id == null) {
            throw new ValidationException("ID номера не может быть null");
        }

        boolean deleted = roomRepository.deleteById(id);

        if (!deleted) {
            throw new EntityNotFoundException(
                    "Номер с ID " + id + " не найден"
            );
        }
    }

    private void validateRoom(
            int number,
            RoomType type,
            BigDecimal pricePerNight,
            int capacity
    ) {
        if (number <= 0) {
            throw new ValidationException(
                    "Номер комнаты должен быть положительным"
            );
        }

        if (type == null) {
            throw new ValidationException(
                    "Тип номера не может быть null"
            );
        }

        if (pricePerNight == null
                || pricePerNight.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(
                    "Стоимость номера должна быть положительной"
            );
        }

        if (capacity <= 0) {
            throw new ValidationException(
                    "Вместимость номера должна быть положительной"
            );
        }
    }
}