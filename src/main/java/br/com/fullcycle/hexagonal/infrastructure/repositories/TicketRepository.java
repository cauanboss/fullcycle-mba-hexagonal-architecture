package br.com.fullcycle.hexagonal.infrastructure.repositories;

import br.com.fullcycle.hexagonal.infrastructure.models.Ticket;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface TicketRepository extends CrudRepository<Ticket, Long> {

  Optional<Ticket> findByEventIdAndCustomerId(Long id, Long customerId);
}
