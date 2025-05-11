package br.com.fullcycle.hexagonal.application.usecases.customer;

import br.com.fullcycle.hexagonal.integrationTest;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.CustomerEntity;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.CustomerJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CreateCustomerUseCaseITTest extends integrationTest {

  @Autowired
  private CreateCustomerUseCase usecase;

  @Autowired
  private CustomerJpaRepository customerRepository;

  @AfterEach
  void tearDown() {
    customerRepository.deleteAll();
  }

  @Test
  @DisplayName("Deve Criar um cliente")
  public void testCreate() throws Exception {
    // given

    final var expectedCPF = "12345678901";
    final var expectedEmail = "john.doe@gmail.com";
    final var expectedName = "John Doe";

    final var createInput = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);

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
    this.saveCustomer(expectedCPF, expectedEmail, expectedName);

    final var createInput = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);

    final var actualExeption = Assertions.assertThrows(ValidationException.class, () -> usecase.execute(createInput));

    // then
    Assertions.assertEquals(expectedError, actualExeption.getMessage());
  }

  @Test
  @DisplayName("Não deve cadastrar um cliente com CPF duplicado")
  public void testCreateWithDuplicatedEmailShouldFail() throws Exception {

    final var expectedCPF = "123456789011";
    final var expectedEmail = "john.doe@gmail.com";
    final var expectedName = "John Doe";
    final var expectedError = "Customer already exists";

    this.saveCustomer(expectedCPF, expectedEmail, expectedName);

    final var createInput = new CreateCustomerUseCase.Input(expectedCPF, expectedEmail, expectedName);

    final var actualException = Assertions.assertThrows(ValidationException.class, () -> usecase.execute(createInput));

    // then
    Assertions.assertEquals(expectedError, actualException.getMessage());
  }

  private CustomerEntity saveCustomer(final String cpf, final String email, final String name) {
    final var customer = new CustomerEntity();
    customer.setCpf(cpf);
    customer.setEmail(email);
    customer.setName(name);
    return customerRepository.save(customer);
  }
}
