package br.com.fullcycle.hexagonal.application.domain.person;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EmailTest {

  @Test
  @DisplayName("Deve criar um Email")
  public void testCreateEmail() {
    final var email = new Email("john.doe@example.com");
    Assertions.assertNotNull(email);
  }

  @Test
  @DisplayName("Não deve criar um Email com um valor nulo")
  public void testCreateEmailWithNullValue() {
    final var actualException =
        Assertions.assertThrows(
            ValidationException.class,
            () -> {
              new Email(null);
            });
    Assertions.assertEquals("Invalid value for Email", actualException.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um Email com um valor invalido")
  public void testCreateEmailWithInvalidValue() {
    final var actualException =
        Assertions.assertThrows(
            ValidationException.class,
            () -> {
              new Email("john.doe@example");
            });
    Assertions.assertEquals("Invalid value for Email", actualException.getMessage());
  }
}
