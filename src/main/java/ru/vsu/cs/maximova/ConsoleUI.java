package ru.vsu.cs.maximova;

import ru.vsu.cs.maximova.entity.Booking;
import ru.vsu.cs.maximova.entity.Guest;
import ru.vsu.cs.maximova.entity.Room;
import ru.vsu.cs.maximova.entity.RoomType;
import ru.vsu.cs.maximova.ex.AppException;
import ru.vsu.cs.maximova.service.BookingService;
import ru.vsu.cs.maximova.service.GuestService;
import ru.vsu.cs.maximova.service.RoomService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class ConsoleUI {

    private final RoomService roomService;
    private final GuestService guestService;
    private final BookingService bookingService;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleUI(
            RoomService roomService,
            GuestService guestService,
            BookingService bookingService
    ) {
        this.roomService = roomService;
        this.guestService = guestService;
        this.bookingService = bookingService;
    }

    public void start() {
        boolean running = true;

        while (running) {
            printMenu();
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    roomsMenu();
                    break;
                case "2":
                    guestsMenu();
                    break;
                case "3":
                    bookingsMenu();
                    break;
                case "0":
                    running = false;
                    System.out.println("Программа завершена.");
                    break;
                default:
                    System.out.println("Неизвестная команда.");
                    break;
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=== Система бронирования гостиницы ===");
        System.out.println("1. Управление номерами");
        System.out.println("2. Управление гостями");
        System.out.println("3. Управление бронированиями");
        System.out.println("0. Выход");
    }


    private void roomsMenu() {
        boolean running = true;

        while (running) {
            System.out.println("\n=== Управление номерами ===");
            System.out.println("1. Добавить номер");
            System.out.println("2. Показать все номера");
            System.out.println("0. Назад");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addRoom();
                    break;
                case "2":
                    listRooms();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Неизвестная команда.");
                    break;
            }
        }
    }

    private void addRoom() {
        try {
            System.out.print("Введите номер комнаты: ");
            int number = Integer.parseInt(scanner.nextLine().trim());

            System.out.println("Доступные типы номеров:");
            for (RoomType type : RoomType.values()) {
                System.out.println("- " + type);
            }

            System.out.print("Введите тип номера: ");
            RoomType type = RoomType.valueOf(
                    scanner.nextLine().trim().toUpperCase()
            );

            System.out.print("Введите стоимость за ночь: ");
            BigDecimal price = new BigDecimal(
                    scanner.nextLine().trim()
            );

            System.out.print("Введите вместимость: ");
            int capacity = Integer.parseInt(
                    scanner.nextLine().trim()
            );

            Room room = roomService.createRoom(
                    number, type, price, capacity
            );

            System.out.println("Номер успешно добавлен!");
            System.out.println("ID номера: " + room.getId());

        } catch (AppException | IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void listRooms() {
        List<Room> rooms = roomService.getAllRooms();

        if (rooms.isEmpty()) {
            System.out.println("Номера пока не добавлены.");
            return;
        }

        System.out.println("\n=== Список номеров ===");

        for (Room room : rooms) {
            System.out.println(
                    "ID: " + room.getId()
                            + " | Номер: " + room.getNumber()
                            + " | Тип: " + room.getType()
                            + " | Цена за ночь: " + room.getPricePerNight()
                            + " | Вместимость: " + room.getCapacity()
            );
        }
    }


    private void guestsMenu() {
        boolean running = true;

        while (running) {
            System.out.println("\n=== Управление гостями ===");
            System.out.println("1. Зарегистрировать гостя");
            System.out.println("2. Показать всех гостей");
            System.out.println("0. Назад");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addGuest();
                    break;
                case "2":
                    listGuests();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Неизвестная команда.");
                    break;
            }
        }
    }

    private void addGuest() {
        try {
            System.out.print("Введите имя гостя: ");
            String name = scanner.nextLine();

            System.out.print("Введите email: ");
            String email = scanner.nextLine();

            System.out.print("Введите телефон: ");
            String phone = scanner.nextLine();

            Guest guest = guestService.createGuest(
                    name, email, phone
            );

            System.out.println("Гость успешно зарегистрирован!");
            System.out.println("ID гостя: " + guest.getId());

        } catch (AppException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void listGuests() {
        List<Guest> guests = guestService.getAllGuests();

        if (guests.isEmpty()) {
            System.out.println("Зарегистрированных гостей пока нет.");
            return;
        }

        System.out.println("\n=== Список гостей ===");

        for (Guest guest : guests) {
            System.out.println(
                    "ID: " + guest.getId()
                            + " | Имя: " + guest.getName()
                            + " | Email: " + guest.getEmail()
                            + " | Телефон: " + guest.getPhone()
            );
        }
    }


    private void bookingsMenu() {
        boolean running = true;

        while (running) {
            System.out.println("\n=== Управление бронированиями ===");
            System.out.println("1. Создать бронирование");
            System.out.println("2. Показать все бронирования");
            System.out.println("3. Отменить бронирование");
            System.out.println("4. Завершить бронирование");
            System.out.println("0. Назад");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addBooking();
                    break;
                case "2":
                    listBookings();
                    break;
                case "3":
                    cancelBooking();
                    break;
                case "4":
                    completeBooking();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Неизвестная команда.");
                    break;
            }
        }
    }

    private void addBooking() {
        try {
            List<Guest> guests = guestService.getAllGuests();
            List<Room> rooms = roomService.getAllRooms();

            if (guests.isEmpty()) {
                System.out.println(
                        "Сначала зарегистрируйте хотя бы одного гостя."
                );
                return;
            }

            if (rooms.isEmpty()) {
                System.out.println(
                        "Сначала добавьте хотя бы один номер."
                );
                return;
            }

            System.out.println("\n=== Зарегистрированные гости ===");
            for (Guest guest : guests) {
                System.out.println(
                        "ID: " + guest.getId()
                                + " | Имя: " + guest.getName()
                                + " | Email: " + guest.getEmail()
                );
            }

            System.out.print("Введите ID гостя: ");
            UUID guestId = UUID.fromString(
                    scanner.nextLine().trim()
            );

            System.out.println("\n=== Доступные для выбора номера ===");
            for (Room room : rooms) {
                System.out.println(
                        "ID: " + room.getId()
                                + " | Номер: " + room.getNumber()
                                + " | Тип: " + room.getType()
                                + " | Цена за ночь: " + room.getPricePerNight()
                                + " | Вместимость: " + room.getCapacity()
                );
            }

            System.out.print("Введите ID номера: ");
            UUID roomId = UUID.fromString(
                    scanner.nextLine().trim()
            );

            System.out.print("Дата заезда (ГГГГ-ММ-ДД): ");
            LocalDate checkIn = LocalDate.parse(
                    scanner.nextLine().trim()
            );

            System.out.print("Дата выезда (ГГГГ-ММ-ДД): ");
            LocalDate checkOut = LocalDate.parse(
                    scanner.nextLine().trim()
            );

            System.out.print("Количество гостей: ");
            int guestsCount = Integer.parseInt(
                    scanner.nextLine().trim()
            );

            Booking booking = bookingService.createBooking(
                    guestId,
                    roomId,
                    checkIn,
                    checkOut,
                    guestsCount
            );

            System.out.println("\nБронирование успешно создано!");
            System.out.println("ID бронирования: " + booking.getId());
            System.out.println("Стоимость: " + booking.getTotalPrice());
            System.out.println("Статус: " + booking.getStatus());

        } catch (AppException
                 | IllegalArgumentException
                 | DateTimeParseException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void listBookings() {
        List<Booking> bookings = bookingService.getAllBookings();

        if (bookings.isEmpty()) {
            System.out.println("Бронирований пока нет.");
            return;
        }

        System.out.println("\n=== Список бронирований ===");

        for (Booking booking : bookings) {
            System.out.println(
                    "ID: " + booking.getId()
                            + " | Гость: " + booking.getGuest().getName()
                            + " | Номер: " + booking.getRoom().getNumber()
                            + " | Заезд: " + booking.getCheckIn()
                            + " | Выезд: " + booking.getCheckOut()
                            + " | Гостей: " + booking.getGuestsCount()
                            + " | Стоимость: " + booking.getTotalPrice()
                            + " | Статус: " + booking.getStatus()
            );
        }
    }

    private void cancelBooking() {
        try {
            System.out.print("Введите ID бронирования для отмены: ");
            UUID bookingId = UUID.fromString(
                    scanner.nextLine().trim()
            );

            bookingService.cancelBooking(bookingId);

            System.out.println("Бронирование отменено.");

        } catch (AppException | IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void completeBooking() {
        try {
            System.out.print("Введите ID завершённого бронирования: ");
            UUID bookingId = UUID.fromString(
                    scanner.nextLine().trim()
            );

            bookingService.completeBooking(bookingId);

            System.out.println("Бронирование завершено.");

        } catch (AppException | IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}