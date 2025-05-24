package br.com.fullcycle.hexagonal.application.domain.person;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class NameTest {

  @Test
  @DisplayName("Deve criar um Name")
  public void testCreateName() {
    final var name = new Name("John Doe");
    Assertions.assertNotNull(name);
  }

  @Test
  @DisplayName("Não deve criar um Name com um valor nulo")
  public void testCreateNameWithNullValue() {
    final var actualException =
        Assertions.assertThrows(
            ValidationException.class,
            () -> {
              new Name(null);
            });
    Assertions.assertEquals("Invalid value for Name", actualException.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um Name com um valor invalido")
  public void testCreateNameWithInvalidValue() {
    final var actualException =
        Assertions.assertThrows(
            ValidationException.class,
            () -> {
              new Name("");
            });
    Assertions.assertEquals("Invalid value for Name", actualException.getMessage());
  }
}
