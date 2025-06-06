package br.com.fullcycle.hexagonal.infrastructure.graphql;

import br.com.fullcycle.hexagonal.application.usecases.customer.CreateCustomerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.customer.GetCustomerByIdUseCase;
import br.com.fullcycle.hexagonal.infrastructure.dtos.NewCustomerDTO;
import java.util.Objects;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class CustomerResolver {

  private final CreateCustomerUseCase createCustomerUseCase;
  private final GetCustomerByIdUseCase getCustomerByIdUseCase;

  public CustomerResolver(
      final CreateCustomerUseCase createCustomerUseCase,
      final GetCustomerByIdUseCase getCustomerByIdUseCase) {
    this.createCustomerUseCase = Objects.requireNonNull(createCustomerUseCase);
    this.getCustomerByIdUseCase = Objects.requireNonNull(getCustomerByIdUseCase);
  }

  @MutationMapping
  public CreateCustomerUseCase.Output createCustomer(@Argument NewCustomerDTO input) {
    return createCustomerUseCase.execute(
        new CreateCustomerUseCase.Input(input.cpf(), input.email(), input.name()));
  }

  @QueryMapping
  public GetCustomerByIdUseCase.Output customerOfId(@Argument String id) {
    final var result = getCustomerByIdUseCase.execute(new GetCustomerByIdUseCase.Input(id));

    if (result != null) {
      return result;
    }
    return null;
  }
}
