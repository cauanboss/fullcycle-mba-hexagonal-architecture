package br.com.fullcycle.hexagonal.application.usecases.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.com.fullcycle.hexagonal.integrationTest;
import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.domain.event.Event;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.TicketStatus;
import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;
import br.com.fullcycle.hexagonal.application.repositories.EventRepository;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;
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

  @Autowired
  private PartnerRepository partnerRepository;

  @BeforeEach
  void tearDown() {
    eventRepository.deleteAll();
    customerRepository.deleteAll();
    partnerRepository.deleteAll();
  }

  @Test
  @DisplayName("Deve criar um evento")
  public void testCreate() throws Exception {

    final var aPartner = savePartner("John Doe", "58803167000170", "john.doe@gmail.com");
    final var aEvent = saveEvent("Disney on Ice", 10, aPartner);
    final var aCustomer = saveCustomer("John Doe", "john.doe@gmail.com", "12345678901");
    final var expectedEventId = aEvent.eventId().value();

    final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(aEvent.eventId().value(),
        aCustomer.customerId().value());
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
    final var expectedEventId = UUID.randomUUID().toString();
    final var aCustomer = saveCustomer("John Doe", "john.doe@gmail.com", "12345678901");
    final var expectedCustomerId = aCustomer.customerId().value();

    final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(expectedEventId,
        expectedCustomerId);
    final var actualException = assertThrows(ValidationException.class, () -> usecase.execute(subscribeInput));

    assertEquals(expectedError, actualException.getMessage());
  }

  @Test
  @DisplayName("não deve criar um ticket com um cliente não encontrado")
  public void testReserveTicketWithoutCustomer() throws Exception {

    // given
    final var expectedError = "Customer not found";
    final var aPartner = savePartner("John Doe", "58803167000170", "john.doe@gmail.com");
    final var aEvent = saveEvent("Disney on Ice", 10, aPartner);
    final var expectedEventId = aEvent.eventId().value();
    final var expectedCustomerId = UUID.randomUUID().toString();

    final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(expectedEventId,
        expectedCustomerId);

    final var actualException = assertThrows(ValidationException.class, () -> usecase.execute(subscribeInput));

    assertEquals(expectedError, actualException.getMessage());
  }

  @Test
  @DisplayName("não deve criar um ticket com um cliente já inscrito no evento")
  public void testReserveTicketWithCustomerAlreadySubscribed() throws Exception {

    // given
    final var expectedError = "Customer already registered for this event";

    final var aPartner = savePartner("John Doe", "58803167000170", "john.doe@gmail.com");
    final var aEvent = saveEvent("Disney on Ice", 10, aPartner);
    final var aCustomer = saveCustomer("John Doe", "john.doe@gmail.com", "12345678901");
    final var expectedCustomerId = aCustomer.customerId().value();

    final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(aEvent.eventId().value(),
        expectedCustomerId);

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

    final var aPartner = savePartner("John Doe", "58803167000170", "john.doe@gmail.com");
    final var aEvent = saveEvent("Disney on Ice", 0, aPartner);
    final var aCustomer = saveCustomer("John Doe", "john.doe@gmail.com", "12345678901");

    final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(aEvent.eventId().value(),
        aCustomer.customerId().value());

    final var actualException = assertThrows(ValidationException.class, () -> usecase.execute(subscribeInput));

    // then
    assertEquals(expectedError, actualException.getMessage());
  }

  private Event saveEvent(final String name, final int totalSpots, final Partner partner) {
    return eventRepository.create(Event.newEvent(name, "2021-01-01", totalSpots, partner));
  }

  private Customer saveCustomer(final String name, final String email, final String cpf) {
    return customerRepository.create(Customer.newCustomer(name, cpf, email));
  }

  private Partner savePartner(final String name, final String cnpj, final String email) {
    return partnerRepository.create(Partner.newPartner(name, cnpj, email));
  }

}
