package ru.vsu.cs.maximova.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.vsu.cs.maximova.entity.Guest;
import ru.vsu.cs.maximova.entity.Room;
import ru.vsu.cs.maximova.entity.RoomType;
import ru.vsu.cs.maximova.ex.ValidationException;
import ru.vsu.cs.maximova.repository.impl.InMemoryBookingRepository;
import ru.vsu.cs.maximova.repository.impl.InMemoryGuestRepository;
import ru.vsu.cs.maximova.repository.impl.InMemoryRoomRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BookingServiceImplTest {

    private BookingServiceImpl bookingService;
    private Guest guest;
    private Room room;

    @BeforeEach
    void setUp() {
        InMemoryBookingRepository bookingRepository =
                new InMemoryBookingRepository();
        InMemoryGuestRepository guestRepository =
                new InMemoryGuestRepository();
        InMemoryRoomRepository roomRepository =
                new InMemoryRoomRepository();

        bookingService = new BookingServiceImpl(
                bookingRepository,
                guestRepository,
                roomRepository
        );

        guest = guestRepository.save(
                new Guest("Иван Иванов", "ivan@example.com", "89991234567")
        );

        room = roomRepository.save(
                new Room(
                        101,
                        RoomType.DOUBLE,
                        new BigDecimal("1000"),
                        2
                )
        );
    }

    @Test
    void shouldCalculateTotalPriceCorrectly() {
        LocalDate checkIn = LocalDate.now().plusDays(10);
        LocalDate checkOut = checkIn.plusDays(3);

        var booking = bookingService.createBooking(
                guest.getId(),
                room.getId(),
                checkIn,
                checkOut,
                2
        );

        assertEquals(
                new BigDecimal("3000"),
                booking.getTotalPrice()
        );
    }

    @Test
    void shouldRejectOverlappingBookings() {
        LocalDate checkIn = LocalDate.now().plusDays(10);

        bookingService.createBooking(
                guest.getId(),
                room.getId(),
                checkIn,
                checkIn.plusDays(3),
                2
        );

        assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking(
                        guest.getId(),
                        room.getId(),
                        checkIn.plusDays(2),
                        checkIn.plusDays(5),
                        1
                )
        );
    }

    @Test
    void shouldRejectTooManyGuests() {
        LocalDate checkIn = LocalDate.now().plusDays(10);

        assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking(
                        guest.getId(),
                        room.getId(),
                        checkIn,
                        checkIn.plusDays(2),
                        3
                )
        );
    }

    @Test
    void shouldRejectInvalidDates() {
        LocalDate checkIn = LocalDate.now().plusDays(10);

        assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking(
                        guest.getId(),
                        room.getId(),
                        checkIn,
                        checkIn.minusDays(1),
                        1
                )
        );
    }
}