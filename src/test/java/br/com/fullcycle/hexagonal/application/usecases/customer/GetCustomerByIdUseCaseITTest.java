package br.com.fullcycle.hexagonal.application.usecases.customer;

import br.com.fullcycle.hexagonal.integrationTest;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.usecases.customer.GetCustomerByIdUseCase;
import br.com.fullcycle.hexagonal.infrastructure.models.Customer;
import br.com.fullcycle.hexagonal.infrastructure.repositories.CustomerRepository;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class GetCustomerByIdUseCaseITTest extends integrationTest {

  @Autowired
  private GetCustomerByIdUseCase usecase;

  @Autowired
  private CustomerRepository customerRepository;

  @BeforeEach
  void tearDown() {
    customerRepository.deleteAll();
  }

  @Test
  @DisplayName("Deve obter um cliente por id")
  public void testGetBy() throws Exception {

    final var expectedCPF = "12345678901";
    final var expectedEmail = "john.doe@gmail.com";
    final var expectedName = "John Doe";
    final var customer = saveCustomer(expectedCPF, expectedEmail, expectedName);
    final var expectedId = customer.getId();

    final var input = new GetCustomerByIdUseCase.Input(expectedId.toString());
    final var output = usecase.execute(input);
    // then
    Assertions.assertEquals(expectedId, output.id());
    Assertions.assertEquals(expectedCPF, output.cpf());
    Assertions.assertEquals(expectedEmail, output.email());
    Assertions.assertEquals(expectedName, output.name());
  }

  @Test
  @DisplayName("Deve obter um vazio por tentar recuperar um cliente não existente")
  public void testGetByClient() {

    try {
      final var expectedId = UUID.randomUUID().toString();

      final var input = new GetCustomerByIdUseCase.Input(expectedId);
      // When & Then (act & assert)
      RuntimeException exception = Assertions.assertThrows(
          RuntimeException.class, () -> usecase.execute(input) // Executa e espera a exceção
      );

      // Verifica a mensagem da exceção
      Assertions.assertEquals("Customer not found", exception.getMessage());

    } catch (final ValidationException e) {
      Assertions.assertNotNull(e.getMessage());
    }
  }

  private Customer saveCustomer(final String cpf, final String email, final String name) {
    final var customer = new Customer();
    customer.setCpf(cpf);
    customer.setEmail(email);
    customer.setName(name);
    return customerRepository.save(customer);
  }
}
