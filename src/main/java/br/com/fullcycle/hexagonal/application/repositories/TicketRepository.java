package br.com.fullcycle.hexagonal.application.repositories;

import java.util.Optional;

import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.event.EventId;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.Ticket;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.TicketId;

public interface TicketRepository {

    Optional<Ticket> ticketOfId(TicketId ticketId);

    Optional<Ticket> ticketOfEventId(EventId eventId);

    Optional<Ticket> ticketOfCustomerId(CustomerId customerId);

    Ticket create(Ticket ticket);

    Ticket update(Ticket ticket);

}
