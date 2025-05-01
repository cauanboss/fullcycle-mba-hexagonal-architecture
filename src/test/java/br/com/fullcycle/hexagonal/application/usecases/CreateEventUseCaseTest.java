package br.com.fullcycle.hexagonal.application.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.infrastructure.models.Event;
import br.com.fullcycle.hexagonal.infrastructure.models.Partner;
import br.com.fullcycle.hexagonal.infrastructure.services.EventService;
import br.com.fullcycle.hexagonal.infrastructure.services.PartnerService;
import io.hypersistence.tsid.TSID;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CreateEventUseCaseTest {

  @Test
  @DisplayName("Deve criar um evento")
  public void testCreate() throws Exception {
    // given
    final var expectedName = "Disney on Ice";
    final var expectedDate = "2021-01-01";
    final var expectedTotalSpots = 10;
    final var expectedPartnerId = TSID.fast().toLong();

    final var createInput =
        new CreateEventUseCase.Input(
            expectedName, expectedDate, expectedTotalSpots, expectedPartnerId);
    // when
    final var eventService = Mockito.mock(EventService.class);
    final var partnerService = Mockito.mock(PartnerService.class);

    when(eventService.save(any()))
        .thenAnswer(
            a -> {
              final var event = a.getArgument(0, Event.class);
              event.setId(TSID.fast().toLong());
              return event;
            });

    when(partnerService.findById(eq(expectedPartnerId))).thenReturn(Optional.of(new Partner()));

    final var createEventUseCase = new CreateEventUseCase(eventService, partnerService);
    final var output = createEventUseCase.execute(createInput);

    // then
    assertThat(output.id()).isNotNull();
    assertThat(output.name()).isEqualTo(expectedName);
    assertThat(output.date()).isEqualTo(expectedDate);
    assertThat(output.totalSpots()).isEqualTo(expectedTotalSpots);
    assertThat(output.partnerId()).isEqualTo(expectedPartnerId);
  }

  @Test
  @DisplayName("Nâo deve criar evento com parceiro não encontrado")
  public void testCreateEvent_whenPartnerDoesNotExist_shouldThrowError() throws Exception {
    // given
    final var expectedName = "Disney on Ice";
    final var expectedDate = "2021-01-01";
    final var expectedTotalSpots = 10;
    final var expectedPartnerId = TSID.fast().toLong();
    final var expectedErrorMessage = "Partner not found";

    final var createInput =
        new CreateEventUseCase.Input(
            expectedName, expectedDate, expectedTotalSpots, expectedPartnerId);
    // when
    final var eventService = Mockito.mock(EventService.class);
    final var partnerService = Mockito.mock(PartnerService.class);

    when(partnerService.findById(eq(expectedPartnerId))).thenReturn(Optional.empty());

    final var createEventUseCase = new CreateEventUseCase(eventService, partnerService);
    final var output =
        assertThrows(ValidationException.class, () -> createEventUseCase.execute(createInput));

    assertThat(output.getMessage()).isEqualTo(expectedErrorMessage);
  }
}
