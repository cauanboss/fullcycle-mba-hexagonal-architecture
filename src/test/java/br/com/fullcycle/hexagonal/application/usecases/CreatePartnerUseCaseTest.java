package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.integrationTest;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.inMemoryPartnerRepository;
import br.com.fullcycle.hexagonal.application.entities.Partner;
import br.com.fullcycle.hexagonal.infrastructure.repositories.PartnerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CreatePartnerUseCaseTest extends integrationTest {

  // @Autowired
  // private CreatePartnerUseCase usecase;

  // @Autowired
  // private PartnerRepository partnerRepository;

  // @BeforeEach
  // void tearDown() {
  // partnerRepository.deleteAll();
  // }

  @Test
  @DisplayName("Deve Criar um Parceiro")
  public void testCreate() throws Exception {
    // given

    final var expectedCNPJ = "66666538000100";
    final var expectedEmail = "novo.parceiro@gmail.com";
    final var expectedName = "Novo Parceiro";

    final var partnerRepository = new inMemoryPartnerRepository();

    final var createInput = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);

    final var usecase = new CreatePartnerUseCase(partnerRepository);
    final var output = usecase.execute(createInput);

    System.err.println(output);
    Assertions.assertNotNull(output.id());
    Assertions.assertEquals(expectedCNPJ, output.cnpj());
    Assertions.assertEquals(expectedEmail, output.email());
    Assertions.assertEquals(expectedName, output.name());
  }

  @Test
  @DisplayName("Não deve cadastrar um parceiro com CNPJ duplicado")
  public void testCreateWithDuplicatedCNPJShouldFail() throws Exception {

    final var expectedCNPJ = "41536538000100";
    final var expectedEmail = "john.doe@gmail.com";
    final var expectedName = "John Doe";
    final var expectedError = "Partner already exists";
    final var partnerRepository = new inMemoryPartnerRepository();
    final var partner = Partner.newPartner(expectedName, expectedCNPJ, expectedEmail);
    partnerRepository.create(partner);

    final var createInput = new CreatePartnerUseCase.Input(partner.cnpj().value(), partner.email().value(),
        partner.name().value());

    final var usecase = new CreatePartnerUseCase(partnerRepository);
    final var actualExeption = Assertions.assertThrows(ValidationException.class, () -> usecase.execute(createInput));

    // then
    Assertions.assertEquals(expectedError, actualExeption.getMessage());
  }

  @Test
  @DisplayName("Não deve cadastrar um parceiro com e-mail duplicado")
  public void testCreateWithDuplicatedEmailShouldFail() throws Exception {

    final var expectedCNPJ = "41536538000100";
    final var expectedEmail = "john.doe@gmail.com";
    final var expectedName = "John Doe";
    final var expectedError = "Partner already exists";
    final var partnerRepository = new inMemoryPartnerRepository();
    final var partner = Partner.newPartner(expectedName, expectedCNPJ, expectedEmail);
    partnerRepository.create(partner);
    final var createInput = new CreatePartnerUseCase.Input(partner.cnpj().value(), partner.email().value(),
        partner.name().value());

    final var usecase = new CreatePartnerUseCase(partnerRepository);
    final var actualException = Assertions.assertThrows(ValidationException.class, () -> usecase.execute(createInput));

    // then
    Assertions.assertEquals(expectedError, actualException.getMessage());
  }
}
