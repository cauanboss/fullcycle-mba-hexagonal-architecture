package br.com.fullcycle.hexagonal.application.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.com.fullcycle.hexagonal.application.inMemoryEventRepository;
import br.com.fullcycle.hexagonal.application.inMemoryPartnerRepository;
import br.com.fullcycle.hexagonal.application.entities.Partner;
import br.com.fullcycle.hexagonal.application.entities.PartnerId;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CreateEventUseCaseTest {

  @Test
  @DisplayName("Deve criar um evento")
  public void testCreate() throws Exception {
    final var expectedName = "Disney on Ice";
    final var expectedDate = "2021-01-01";
    final var expectedTotalSpots = 10;
    final var expectedPartnerId = PartnerId.unique();

    final var expectedCNPJ = "12345678901234";
    final var expectedEmail = "test@test.com";

    final var eventRepository = new inMemoryEventRepository();
    final var partnerRepository = new inMemoryPartnerRepository();

    partnerRepository.create(new Partner(expectedPartnerId, expectedName, expectedCNPJ, expectedEmail));

    final var createInput = new CreateEventUseCase.Input(
        expectedName, expectedDate, expectedTotalSpots, expectedPartnerId.value());

    final var createEventUseCase = new CreateEventUseCase(eventRepository, partnerRepository);
    final var output = createEventUseCase.execute(createInput);

    // then
    assertThat(output.id()).isNotNull();
    assertThat(output.name()).isEqualTo(expectedName);
    assertThat(output.date()).isEqualTo(expectedDate);
    assertThat(output.totalSpots()).isEqualTo(expectedTotalSpots);
    assertThat(output.partnerId()).isEqualTo(expectedPartnerId.value());
  }

  @Test
  @DisplayName("Nâo deve criar evento com parceiro não encontrado")
  public void testCreateEvent_whenPartnerDoesNotExist_shouldThrowError() throws Exception {
    // given
    final var expectedName = "Disney on Ice";
    final var expectedDate = "2021-01-01";
    final var expectedTotalSpots = 10;
    final var expectedPartnerId = PartnerId.unique();
    final var expectedErrorMessage = "Partner not found";

    final var createInput = new CreateEventUseCase.Input(
        expectedName, expectedDate, expectedTotalSpots, expectedPartnerId.value());
    // when
    final var eventRepository = new inMemoryEventRepository();
    final var partnerRepository = new inMemoryPartnerRepository();

    final var createEventUseCase = new CreateEventUseCase(eventRepository, partnerRepository);
    final var output = assertThrows(ValidationException.class, () -> createEventUseCase.execute(createInput));

    assertThat(output.getMessage()).isEqualTo(expectedErrorMessage);
  }
}
