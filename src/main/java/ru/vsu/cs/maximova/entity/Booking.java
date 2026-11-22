package ru.vsu.cs.maximova.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Booking extends AbstractEntity {

    private final Guest guest;
    private final Room room;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private final int guestsCount;
    private final BigDecimal totalPrice;
    private BookingStatus status;

    public Booking(
            Guest guest,
            Room room,
            LocalDate checkIn,
            LocalDate checkOut,
            int guestsCount,
            BigDecimal totalPrice
    ) {
        this.guest = guest;
        this.room = room;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.guestsCount = guestsCount;
        this.totalPrice = totalPrice;
        this.status = BookingStatus.ACTIVE;
    }

    public Guest getGuest() {
        return guest;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public int getGuestsCount() {
        return guestsCount;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}