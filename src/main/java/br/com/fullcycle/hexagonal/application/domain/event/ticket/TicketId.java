package br.com.fullcycle.hexagonal.application.domain.event.ticket;

import java.util.UUID;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public record TicketId(String value) {

    public TicketId {
        if (value == null) {
            throw new ValidationException("TicketId is required", null);
        }
    }

    public static TicketId unique() {
        return new TicketId(UUID.randomUUID().toString());
    }

    public static TicketId with(String value) {
        try {
            return new TicketId(UUID.fromString(value).toString());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid value for TicketId", null);
        }
    }
}
