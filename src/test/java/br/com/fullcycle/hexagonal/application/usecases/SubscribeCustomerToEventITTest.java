package br.com.fullcycle.hexagonal.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.com.fullcycle.hexagonal.integrationTest;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.infrastructure.models.Customer;
import br.com.fullcycle.hexagonal.infrastructure.models.Event;
import br.com.fullcycle.hexagonal.infrastructure.models.TicketStatus;
import br.com.fullcycle.hexagonal.infrastructure.repositories.CustomerRepository;
import br.com.fullcycle.hexagonal.infrastructure.repositories.EventRepository;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SubscribeCustomerToEventITTest extends integrationTest {

  @Autowired
  private SubscribeCustomerToEventUseCase usecase;

  @Autowired
  private EventRepository eventRepository;

  @Autowired
  private CustomerRepository customerRepository;

  @BeforeEach
  void tearDown() {
    eventRepository.deleteAll();
    customerRepository.deleteAll();
  }

  @Test
  @DisplayName("Deve criar um evento")
  public void testCreate() throws Exception {

    final var aEvent = saveEvent("Disney on Ice", 10);
    final var aCustomer = saveCustomer("John Doe", "john.doe@gmail.com", "12345678901");
    final var expectedEventId = aEvent.getId();

    final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(aEvent.getId(), aCustomer.getId());
    final var output = usecase.execute(subscribeInput);

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
    final var aCustomer = saveCustomer("John Doe", "john.doe@gmail.com", "12345678901");
    final var expectedCustomerId = aCustomer.getId();

    final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(expectedEventId, expectedCustomerId);
    final var actualException = assertThrows(ValidationException.class, () -> usecase.execute(subscribeInput));

    assertEquals(expectedError, actualException.getMessage());
  }

  @Test
  @DisplayName("não deve criar um ticket com um cliente não encontrado")
  public void testReserveTicketWithoutCustomer() throws Exception {

    // given
    final var expectedError = "Customer not found";
    final var aEvent = saveEvent("Disney on Ice", 10);
    final var expectedEventId = aEvent.getId();
    final var expectedCustomerId = UUID.randomUUID().getMostSignificantBits();

    final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(expectedEventId, expectedCustomerId);

    final var actualException = assertThrows(ValidationException.class, () -> usecase.execute(subscribeInput));

    assertEquals(expectedError, actualException.getMessage());
  }

  @Test
  @DisplayName("não deve criar um ticket com um cliente já inscrito no evento")
  public void testReserveTicketWithCustomerAlreadySubscribed() throws Exception {

    // given
    final var expectedError = "Customer already registered for this event";

    final var aEvent = saveEvent("Disney on Ice", 10);
    final var aCustomer = saveCustomer("John Doe", "john.doe@gmail.com", "12345678901");
    final var expectedCustomerId = aCustomer.getId();

    final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(aEvent.getId(), expectedCustomerId);

    usecase.execute(subscribeInput);

    final var actualException = assertThrows(ValidationException.class, () -> usecase.execute(subscribeInput));

    // then
    assertEquals(expectedError, actualException.getMessage());
  }

  @Test
  @DisplayName("Um mesmo cliente não pode comprar de um evento que não tenha mais cadeiras")
  public void testReserveTicketWithEventSoldOut() throws Exception {

    // given
    final var expectedError = "Event sold out";

    final var aEvent = saveEvent("Disney on Ice", 0);
    final var aCustomer = saveCustomer("John Doe", "john.doe@gmail.com", "12345678901");

    final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(aEvent.getId(), aCustomer.getId());

    final var actualException = assertThrows(ValidationException.class, () -> usecase.execute(subscribeInput));

    // then
    assertEquals(expectedError, actualException.getMessage());
  }

  private Event saveEvent(final String name, final int totalSpots) {
    final var event = new Event();
    event.setName(name);
    event.setTotalSpots(totalSpots);
    return eventRepository.save(event);
  }

  private Customer saveCustomer(final String name, final String email, final String cpf) {
    final var customer = new Customer();
    customer.setName(name);
    customer.setEmail(email);
    customer.setCpf(cpf);
    return customerRepository.save(customer);
  }

}
