package br.com.fullcycle.hexagonal.infrastructure.repositories;

import br.com.fullcycle.hexagonal.application.domain.event.ticket.Ticket;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.TicketId;
import br.com.fullcycle.hexagonal.application.repositories.TicketRepository;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.TicketEntity;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.TicketJpaRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

// interface Adapter
@Component
public class TicketDatabaseRepository implements TicketRepository {

  private final TicketJpaRepository ticketJpaRepository;

  public TicketDatabaseRepository(TicketJpaRepository ticketJpaRepository) {
    this.ticketJpaRepository = ticketJpaRepository;
  }

  @Override
  public Optional<Ticket> ticketOfId(final TicketId ticketId) {
    return this.ticketJpaRepository
        .findById(UUID.fromString(ticketId.value()))
        .map(it -> it.toTicket());
  }

  @Override
  @Transactional
  public Ticket create(final Ticket ticket) {
    return this.ticketJpaRepository.save(TicketEntity.of(ticket)).toTicket();
  }

  @Override
  @Transactional
  public Ticket update(final Ticket ticket) {
    return this.ticketJpaRepository.save(TicketEntity.of(ticket)).toTicket();
  }

  @Override
  @Transactional
  public void deleteAll() {
    this.ticketJpaRepository.deleteAll();
  }
}
