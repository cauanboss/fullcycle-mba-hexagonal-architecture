package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.application.UseCase;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.infrastructure.models.Ticket;
import br.com.fullcycle.hexagonal.infrastructure.models.TicketStatus;
import br.com.fullcycle.hexagonal.infrastructure.services.CustomerService;
import br.com.fullcycle.hexagonal.infrastructure.services.EventService;
import java.time.Instant;
import org.springframework.transaction.annotation.Transactional;

public class SubscribeCustomerToEventUseCase
    extends UseCase<SubscribeCustomerToEventUseCase.Input, SubscribeCustomerToEventUseCase.Output> {

  private final EventService eventService;
  private final CustomerService customerService;

  public SubscribeCustomerToEventUseCase(EventService eventService, CustomerService customerService) {
    this.eventService = eventService;
    this.customerService = customerService;
  }

  @Override
  @Transactional
  public Output execute(Input input) {

    var event = eventService
        .findById(input.eventId)
        .orElseThrow(() -> new ValidationException("Event not found", null));

    var customer = customerService
        .findById(input.customerId)
        .orElseThrow(() -> new ValidationException("Customer not found", null));

    if (eventService
        .findTicketByEventIdAndCustomerId(input.eventId, input.customerId)
        .isPresent()) {
      throw new ValidationException("Customer already registered for this event", null);
    }

    if (event.getTotalSpots() < event.getTickets().size() + 1) {
      throw new ValidationException("Event sold out", null);
    }

    var ticket = new Ticket();
    ticket.setEvent(event);
    ticket.setCustomer(customer);
    ticket.setReservedAt(Instant.now());
    ticket.setStatus(TicketStatus.PENDING);

    event.getTickets().add(ticket);

    eventService.save(event);

    return new Output(event.getId(), ticket.getStatus().name(), ticket.getReservedAt());
  }

  public record Input(Long eventId, Long customerId) {
  }

  public record Output(Long eventId, String ticketStatus, Instant reservationDate) {
  }
}
