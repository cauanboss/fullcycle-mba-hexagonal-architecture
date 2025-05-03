package br.com.fullcycle.hexagonal.application.usecases.customer;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.repository.inMemoryCustomerRepository;
import br.com.fullcycle.hexagonal.application.usecases.customer.CreateCustomerUseCase;
import br.com.fullcycle.hexagonal.application.domain.customer.Customer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CreateCustomerUseCaseTest {

  @Test
  @DisplayName("Deve Criar um cliente")
  public void testCreate() throws Exception {
    // given

    final var expectedCPF = "12345678901";
    final var expectedEmail = "john.doe@gmail.com";
    final var expectedName = "John Doe";

    final var customerRepository = new inMemoryCustomerRepository();

    final var createInput = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);

    final var usecase = new CreateCustomerUseCase(customerRepository);
    final var output = usecase.execute(createInput);

    Assertions.assertNotNull(output.id());
    Assertions.assertEquals(expectedCPF, output.cpf());
    Assertions.assertEquals(expectedEmail, output.email());
    Assertions.assertEquals(expectedName, output.name());
  }

  @Test
  @DisplayName("Não deve cadastrar um cliente com CPF duplicado")
  public void testCreateWithDuplicatedCPFShouldFail() throws Exception {

    final var expectedCPF = "12345678901";
    final var expectedEmail = "john.doe@gmail.com";
    final var expectedName = "John Doe";
    final var expectedError = "Customer already exists";

    final var aCustomer = Customer.newCustomer(expectedName, expectedCPF, expectedEmail);

    final var customerRepository = new inMemoryCustomerRepository();
    customerRepository.create(aCustomer);

    final var createInput = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);
    final var usecase = new CreateCustomerUseCase(customerRepository);
    final var actualExeption = Assertions.assertThrows(ValidationException.class, () -> usecase.execute(createInput));

    // then
    Assertions.assertEquals(expectedError, actualExeption.getMessage());
  }

  @Test
  @DisplayName("Não deve cadastrar um cliente com email duplicado")
  public void testCreateWithDuplicatedEmailShouldFail() throws Exception {

    final var expectedCPF = "12345678901";
    final var expectedEmail = "john.doe@gmail.com";
    final var expectedName = "John Doe";
    final var expectedError = "Customer already exists";

    final var aCustomer = Customer.newCustomer(expectedName, expectedCPF, expectedEmail);

    final var customerRepository = new inMemoryCustomerRepository();
    customerRepository.create(aCustomer);

    final var createInput = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);
    final var usecase = new CreateCustomerUseCase(customerRepository);

    final var actualException = Assertions.assertThrows(ValidationException.class, () -> usecase.execute(createInput));

    // then
    Assertions.assertEquals(expectedError, actualException.getMessage());
  }

}
