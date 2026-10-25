package ru.vsu.cs.maximova;

import ru.vsu.cs.maximova.entity.Room;
import ru.vsu.cs.maximova.entity.RoomType;
import ru.vsu.cs.maximova.ex.AppException;
import ru.vsu.cs.maximova.service.BookingService;
import ru.vsu.cs.maximova.service.GuestService;
import ru.vsu.cs.maximova.service.RoomService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

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
                    System.out.println(
                            "Управление гостями будет добавлено следующим этапом."
                    );
                    break;

                case "3":
                    System.out.println(
                            "Управление бронированиями будет добавлено следующим этапом."
                    );
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
            BigDecimal price = new BigDecimal(scanner.nextLine().trim());

            System.out.print("Введите вместимость: ");
            int capacity = Integer.parseInt(scanner.nextLine().trim());

            Room room = roomService.createRoom(
                    number,
                    type,
                    price,
                    capacity
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
}