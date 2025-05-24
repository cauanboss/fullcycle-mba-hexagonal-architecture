package br.com.fullcycle.hexagonal.infrastructure.jpa.repositories;

import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.PartnerEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface PartnerJpaRepository extends CrudRepository<PartnerEntity, UUID> {

  Optional<PartnerEntity> findByCnpj(String cnpj);

  Optional<PartnerEntity> findByEmail(String email);
}
