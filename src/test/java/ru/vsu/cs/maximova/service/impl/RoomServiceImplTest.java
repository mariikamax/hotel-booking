
package ru.vsu.cs.maximova.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.vsu.cs.maximova.entity.Room;
import ru.vsu.cs.maximova.entity.RoomType;
import ru.vsu.cs.maximova.ex.ValidationException;
import ru.vsu.cs.maximova.repository.impl.InMemoryRoomRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class RoomServiceImplTest {

    private RoomServiceImpl roomService;

    @BeforeEach
    void setUp() {
        roomService = new RoomServiceImpl(
                new InMemoryRoomRepository()
        );
    }

    @Test
    void shouldCreateRoomWithValidData() {
        Room room = roomService.createRoom(
                101,
                RoomType.DOUBLE,
                new BigDecimal("1500"),
                2
        );

        assertNotNull(room);
        assertEquals(101, room.getNumber());
        assertEquals(new BigDecimal("1500"), room.getPricePerNight());
        assertEquals(2, room.getCapacity());
    }

    @Test
    void shouldRejectDuplicateRoomNumber() {
        roomService.createRoom(
                101,
                RoomType.DOUBLE,
                new BigDecimal("1500"),
                2
        );

        assertThrows(
                ValidationException.class,
                () -> roomService.createRoom(
                        101,
                        RoomType.SINGLE,
                        new BigDecimal("1000"),
                        1
                )
        );
    }

    @Test
    void shouldRejectNonPositivePrice() {
        assertThrows(
                ValidationException.class,
                () -> roomService.createRoom(
                        102,
                        RoomType.SINGLE,
                        BigDecimal.ZERO,
                        1
                )
        );
    }

    @Test
    void shouldRejectInvalidCapacity() {
        assertThrows(
                ValidationException.class,
                () -> roomService.createRoom(
                        103,
                        RoomType.DOUBLE,
                        new BigDecimal("1500"),
                        0
                )
        );
    }
}
