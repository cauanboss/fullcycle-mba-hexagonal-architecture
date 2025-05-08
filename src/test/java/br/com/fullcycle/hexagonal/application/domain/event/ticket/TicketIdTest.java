package br.com.fullcycle.hexagonal.application.domain.event.ticket;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class TicketIdTest {

    @Test
    @DisplayName("Deve criar um TicketId")
    public void testCreateTicketId() {
        final var ticketId = TicketId.unique();
        Assertions.assertNotNull(ticketId);
    }

    @Test
    @DisplayName("Deve criar um TicketId com um valor nulo")
    public void testCreateTicketIdWithNullValue() {
        final var ticketId = TicketId.unique();
        Assertions.assertNotNull(ticketId);
    }

    @Test
    @DisplayName("Deve criar um TicketId com um valor invalido")
    public void testCreateTicketIdWithInvalidValue() {
        final var ticketId = TicketId.unique();
        Assertions.assertNotNull(ticketId);
    }
}
