package br.com.fullcycle.hexagonal.application.entities;

import java.util.UUID;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public record CustomerId(UUID value) {

    public CustomerId {
        if (value == null) {
            throw new ValidationException("CustomerId is required", null);
        }
    }

    public static CustomerId unique() {
        return new CustomerId(UUID.randomUUID());
    }

    public static CustomerId with(String value) {
        try {
            return new CustomerId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid value for CustomerId", null);
        }
    }

}
