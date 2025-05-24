package br.com.fullcycle.hexagonal.application.domain.partner;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PartnerTest {

  @Test
  @DisplayName("Deve criar um parceiro com sucesso")
  public void testCreatePartnerSuccess() {
    final var expectedName = "John Doe";
    final var expectedCNPJ = "41536538000100";
    final var expectedEmail = "john.doe@gmail.com";

    final var partner = Partner.newPartner(expectedName, expectedCNPJ, expectedEmail);

    Assertions.assertNotNull(partner);
    Assertions.assertEquals(expectedName, partner.name().value());
    Assertions.assertEquals(expectedCNPJ, partner.cnpj().value());
    Assertions.assertEquals(expectedEmail, partner.email().value());
  }

  @Test
  @DisplayName("Não deve criar um parceiro com um cnpj invalido")
  public void testCreatePartnerWithInvalidCNPJ() {
    final var expectedName = "John Doe";
    final var expectedCNPJ = "1234567";
    final var expectedEmail = "john.doe@gmail.com";

    final var actualException =
        Assertions.assertThrows(
            ValidationException.class,
            () -> Partner.newPartner(expectedName, expectedCNPJ, expectedEmail));

    Assertions.assertEquals("Invalid value for Cnpj", actualException.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um parceiro com um email invalido")
  public void testCreatePartnerWithInvalidEmail() {
    final var expectedName = "John Doe";
    final var expectedCNPJ = "12345678901";
    final var expectedEmail = "john.doegmail.com";

    final var actualException =
        Assertions.assertThrows(
            ValidationException.class,
            () -> Partner.newPartner(expectedName, expectedCNPJ, expectedEmail));

    Assertions.assertEquals("Invalid value for Email", actualException.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um cliente com um nome invalido")
  public void testCreateCustomerWithInvalidName() {
    final var expectedName = "";
    final var expectedCNPJ = "12345678901";
    final var expectedEmail = "john.doe@gmail.com";

    final var actualException =
        Assertions.assertThrows(
            ValidationException.class,
            () -> Partner.newPartner(expectedName, expectedCNPJ, expectedEmail));

    Assertions.assertEquals("Invalid value for Name", actualException.getMessage());
  }
}
