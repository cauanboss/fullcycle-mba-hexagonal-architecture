package br.com.fullcycle.hexagonal.application.domain.event;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fullcycle.hexagonal.application.domain.event.Event;
import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public class EventTest {

    @Test
    @DisplayName("Deve criar um evento com sucesso")
    public void testCreateEvent() {
        final var expectedName = "John Doe";
        final var expectedDate = LocalDate.now().toString();
        final var expectedTotalSpots = 100;
        final var expectedCNPJ = "12345678000100";
        final var expectedEmail = "john.doe@gmail.com";

        final var partner = Partner.newPartner(expectedName, expectedCNPJ, expectedEmail);

        final var event = Event.newEvent(expectedName, expectedDate, expectedTotalSpots, partner);

        Assertions.assertNotNull(event);
        Assertions.assertEquals(expectedName, event.name());
        Assertions.assertEquals(expectedDate, event.date().toString());
        Assertions.assertEquals(expectedTotalSpots, event.totalSpots());
        Assertions.assertEquals(partner.partnerId(), event.partnerId());
    }

    @Test
    @DisplayName("Nâo deve criar um evento com um nome invalido ")
    public void testCreateEventWithInvalidName() {
        final var expectedPartnerName = "John Doe";
        final var expectedPartnerCNPJ = "12345678000100";
        final var expectedPartnerEmail = "john.doe@gmail.com";
        final var expectedName = "";
        final var expectedDate = LocalDate.now().toString();
        final var expectedTotalSpots = 100;

        final var partner = Partner.newPartner(expectedPartnerName, expectedPartnerCNPJ,
                expectedPartnerEmail);

        final var actualException = Assertions.assertThrows(ValidationException.class, () -> {
            Event.newEvent(expectedName, expectedDate, expectedTotalSpots, partner);
        });

        Assertions.assertEquals("Invalid value for Name", actualException.getMessage());
    }

    @Test
    @DisplayName("Nâo deve criar um evento com um data invalida")
    public void testCreateEventWithInvalidDate() {
        final var expectedPartnerName = "John Doe";
        final var expectedPartnerCNPJ = "12345678000100";
        final var expectedPartnerEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";
        final var expectedDate = "";
        final var expectedTotalSpots = 100;

        final var partner = Partner.newPartner(expectedPartnerName, expectedPartnerCNPJ,
                expectedPartnerEmail);

        final var actualException = Assertions.assertThrows(ValidationException.class, () -> {
            Event.newEvent(expectedName, expectedDate, expectedTotalSpots, partner);
        });

        Assertions.assertEquals("Invalid value for date", actualException.getMessage());
    }

    @Test
    @DisplayName("Nâo deve criar um evento com um total de spots invalido")
    public void testCreateEventWithInvalidTotalSpots() {
        final var expectedPartnerName = "John Doe";
        final var expectedPartnerCNPJ = "12345678000100";
        final var expectedPartnerEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";
        final var expectedDate = LocalDate.now().toString();
        final Integer expectedTotalSpots = null;

        final var partner = Partner.newPartner(expectedPartnerName, expectedPartnerCNPJ,
                expectedPartnerEmail);

        final var actualException = Assertions.assertThrows(ValidationException.class, () -> {
            Event.newEvent(expectedName, expectedDate, expectedTotalSpots, partner);
        });

        Assertions.assertEquals("Invalid value for totalSpots", actualException.getMessage());
    }
}
