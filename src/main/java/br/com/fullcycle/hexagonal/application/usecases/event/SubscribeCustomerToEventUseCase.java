package br.com.fullcycle.hexagonal.application.usecases.event;

import br.com.fullcycle.hexagonal.application.UseCase;
import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.event.EventId;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;
import br.com.fullcycle.hexagonal.application.repositories.EventRepository;
import br.com.fullcycle.hexagonal.application.repositories.TicketRepository;
import java.time.Instant;
import org.springframework.transaction.annotation.Transactional;

public class SubscribeCustomerToEventUseCase
    extends UseCase<SubscribeCustomerToEventUseCase.Input, SubscribeCustomerToEventUseCase.Output> {

  private final EventRepository eventRepository;
  private final CustomerRepository customerRepository;
  private final TicketRepository ticketRepository;

  public SubscribeCustomerToEventUseCase(
      EventRepository eventRepository,
      CustomerRepository customerRepository,
      TicketRepository ticketRepository) {
    this.eventRepository = eventRepository;
    this.customerRepository = customerRepository;
    this.ticketRepository = ticketRepository;
  }

  @Override
  @Transactional
  public Output execute(Input input) {

    var event =
        eventRepository
            .eventOfId(EventId.with(input.eventId))
            .orElseThrow(() -> new ValidationException("Event not found", null));

    var customer =
        customerRepository
            .customerOfId(CustomerId.with(input.customerId))
            .orElseThrow(() -> new ValidationException("Customer not found", null));

    var ticket = event.resetTicket(customer.customerId());

    ticketRepository.create(ticket);
    eventRepository.update(event);

    return new Output(event.eventId().value(), ticket.status().name(), ticket.reservedAt());
  }

  public record Input(String eventId, String customerId) {}

  public record Output(String eventId, String ticketStatus, Instant reservationDate) {}
}
