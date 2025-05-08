package br.com.fullcycle.hexagonal.application.domain.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

import org.junit.jupiter.api.Assertions;

public class CustomerIdTest {

    @Test
    @DisplayName("Deve criar um CustomerId")
    public void testCreateCustomerId() {
        final var expectedCustomerId = "1234567890";
        final var actualException = Assertions.assertThrows(ValidationException.class,
                () -> CustomerId.with(expectedCustomerId));
        Assertions.assertEquals("Invalid value for CustomerId", actualException.getMessage());
    }

    @Test
    @DisplayName("Deve criar um CustomerId com um valor nulo")
    public void testCreateCustomerIdWithNullValue() {
        final var customerId = CustomerId.unique();
        Assertions.assertNotNull(customerId);
    }

    @Test
    @DisplayName("Deve criar um CustomerId com um valor invalido")
    public void testCreateCustomerIdWithInvalidValue() {
        final var expectedCustomerId = "1234567890";
        final var actualException = Assertions.assertThrows(ValidationException.class,
                () -> CustomerId.with(expectedCustomerId));
        Assertions.assertEquals("Invalid value for CustomerId", actualException.getMessage());
    }
}