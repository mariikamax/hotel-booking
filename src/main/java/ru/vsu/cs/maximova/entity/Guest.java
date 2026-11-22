package ru.vsu.cs.maximova.entity;
import java.util.UUID;

public class Guest extends AbstractEntity {

    private final String name;
    private final String email;
    private final String phone;

    public Guest(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Guest(UUID id, String name, String email, String phone) {
        super(id);
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
}