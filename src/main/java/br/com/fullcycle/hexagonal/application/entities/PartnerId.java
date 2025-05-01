package br.com.fullcycle.hexagonal.application.entities;

import java.util.UUID;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public record PartnerId(UUID value) {
    public static PartnerId unique() {
        return new PartnerId(UUID.randomUUID());
    }

    public static PartnerId with(String value) {
        try {
            return new PartnerId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid value for PartnerId", null);
        }
    }

}
