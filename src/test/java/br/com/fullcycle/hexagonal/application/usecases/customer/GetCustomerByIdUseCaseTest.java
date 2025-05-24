package br.com.fullcycle.hexagonal.application.usecases.customer;

import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.repository.inMemoryCustomerRepository;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GetCustomerByIdUseCaseTest {

  @Test
  @DisplayName("Deve obter um cliente por id")
  public void testGetBy() throws Exception {

    // faça um cpf valido
    final var expectedCPF = "12345678901";
    final var expectedEmail = "john.doe@gmail.com";
    final var expectedName = "John Doe";

    final var aCustomer = Customer.newCustomer(expectedName, expectedCPF, expectedEmail);
    final var expectedId = aCustomer.customerId().value().toString();
    // when
    final var customerRepository = new inMemoryCustomerRepository();
    customerRepository.create(aCustomer);

    final var usecase = new GetCustomerByIdUseCase(customerRepository);
    final var input = new GetCustomerByIdUseCase.Input(expectedId);
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

    final var expectedId = UUID.randomUUID().toString();

    final var input = new GetCustomerByIdUseCase.Input(expectedId);
    // when
    final var customerRepository = new inMemoryCustomerRepository();
    final var usecase = new GetCustomerByIdUseCase(customerRepository);
    Assertions.assertThrows(
        RuntimeException.class,
        () -> {
          usecase.execute(input);
        });
  }
}
