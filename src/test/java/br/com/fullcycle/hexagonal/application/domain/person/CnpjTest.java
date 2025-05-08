package br.com.fullcycle.hexagonal.application.domain.person;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public class CnpjTest {

    @Test
    @DisplayName("Deve criar um Cnpj")
    public void testCreateCnpj() {
        final var cnpj = new Cnpj("12345678901234");
        Assertions.assertNotNull(cnpj);
    }

    @Test
    @DisplayName("Não deve criar um Cnpj com um valor nulo")
    public void testCreateCnpjWithNullValue() {
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> {
            new Cnpj(null);
        });
        Assertions.assertEquals("Cnpj is required", actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve criar um Cnpj com um valor invalido")
    public void testCreateCnpjWithInvalidValue() {
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> {
            new Cnpj("123456789012345");
        });
        Assertions.assertEquals("Invalid value for Cnpj", actualException.getMessage());
    }
}
