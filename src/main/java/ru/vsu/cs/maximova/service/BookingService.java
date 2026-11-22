package ru.vsu.cs.maximova.service;

import ru.vsu.cs.maximova.entity.Booking;
import ru.vsu.cs.maximova.entity.BookingStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BookingService {

    Booking createBooking(
            UUID guestId,
            UUID roomId,
            LocalDate checkIn,
            LocalDate checkOut,
            int guestsCount
    );

    Booking getBooking(UUID id);

    List<Booking> getAllBookings();

    List<Booking> getBookingsByGuest(UUID guestId);

    void cancelBooking(UUID id);

    void completeBooking(UUID id);
}