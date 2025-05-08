package br.com.fullcycle.hexagonal.application.domain.customer;

import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CustomerTest {

    @Test
    @DisplayName("Deve criar um cliente com sucesso")
    public void testCreateCustomerSuccess() {
        final var expectedName = "John Doe";
        final var expectedCPF = "12345678901";
        final var expectedEmail = "john.doe@gmail.com";

        final var customer = Customer.newCustomer(expectedName, expectedCPF, expectedEmail);

        Assertions.assertNotNull(customer);
        Assertions.assertEquals(expectedName, customer.name().value());
        Assertions.assertEquals(expectedCPF, customer.cpf().value());
        Assertions.assertEquals(expectedEmail, customer.email().value());
    }

    @Test
    @DisplayName("Não deve criar um cliente com um cpf invalido")
    public void testCreateCustomerWithInvalidCPF() {
        final var expectedName = "John Doe";
        final var expectedCPF = "1234567";
        final var expectedEmail = "john.doe@gmail.com";

        final var actualException = Assertions.assertThrows(ValidationException.class,
                () -> Customer.newCustomer(expectedName, expectedCPF, expectedEmail));

        Assertions.assertEquals("Invalid value for Cpf", actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve criar um cliente com um email invalido")
    public void testCreateCustomerWithInvalidEmail() {
        final var expectedName = "John Doe";
        final var expectedCPF = "12345678901";
        final var expectedEmail = "john.doegmail.com";

        final var actualException = Assertions.assertThrows(ValidationException.class,
                () -> Customer.newCustomer(expectedName, expectedCPF, expectedEmail));

        Assertions.assertEquals("Invalid value for Email", actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve criar um cliente com um nome invalido")
    public void testCreateCustomerWithInvalidName() {
        final var expectedName = "";
        final var expectedCPF = "12345678901";
        final var expectedEmail = "john.doe@gmail.com";

        final var actualException = Assertions.assertThrows(ValidationException.class,
                () -> Customer.newCustomer(expectedName, expectedCPF, expectedEmail));

        Assertions.assertEquals("Invalid value for Name", actualException.getMessage());
    }

}
