package ru.vsu.cs.maximova.service;

import ru.vsu.cs.maximova.entity.Guest;

import java.util.List;
import java.util.UUID;

public interface GuestService {
    Guest createGuest(String name, String email, String phone);
    Guest getGuest(UUID id);
    List<Guest> getAllGuests();
    void deleteGuest(UUID id);
}