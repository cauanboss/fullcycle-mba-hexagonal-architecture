package br.com.fullcycle.hexagonal.application.usecases.partner;

import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;
import br.com.fullcycle.hexagonal.integrationTest;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class GetPartnerByIdUseCaseITTest extends integrationTest {

  @Autowired private GetPartnerByIdUseCase usecase;

  @Autowired private PartnerRepository partnerRepository;

  @BeforeEach
  void tearDown() {
    partnerRepository.deleteAll();
  }

  @Test
  @DisplayName("Deve obter um parceiro por id")
  public void testGetBy() throws Exception {

    final var expectedCNPJ = "41536538000100";
    final var expectedEmail = "john.doe@gmail.com";
    final var expectedName = "John Doe";
    final var partner = savePartner(expectedCNPJ, expectedEmail, expectedName);
    final var expectedId = partner.partnerId();
    final var input = new GetPartnerByIdUseCase.Input(expectedId);
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

    final var output = usecase.execute(input);

    Assertions.assertTrue(output.isEmpty());
  }

  private Partner savePartner(final String cnpj, final String email, final String name) {
    return partnerRepository.create(Partner.newPartner(name, cnpj, email));
  }
}
