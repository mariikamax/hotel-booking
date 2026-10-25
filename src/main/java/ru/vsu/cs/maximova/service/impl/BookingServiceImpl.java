package ru.vsu.cs.maximova.service.impl;

import ru.vsu.cs.maximova.entity.Booking;
import ru.vsu.cs.maximova.entity.BookingStatus;
import ru.vsu.cs.maximova.entity.Guest;
import ru.vsu.cs.maximova.entity.Room;
import ru.vsu.cs.maximova.ex.EntityNotFoundException;
import ru.vsu.cs.maximova.ex.ValidationException;
import ru.vsu.cs.maximova.repository.AbstractRepository;
import ru.vsu.cs.maximova.service.BookingService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BookingServiceImpl implements BookingService {

    private final AbstractRepository<Booking> bookingRepository;
    private final AbstractRepository<Guest> guestRepository;
    private final AbstractRepository<Room> roomRepository;

    public BookingServiceImpl(
            AbstractRepository<Booking> bookingRepository,
            AbstractRepository<Guest> guestRepository,
            AbstractRepository<Room> roomRepository
    ) {
        if (bookingRepository == null
                || guestRepository == null
                || roomRepository == null) {
            throw new IllegalArgumentException(
                    "Репозитории не могут быть null"
            );
        }

        this.bookingRepository = bookingRepository;
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public Booking createBooking(
            UUID guestId,
            UUID roomId,
            LocalDate checkIn,
            LocalDate checkOut,
            int guestsCount
    ) {
        if (guestId == null || roomId == null) {
            throw new ValidationException(
                    "ID гостя и номера не могут быть null"
            );
        }

        if (checkIn == null || checkOut == null) {
            throw new ValidationException(
                    "Даты заезда и выезда обязательны"
            );
        }

        if (!checkOut.isAfter(checkIn)) {
            throw new ValidationException(
                    "Дата выезда должна быть позже даты заезда"
            );
        }

        if (checkIn.isBefore(LocalDate.now())) {
            throw new ValidationException(
                    "Дата заезда не может быть в прошлом"
            );
        }

        if (guestsCount <= 0) {
            throw new ValidationException(
                    "Количество гостей должно быть положительным"
            );
        }

        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Гость с ID " + guestId + " не найден"
                ));

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Номер с ID " + roomId + " не найден"
                ));

        if (guestsCount > room.getCapacity()) {
            throw new ValidationException(
                    "Количество гостей превышает вместимость номера"
            );
        }

        boolean overlaps = bookingRepository.findAll().stream()
                .filter(booking -> booking.getRoom().getId().equals(roomId))
                .filter(booking -> booking.getStatus() == BookingStatus.ACTIVE)
                .anyMatch(booking ->
                        checkIn.isBefore(booking.getCheckOut())
                                && checkOut.isAfter(booking.getCheckIn())
                );

        if (overlaps) {
            throw new ValidationException(
                    "Номер уже забронирован на выбранные даты"
            );
        }

        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);

        BigDecimal totalPrice = room.getPricePerNight()
                .multiply(BigDecimal.valueOf(nights));

        Booking booking = new Booking(
                guest,
                room,
                checkIn,
                checkOut,
                guestsCount,
                totalPrice
        );

        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBooking(UUID id) {
        if (id == null) {
            throw new ValidationException(
                    "ID бронирования не может быть null"
            );
        }

        return bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Бронирование с ID " + id + " не найдено"
                ));
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public List<Booking> getBookingsByGuest(UUID guestId) {
        if (guestId == null) {
            throw new ValidationException(
                    "ID гостя не может быть null"
            );
        }

        return bookingRepository.findAll().stream()
                .filter(booking ->
                        booking.getGuest().getId().equals(guestId)
                )
                .collect(Collectors.toList());
    }

    @Override
    public void cancelBooking(UUID id) {
        Booking booking = getBooking(id);

        if (booking.getStatus() != BookingStatus.ACTIVE) {
            throw new ValidationException(
                    "Отменить можно только активное бронирование"
            );
        }

        booking.setStatus(BookingStatus.CANCELLED);
    }

    @Override
    public void completeBooking(UUID id) {
        Booking booking = getBooking(id);

        if (booking.getStatus() != BookingStatus.ACTIVE) {
            throw new ValidationException(
                    "Завершить можно только активное бронирование"
            );
        }

        booking.setStatus(BookingStatus.COMPLETED);
    }
}