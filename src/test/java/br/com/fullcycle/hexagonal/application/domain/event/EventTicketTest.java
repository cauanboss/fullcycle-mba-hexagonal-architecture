package br.com.fullcycle.hexagonal.application.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public class EventTicketTest {

    @Test
    @DisplayName("Deve criar um EventTicket")
    public void testCreateEventTicket() {
        final var eventId = EventId.unique();
        final var customerId = CustomerId.unique();
        final var eventTicket = EventTicket.newEventTicket(eventId, customerId, 1);
        Assertions.assertNotNull(eventTicket);
        Assertions.assertEquals(eventId, eventTicket.eventId());
        Assertions.assertEquals(customerId, eventTicket.customerId());
        Assertions.assertEquals(1, eventTicket.ordering());
    }

    @Test
    @DisplayName("Deve criar um EventTicket com um valor nulo")
    public void testCreateEventTicketWithNullValue() {
        final var eventId = EventId.unique();
        final var customerId = CustomerId.unique();
        final var actualException = Assertions.assertThrows(ValidationException.class,
                () -> EventTicket.newEventTicket(eventId, customerId, null));
        Assertions.assertEquals("Ordering is required", actualException.getMessage());
    }

    @Test
    @DisplayName("Deve criar um EventTicket com um valor invalido")
    public void testCreateEventTicketWithInvalidValue() {
        final var eventId = EventId.unique();
        final var customerId = CustomerId.unique();
        final Integer ordering = null;
        final var actualException = Assertions.assertThrows(ValidationException.class,
                () -> EventTicket.newEventTicket(eventId, customerId, ordering));
        Assertions.assertEquals("Ordering is required", actualException.getMessage());
    }
}
