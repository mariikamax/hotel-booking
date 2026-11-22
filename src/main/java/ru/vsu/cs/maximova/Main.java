
package ru.vsu.cs.maximova;

import ru.vsu.cs.maximova.config.DatabaseConfig;
import ru.vsu.cs.maximova.config.DatabaseMigration;
import ru.vsu.cs.maximova.repository.jdbc.JdbcBookingRepository;
import ru.vsu.cs.maximova.repository.jdbc.JdbcGuestRepository;
import ru.vsu.cs.maximova.repository.jdbc.JdbcRoomRepository;
import ru.vsu.cs.maximova.service.BookingService;
import ru.vsu.cs.maximova.service.GuestService;
import ru.vsu.cs.maximova.service.RoomService;
import ru.vsu.cs.maximova.service.impl.BookingServiceImpl;
import ru.vsu.cs.maximova.service.impl.GuestServiceImpl;
import ru.vsu.cs.maximova.service.impl.RoomServiceImpl;

import javax.sql.DataSource;

public class Main {

    public static void main(String[] args) {
        DataSource dataSource = DatabaseConfig.createDataSource();

        DatabaseMigration.migrate(dataSource);

        JdbcRoomRepository roomRepository =
                new JdbcRoomRepository(dataSource);

        JdbcGuestRepository guestRepository =
                new JdbcGuestRepository(dataSource);

        JdbcBookingRepository bookingRepository =
                new JdbcBookingRepository(
                        dataSource,
                        guestRepository,
                        roomRepository
                );

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
