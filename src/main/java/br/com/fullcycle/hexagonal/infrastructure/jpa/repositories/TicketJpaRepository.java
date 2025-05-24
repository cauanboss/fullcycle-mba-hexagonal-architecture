package br.com.fullcycle.hexagonal.infrastructure.jpa.repositories;

import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.TicketEntity;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface TicketJpaRepository extends CrudRepository<TicketEntity, UUID> {}
