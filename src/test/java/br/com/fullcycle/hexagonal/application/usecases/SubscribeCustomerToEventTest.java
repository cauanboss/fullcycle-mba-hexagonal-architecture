package br.com.fullcycle.hexagonal.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.infrastructure.models.Customer;
import br.com.fullcycle.hexagonal.infrastructure.models.Event;
import br.com.fullcycle.hexagonal.infrastructure.models.Ticket;
import br.com.fullcycle.hexagonal.infrastructure.models.TicketStatus;
import br.com.fullcycle.hexagonal.infrastructure.services.CustomerService;
import br.com.fullcycle.hexagonal.infrastructure.services.EventService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class SubscribeCustomerToEventTest {

  @Test
  @DisplayName("Deve criar um evento")
  public void testCreate() throws Exception {

    // given
    final var expectedEventId = UUID.randomUUID().getMostSignificantBits();
    final var expectedCustomerId = UUID.randomUUID().getMostSignificantBits();
    final var expectedTicketsSize = 1;

    final var aCustomer = new Customer();
    aCustomer.setId(expectedCustomerId);
    aCustomer.setName("John Doe");
    aCustomer.setEmail("john.doe@gmail.com");
    aCustomer.setCpf("12345678901");

    final var aEvent = new Event();
    aEvent.setId(expectedEventId);
    aEvent.setName("Disney on Ice");
    aEvent.setTotalSpots(10);

    final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(expectedEventId, expectedCustomerId);

    // when
    final var customerService = Mockito.mock(CustomerService.class);
    final var eventService = Mockito.mock(EventService.class);

    when(eventService.findById(expectedEventId)).thenReturn(Optional.of(aEvent));
    when(customerService.findById(expectedCustomerId)).thenReturn(Optional.of(aCustomer));
    when(eventService.findTicketByEventIdAndCustomerId(expectedEventId, expectedCustomerId))
        .thenReturn(Optional.empty());

    when(eventService.save(any()))
        .thenAnswer(
            a -> {
              var event = a.getArgument(0, Event.class);
              assertEquals(expectedTicketsSize, event.getTickets().size());
              return event;
            });

    final var subscribeUseCase = new SubscribeCustomerToEventUseCase(eventService, customerService);
    final var output = subscribeUseCase.execute(subscribeInput);

    // then
    assertEquals(expectedEventId, output.eventId());
    assertNotNull(output.reservationDate());
    assertEquals(TicketStatus.PENDING.name(), output.ticketStatus());
  }

  @Test
  @DisplayName("Deve retornar um erro caso o evento não seja encontrado")
  public void testReserveTicketWithoutEvent() throws Exception {

    // given
    final var expectedError = "Event not found";
    final var expectedEventId = UUID.randomUUID().getMostSignificantBits();
    final var expectedCustomerId = UUID.randomUUID().getMostSignificantBits();

    final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(expectedEventId, expectedCustomerId);

    // when
    final var customerService = Mockito.mock(CustomerService.class);
    final var eventService = Mockito.mock(EventService.class);

    when(customerService.findById(expectedCustomerId)).thenReturn(Optional.of(new Customer()));
    when(eventService.findById(expectedEventId)).thenReturn(Optional.empty());

    final var useCasae = new SubscribeCustomerToEventUseCase(eventService, customerService);
    final var actualException = assertThrows(ValidationException.class, () -> useCasae.execute(subscribeInput));

    assertEquals(expectedError, actualException.getMessage());
  }

  @Test
  @DisplayName("não deve criar um ticket com um cliente não encontrado")
  public void testReserveTicketWithoutCustomer() throws Exception {

    // given
    final var expectedError = "Customer not found";
    final var expectedEventId = UUID.randomUUID().getMostSignificantBits();
    final var expectedCustomerId = UUID.randomUUID().getMostSignificantBits();

    final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(expectedEventId, expectedCustomerId);

    // when
    final var customerService = Mockito.mock(CustomerService.class);
    final var eventService = Mockito.mock(EventService.class);

    when(customerService.findById(expectedCustomerId)).thenReturn(Optional.empty());
    when(eventService.findById(expectedEventId)).thenReturn(Optional.of(new Event()));

    final var useCasae = new SubscribeCustomerToEventUseCase(eventService, customerService);
    final var actualException = assertThrows(ValidationException.class, () -> useCasae.execute(subscribeInput));

    assertEquals(expectedError, actualException.getMessage());
  }

  @Test
  @DisplayName("não deve criar um ticket com um cliente já inscrito no evento")
  public void testReserveTicketWithCustomerAlreadySubscribed() throws Exception {

    // given
    final var expectedError = "Customer already registered for this event";
    final var expectedEventId = UUID.randomUUID().getMostSignificantBits();
    final var expectedCustomerId = UUID.randomUUID().getMostSignificantBits();

    final var aEvent = new Event();
    aEvent.setId(expectedEventId);
    aEvent.setName("Disney on Ice");
    aEvent.setTotalSpots(10);

    final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(aEvent.getId(), expectedCustomerId);

    // when
    final var customerService = Mockito.mock(CustomerService.class);
    final var eventService = Mockito.mock(EventService.class);

    final var subscribeUseCase = new SubscribeCustomerToEventUseCase(eventService, customerService);

    when(eventService.findById(expectedEventId)).thenReturn(Optional.of(aEvent));
    when(customerService.findById(expectedCustomerId)).thenReturn(Optional.of(new Customer()));
    when(eventService.findTicketByEventIdAndCustomerId(expectedEventId, expectedCustomerId))
        .thenReturn(Optional.of(new Ticket()));

    final var actualException = assertThrows(ValidationException.class, () -> subscribeUseCase.execute(subscribeInput));

    // then
    assertEquals(expectedError, actualException.getMessage());
  }

  @Test
  @DisplayName("Um mesmo cliente não pode comprar de um evento que não tenha mais cadeiras")
  public void testReserveTicketWithEventSoldOut() throws Exception {

    // given
    final var expectedError = "Event sold out";
    final var expectedEventId = UUID.randomUUID().getMostSignificantBits();
    final var expectedCustomerId = UUID.randomUUID().getMostSignificantBits();

    final var aEvent = new Event();
    aEvent.setId(expectedEventId);
    aEvent.setName("Disney on Ice");
    aEvent.setTotalSpots(0);

    final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(aEvent.getId(), expectedCustomerId);

    // when
    final var customerService = Mockito.mock(CustomerService.class);
    final var eventService = Mockito.mock(EventService.class);

    final var subscribeUseCase = new SubscribeCustomerToEventUseCase(eventService, customerService);

    when(eventService.findById(expectedEventId)).thenReturn(Optional.of(aEvent));
    when(customerService.findById(expectedCustomerId)).thenReturn(Optional.of(new Customer()));
    when(eventService.findTicketByEventIdAndCustomerId(expectedEventId, expectedCustomerId))
        .thenReturn(Optional.empty());

    final var actualException = assertThrows(ValidationException.class, () -> subscribeUseCase.execute(subscribeInput));

    // then
    assertEquals(expectedError, actualException.getMessage());
  }
}
