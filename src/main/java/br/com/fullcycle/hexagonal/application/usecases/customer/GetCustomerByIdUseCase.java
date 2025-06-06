package br.com.fullcycle.hexagonal.application.usecases.customer;

import br.com.fullcycle.hexagonal.application.UseCase;
import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;
import java.util.Objects;

public class GetCustomerByIdUseCase
    extends UseCase<GetCustomerByIdUseCase.Input, GetCustomerByIdUseCase.Output> {

  private final CustomerRepository customerRepository;

  public GetCustomerByIdUseCase(final CustomerRepository customerRepository) {
    this.customerRepository = Objects.requireNonNull(customerRepository);
  }

  @Override
  public Output execute(final Input input) {
    return customerRepository
        .customerOfId(CustomerId.with(input.id))
        .map(
            c ->
                new Output(
                    c.customerId().value(), c.cpf().value(), c.email().value(), c.name().value()))
        .orElseThrow(() -> new RuntimeException("Customer not found"));
  }

  public record Input(String id) {}

  public record Output(String id, String cpf, String email, String name) {}
}
