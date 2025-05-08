package br.com.fullcycle.hexagonal.application.usecases.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.repository.inMemoryCustomerRepository;
import br.com.fullcycle.hexagonal.application.repository.inMemoryEventRepository;
import br.com.fullcycle.hexagonal.application.repository.inMemoryTicketRepository;
import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.event.Event;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.Ticket;
import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.infrastructure.models.TicketStatus;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SubscribeCustomerToEventTest {

  @Test
  @DisplayName("Deve criar um evento")
  public void testCreate() throws Exception {

    final var aCustomer = Customer.newCustomer("John Doe", "12345678901", "john.doe@gmail.com");
    final var aPartner = Partner.newPartner("Disney", "41536538000100", "john.doe@gmail.com");
    final var aEvent = Event.newEvent("Disney on Ice", "2025-01-01", 10, aPartner);
    final var aTicket = Ticket.newTicket(aEvent.eventId(), aCustomer.customerId());

    final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(aEvent.eventId().value(),
        aCustomer.customerId().value());

    // when
    final var customerRepository = new inMemoryCustomerRepository();
    final var eventRepository = new inMemoryEventRepository();
    final var ticketRepository = new inMemoryTicketRepository();

    eventRepository.create(aEvent);
    customerRepository.create(aCustomer);
    ticketRepository.create(aTicket);

    final var subscribeUseCase = new SubscribeCustomerToEventUseCase(eventRepository, customerRepository,
        ticketRepository);
    final var output = subscribeUseCase.execute(subscribeInput);

    // then
    assertEquals(aEvent.eventId().value(), output.eventId());
    assertNotNull(output.reservationDate());
    assertEquals(TicketStatus.PENDING.name(), output.ticketStatus());
  }

  @Test
  @DisplayName("Deve retornar um erro caso o evento não seja encontrado")
  public void testReserveTicketWithoutEvent() throws Exception {

    // given
    final var expectedError = "Event not found";
    final var expectedEventId = UUID.randomUUID().toString();
    final var expectedCustomerId = UUID.randomUUID().toString();

    final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(expectedEventId, expectedCustomerId);

    // when
    final var customerRepository = new inMemoryCustomerRepository();
    final var eventRepository = new inMemoryEventRepository();
    final var ticketRepository = new inMemoryTicketRepository();

    final var subscribeUseCase = new SubscribeCustomerToEventUseCase(eventRepository, customerRepository,
        ticketRepository);

    final var actualException = assertThrows(ValidationException.class, () -> subscribeUseCase.execute(subscribeInput));

    assertEquals(expectedError, actualException.getMessage());
  }

  @Test
  @DisplayName("não deve criar um ticket com um cliente não encontrado")
  public void testReserveTicketWithoutCustomer() throws Exception {

    // given
    final var expectedError = "Customer not found";
    final var expectedCustomerId = CustomerId.with(UUID.randomUUID().toString());

    final var aPartner = Partner.newPartner("Disney", "41536538000100", "john.doe@gmail.com");
    final var aEvent = Event.newEvent("Disney on Ice", "2025-01-01", 10, aPartner);
    final var aTicket = Ticket.newTicket(aEvent.eventId(), expectedCustomerId);

    final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(aEvent.eventId().value(),
        expectedCustomerId.value());

    // when
    final var customerRepository = new inMemoryCustomerRepository();
    final var eventRepository = new inMemoryEventRepository();
    final var ticketRepository = new inMemoryTicketRepository();

    eventRepository.create(aEvent);
    ticketRepository.create(aTicket);

    final var subscribeUseCase = new SubscribeCustomerToEventUseCase(eventRepository, customerRepository,
        ticketRepository);
    final var actualException = assertThrows(ValidationException.class, () -> subscribeUseCase.execute(subscribeInput));

    assertEquals(expectedError, actualException.getMessage());
  }

  @Test
  @DisplayName("não deve criar um ticket com um cliente já inscrito no evento")
  public void testReserveTicketWithCustomerAlreadySubscribed() throws Exception {

    // given
    final var expectedError = "Customer already registered for this event";
    final var aPartner = Partner.newPartner("Disney", "12345678901234", "john.doe@gmail.com");
    final var aEvent = Event.newEvent("Disney on Ice", "2025-01-01", 10, aPartner);
    final var aCustomer = Customer.newCustomer("John Doe", "12345678901", "john.doe@gmail.com");
    final var aTicket = Ticket.newTicket(aEvent.eventId(), aCustomer.customerId());

    final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(aEvent.eventId().value(),
        aCustomer.customerId().value());

    // when
    final var customerRepository = new inMemoryCustomerRepository();
    final var eventRepository = new inMemoryEventRepository();
    final var ticketRepository = new inMemoryTicketRepository();

    eventRepository.create(aEvent);
    customerRepository.create(aCustomer);
    ticketRepository.create(aTicket);

    final var subscribeUseCase = new SubscribeCustomerToEventUseCase(eventRepository, customerRepository,
        ticketRepository);

    subscribeUseCase.execute(subscribeInput);

    final var actualException = assertThrows(ValidationException.class, () -> subscribeUseCase.execute(subscribeInput));

    // then
    assertEquals(expectedError, actualException.getMessage());
  }

  @Test
  @DisplayName("Um mesmo cliente não pode comprar de um evento que não tenha mais cadeiras")
  public void testReserveTicketWithEventSoldOut() throws Exception {

    // given
    final var expectedError = "Event sold out";

    final var aPartner = Partner.newPartner("Disney", "41536538000100", "john.doe@gmail.com");
    final var aCustomer = Customer.newCustomer("John Doe", "12345678901", "john.doe@gmail.com");
    final var aEvent = Event.newEvent("Disney on Ice", "2025-01-01", 0, aPartner);
    final var aTicket = Ticket.newTicket(aEvent.eventId(), aCustomer.customerId());

    final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(aEvent.eventId().value(),
        aCustomer.customerId().value());

    // when
    final var customerRepository = new inMemoryCustomerRepository();
    final var eventRepository = new inMemoryEventRepository();
    final var ticketRepository = new inMemoryTicketRepository();

    eventRepository.create(aEvent);
    customerRepository.create(aCustomer);
    ticketRepository.create(aTicket);

    final var subscribeUseCase = new SubscribeCustomerToEventUseCase(eventRepository, customerRepository,
        ticketRepository);

    final var actualException = assertThrows(ValidationException.class, () -> subscribeUseCase.execute(subscribeInput));

    // then
    assertEquals(expectedError, actualException.getMessage());
  }
}
