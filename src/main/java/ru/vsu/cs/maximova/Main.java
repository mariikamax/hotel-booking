package ru.vsu.cs.maximova;

import ru.vsu.cs.maximova.repository.impl.InMemoryBookingRepository;
import ru.vsu.cs.maximova.repository.impl.InMemoryGuestRepository;
import ru.vsu.cs.maximova.repository.impl.InMemoryRoomRepository;
import ru.vsu.cs.maximova.service.BookingService;
import ru.vsu.cs.maximova.service.GuestService;
import ru.vsu.cs.maximova.service.RoomService;
import ru.vsu.cs.maximova.service.impl.BookingServiceImpl;
import ru.vsu.cs.maximova.service.impl.GuestServiceImpl;
import ru.vsu.cs.maximova.service.impl.RoomServiceImpl;

public class Main {

    public static void main(String[] args) {
        InMemoryRoomRepository roomRepository =
                new InMemoryRoomRepository();

        InMemoryGuestRepository guestRepository =
                new InMemoryGuestRepository();

        InMemoryBookingRepository bookingRepository =
                new InMemoryBookingRepository();

        RoomService roomService =
                new RoomServiceImpl(roomRepository);

        GuestService guestService =
                new GuestServiceImpl(guestRepository);

        BookingService bookingService =
                new BookingServiceImpl(
                        bookingRepository,
                        guestRepository,
                        roomRepository
                );

        ConsoleUI consoleUI = new ConsoleUI(
                roomService,
                guestService,
                bookingService
        );

        consoleUI.start();
    }
}