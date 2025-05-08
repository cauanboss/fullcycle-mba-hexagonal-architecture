package br.com.fullcycle.hexagonal.application.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public class EventIdTest {

    @Test
    @DisplayName("Deve criar um EventId")
    public void testCreateEventId() {
        final var expectedEventId = "1234567890";
        final var actualException = Assertions.assertThrows(ValidationException.class,
                () -> EventId.with(expectedEventId));
        Assertions.assertEquals("Invalid value for EventId", actualException.getMessage());
    }

    @Test
    @DisplayName("Deve criar um EventId com um valor nulo")
    public void testCreateEventIdWithNullValue() {
        final var eventId = EventId.unique();
        Assertions.assertNotNull(eventId);
    }

    @Test
    @DisplayName("Deve criar um EventId com um valor invalido")
    public void testCreateEventIdWithInvalidValue() {
        final var expectedEventId = "1234567890";
        final var actualException = Assertions.assertThrows(ValidationException.class,
                () -> EventId.with(expectedEventId));
        Assertions.assertEquals("Invalid value for EventId", actualException.getMessage());
    }
}
