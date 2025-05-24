package br.com.fullcycle.hexagonal.infrastructure.graphql;

import br.com.fullcycle.hexagonal.application.usecases.partner.CreatePartnerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.partner.GetPartnerByIdUseCase;
import br.com.fullcycle.hexagonal.infrastructure.dtos.NewPartnerDTO;
import java.util.Objects;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class PartnerResolver {

  private final CreatePartnerUseCase createPartnerUseCase;
  private final GetPartnerByIdUseCase getPartnerByIdUseCase;

  public PartnerResolver(
      final CreatePartnerUseCase createPartnerUseCase,
      final GetPartnerByIdUseCase getPartnerByIdUseCase) {
    this.createPartnerUseCase = Objects.requireNonNull(createPartnerUseCase);
    this.getPartnerByIdUseCase = Objects.requireNonNull(getPartnerByIdUseCase);
  }

  @MutationMapping
  public CreatePartnerUseCase.Output createPartner(@Argument NewPartnerDTO input) {
    return createPartnerUseCase.execute(
        new CreatePartnerUseCase.Input(input.cnpj(), input.email(), input.name()));
  }

  @QueryMapping
  public GetPartnerByIdUseCase.Output partnerOfId(@Argument String id) {
    final var result = getPartnerByIdUseCase.execute(new GetPartnerByIdUseCase.Input(id));

    if (result.isPresent()) {
      return result.get();
    }
    return null;
  }
}
