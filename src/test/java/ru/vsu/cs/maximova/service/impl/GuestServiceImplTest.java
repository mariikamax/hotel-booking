package ru.vsu.cs.maximova.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.vsu.cs.maximova.entity.Guest;
import ru.vsu.cs.maximova.ex.ValidationException;
import ru.vsu.cs.maximova.repository.impl.InMemoryGuestRepository;

import static org.junit.jupiter.api.Assertions.*;

class GuestServiceImplTest {

    private GuestServiceImpl guestService;

    @BeforeEach
    void setUp() {
        guestService = new GuestServiceImpl(
                new InMemoryGuestRepository()
        );
    }

    @Test
    void shouldCreateGuestWithValidData() {
        Guest guest = guestService.createGuest(
                "Иван Иванов",
                "ivan@example.com",
                "+79991234567"
        );

        assertNotNull(guest);
        assertEquals("Иван Иванов", guest.getName());
        assertEquals("ivan@example.com", guest.getEmail());
    }

    @Test
    void shouldRejectInvalidEmail() {
        assertThrows(
                ValidationException.class,
                () -> guestService.createGuest(
                        "Иван Иванов",
                        "invalid-email",
                        "+79991234567"
                )
        );
    }

    @Test
    void shouldRejectInvalidPhone() {
        assertThrows(
                ValidationException.class,
                () -> guestService.createGuest(
                        "Иван Иванов",
                        "ivan@example.com",
                        "123"
                )
        );
    }

    @Test
    void shouldRejectDuplicateEmail() {
        guestService.createGuest(
                "Иван Иванов",
                "ivan@example.com",
                "+79991234567"
        );

        assertThrows(
                ValidationException.class,
                () -> guestService.createGuest(
                        "Пётр Петров",
                        "IVAN@example.com",
                        "+79997654321"
                )
        );
    }
}