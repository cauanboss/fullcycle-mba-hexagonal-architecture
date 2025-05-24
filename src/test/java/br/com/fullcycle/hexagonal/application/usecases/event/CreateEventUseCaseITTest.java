package br.com.fullcycle.hexagonal.application.usecases.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.domain.partner.PartnerId;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.repositories.EventRepository;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;
import br.com.fullcycle.hexagonal.integrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CreateEventUseCaseITTest extends integrationTest {

  @Autowired private CreateEventUseCase usecase;

  @Autowired private EventRepository eventRepository;

  @Autowired private PartnerRepository partnerRepository;

  @BeforeEach
  void tearDown() {
    eventRepository.deleteAll();
    partnerRepository.deleteAll();
  }

  @Test
  @DisplayName("Deve criar um evento")
  public void testCreate() throws Exception {
    // given
    final var partner = savePartner("41536538000100", "john.doe@gmail.com", "John Doe");
    final var expectedName = "Disney on Ice";
    final var expectedDate = "2021-01-01";
    final var expectedTotalSpots = 10;

    final var createInput =
        new CreateEventUseCase.Input(
            expectedName, expectedDate, expectedTotalSpots, partner.partnerId());
    // when

    final var output = usecase.execute(createInput);

    // then
    assertThat(output.id()).isNotNull();
    assertThat(output.name()).isEqualTo(expectedName);
    assertThat(output.date()).isEqualTo(expectedDate);
    assertThat(output.totalSpots()).isEqualTo(expectedTotalSpots);
    assertThat(output.partnerId()).isEqualTo(partner.partnerId());
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

    final var createInput =
        new CreateEventUseCase.Input(
            expectedName, expectedDate, expectedTotalSpots, expectedPartnerId.value());
    // when

    final var output = assertThrows(ValidationException.class, () -> usecase.execute(createInput));

    assertThat(output.getMessage()).isEqualTo(expectedErrorMessage);
  }

  private Partner savePartner(final String cnpj, final String email, final String name) {
    return partnerRepository.create(Partner.newPartner(name, cnpj, email));
  }
}
