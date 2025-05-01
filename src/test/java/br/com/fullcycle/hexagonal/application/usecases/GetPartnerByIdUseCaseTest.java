package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.application.inMemoryPartnerRepository;
import br.com.fullcycle.hexagonal.application.entities.Partner;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GetPartnerByIdUseCaseTest {

  @Test
  @DisplayName("Deve obter um parceiro por id")
  public void testGetBy() throws Exception {

    // final var expectedId = UUID.randomUUID().getMostSignificantBits();
    final var expectedCNPJ = "41536538000100";
    final var expectedEmail = "john.doe@gmail.com";
    final var expectedName = "John Doe";

    final var aPartner = Partner.newPartner(expectedName, expectedCNPJ, expectedEmail);

    final var partnerRepository = new inMemoryPartnerRepository();

    final var createdPartner = partnerRepository.create(aPartner);
    final var expectedId = createdPartner.partnerId().value().toString();

    final var input = new GetPartnerByIdUseCase.Input(expectedId);

    // when

    final var usecase = new GetPartnerByIdUseCase(partnerRepository);
    final var output = usecase.execute(input).get();

    // then
    Assertions.assertEquals(expectedId, output.id());
    Assertions.assertEquals(expectedCNPJ, output.cnpj());
    Assertions.assertEquals(expectedEmail, output.email());
    Assertions.assertEquals(expectedName, output.name());
  }

  @Test
  @DisplayName("Deve obter um parceiro não existente")
  public void testGetByPartner() {
    final var expectedId = UUID.randomUUID().toString();

    final var input = new GetPartnerByIdUseCase.Input(expectedId);

    // when
    final var partnerRepository = new inMemoryPartnerRepository();

    final var usecase = new GetPartnerByIdUseCase(partnerRepository);
    final var output = usecase.execute(input);

    Assertions.assertTrue(output.isEmpty());
  }
}
