package br.com.fullcycle.hexagonal.application.usecases.partner;

import br.com.fullcycle.hexagonal.application.UseCase;
import br.com.fullcycle.hexagonal.application.domain.partner.PartnerId;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;
import java.util.Optional;

public class GetPartnerByIdUseCase
    extends UseCase<GetPartnerByIdUseCase.Input, Optional<GetPartnerByIdUseCase.Output>> {

  private final PartnerRepository partnerRepository;

  public GetPartnerByIdUseCase(final PartnerRepository partnerRepository) {
    this.partnerRepository = partnerRepository;
  }

  @Override
  public Optional<Output> execute(Input input) {
    return partnerRepository
        .partnerOfId(PartnerId.with(input.id()))
        .map(p -> new Output(p.partnerId(), p.cnpj().value(), p.email().value(), p.name().value()));
  }

  public record Input(String id) {
  }

  public record Output(String id, String cnpj, String email, String name) {
  }
}
