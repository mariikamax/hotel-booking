package ru.vsu.cs.maximova;

import ru.vsu.cs.maximova.service.BookingService;
import ru.vsu.cs.maximova.service.GuestService;
import ru.vsu.cs.maximova.service.RoomService;

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
                    System.out.println(
                            "Раздел управления номерами будет добавлен следующим этапом."
                    );
                    break;

                case "2":
                    System.out.println(
                            "Раздел управления гостями будет добавлен следующим этапом."
                    );
                    break;

                case "3":
                    System.out.println(
                            "Раздел бронирования будет добавлен следующим этапом."
                    );
                    break;

                case "0":
                    running = false;
                    System.out.println("Программа завершена.");
                    break;

                default:
                    System.out.println(
                            "Неизвестная команда. Попробуйте ещё раз."
                    );
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
}