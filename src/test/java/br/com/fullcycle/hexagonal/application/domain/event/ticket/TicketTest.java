package br.com.fullcycle.hexagonal.application.domain.event.ticket;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.event.EventId;

public class TicketTest {

    @Test
    @DisplayName("Deve criar um Ticket")
    public void testCreateTicket() {
        final var eventId = EventId.unique();
        final var customerId = CustomerId.unique();
        final var ticket = Ticket.newTicket(eventId, customerId);
        Assertions.assertNotNull(ticket);
        Assertions.assertEquals(eventId, ticket.eventId());
        Assertions.assertEquals(customerId, ticket.customerId());
    }
}