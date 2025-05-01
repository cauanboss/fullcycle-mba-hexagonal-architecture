package br.com.fullcycle.hexagonal.infrastructure.repositories;

import br.com.fullcycle.hexagonal.infrastructure.models.Partner;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface PartnerRepository extends CrudRepository<Partner, Long> {

  Optional<Partner> findByCnpj(String cnpj);

  Optional<Partner> findByEmail(String email);
}
