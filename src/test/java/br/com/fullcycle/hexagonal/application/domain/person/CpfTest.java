package br.com.fullcycle.hexagonal.application.domain.person;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

import org.junit.jupiter.api.Assertions;

public class CpfTest {

    @Test
    @DisplayName("Deve criar um Cpf")
    public void testCreateCpf() {
        final var cpf = new Cpf("12345678901");
        Assertions.assertNotNull(cpf);
    }

    @Test
    @DisplayName("Não deve criar um Cpf com um valor nulo")
    public void testCreateCpfWithNullValue() {
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> {
            new Cpf(null);
        });
        Assertions.assertEquals("Invalid value for Cpf", actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve criar um Cpf com um valor invalido")
    public void testCreateCpfWithInvalidValue() {
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> {
            new Cpf("1234567890100");
        });
        Assertions.assertEquals("Invalid value for Cpf", actualException.getMessage());
    }
}
