package ru.vsu.cs.maximova.service.impl;

import ru.vsu.cs.maximova.entity.Guest;
import ru.vsu.cs.maximova.ex.EntityNotFoundException;
import ru.vsu.cs.maximova.ex.ValidationException;
import ru.vsu.cs.maximova.repository.AbstractRepository;
import ru.vsu.cs.maximova.service.GuestService;
import ru.vsu.cs.maximova.util.StringUtils;

import java.util.List;
import java.util.UUID;

public class GuestServiceImpl implements GuestService {

    private final AbstractRepository<Guest> guestRepository;

    public GuestServiceImpl(AbstractRepository<Guest> guestRepository) {
        if (guestRepository == null) {
            throw new IllegalArgumentException(
                    "Репозиторий гостей не может быть null"
            );
        }
        this.guestRepository = guestRepository;
    }

    @Override
    public Guest createGuest(String name, String email, String phone) {
        validateGuest(name, email, phone);

        boolean emailExists = guestRepository.findAll().stream()
                .anyMatch(guest -> guest.getEmail().equalsIgnoreCase(email.trim()));

        if (emailExists) {
            throw new ValidationException(
                    "Гость с таким email уже существует"
            );
        }

        Guest guest = new Guest(name.trim(), email.trim(), phone.trim());
        return guestRepository.save(guest);
    }

    @Override
    public Guest getGuest(UUID id) {
        if (id == null) {
            throw new ValidationException("ID гостя не может быть null");
        }

        return guestRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Гость с ID " + id + " не найден"
                        )
                );
    }

    @Override
    public List<Guest> getAllGuests() {
        return guestRepository.findAll();
    }

    @Override
    public void deleteGuest(UUID id) {
        if (id == null) {
            throw new ValidationException("ID гостя не может быть null");
        }

        boolean deleted = guestRepository.deleteById(id);

        if (!deleted) {
            throw new EntityNotFoundException(
                    "Гость с ID " + id + " не найден"
            );
        }
    }

    private void validateGuest(String name, String email, String phone) {
        if (StringUtils.isNullOrBlank(name)) {
            throw new ValidationException("Имя гостя не может быть пустым");
        }

        if (StringUtils.isNullOrBlank(email)) {
            throw new ValidationException("Email не может быть пустым");
        }

        if (!email.trim().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new ValidationException("Некорректный формат email");
        }

        if (StringUtils.isNullOrBlank(phone)) {
            throw new ValidationException("Телефон не может быть пустым");
        }
    }
}