package br.com.fullcycle.hexagonal.application.usecases.partner;

import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.repository.inMemoryPartnerRepository;
import br.com.fullcycle.hexagonal.application.usecases.partner.CreatePartnerUseCase;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CreatePartnerUseCaseITTest {

  @Test
  @DisplayName("Deve Criar um Parceiro")
  public void testCreate() throws Exception {
    // given

    final var expectedCNPJ = "41536538000100";
    final var expectedEmail = "john.doe@gmail.com";
    final var expectedName = "John Doe";

    final var createInput = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);

    final var partnerRepository = new inMemoryPartnerRepository();

    final var usecase = new CreatePartnerUseCase(partnerRepository);
    final var output = usecase.execute(createInput);

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

    final var createInput = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);
    final var aPartner = Partner.newPartner(expectedName, expectedCNPJ, expectedEmail);

    final var partnerRepository = new inMemoryPartnerRepository();
    partnerRepository.create(aPartner);

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

    final var createInput = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);
    final var aPartner = Partner.newPartner(expectedName, expectedCNPJ, expectedEmail);

    final var partnerRepository = new inMemoryPartnerRepository();
    partnerRepository.create(aPartner);

    final var usecase = new CreatePartnerUseCase(partnerRepository);

    final var actualException = Assertions.assertThrows(ValidationException.class, () -> usecase.execute(createInput));

    // then
    Assertions.assertEquals(expectedError, actualException.getMessage());
  }
}
