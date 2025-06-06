package br.com.fullcycle.hexagonal.infrastructure.controllers;

import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.domain.event.EventId;
import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;
import br.com.fullcycle.hexagonal.application.repositories.EventRepository;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;
import br.com.fullcycle.hexagonal.application.usecases.event.CreateEventUseCase;
import br.com.fullcycle.hexagonal.infrastructure.Main;
import br.com.fullcycle.hexagonal.infrastructure.dtos.NewEventDTO;
import br.com.fullcycle.hexagonal.infrastructure.dtos.SubscribeDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(classes = Main.class)
class EventControllerTest {

  @Autowired private MockMvc mvc;

  @Autowired private ObjectMapper mapper;

  @Autowired private CustomerRepository customerRepository;

  @Autowired private PartnerRepository partnerRepository;

  @Autowired private EventRepository eventRepository;

  private Customer johnDoe;
  private Partner disney;

  @BeforeEach
  void setUp() {
    johnDoe =
        customerRepository.create(
            Customer.newCustomer("John Doe", "37072097090", "john@gmail.com"));
    disney =
        partnerRepository.create(
            Partner.newPartner("Disney", "23180616000197", "disney@gmail.com"));
  }

  @AfterEach
  void tearDown() {
    eventRepository.deleteAll();
    customerRepository.deleteAll();
    partnerRepository.deleteAll();
  }

  @Test
  @DisplayName("Deve criar um evento")
  public void testCreate() throws Exception {
    final var event = new NewEventDTO("Disney on Ice", "2021-01-01", 100, disney.partnerId());

    final var createResult =
        this.mvc
            .perform(
                MockMvcRequestBuilders.post("/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(event)))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").isString())
            .andReturn()
            .getResponse()
            .getContentAsByteArray();

    var actualResponse = mapper.readValue(createResult, CreateEventUseCase.Output.class);
    Assertions.assertEquals(event.date(), actualResponse.date());
    Assertions.assertEquals(event.totalSpots(), actualResponse.totalSpots());
    Assertions.assertEquals(event.name(), actualResponse.name());
  }

  @Test
  @Transactional
  @DisplayName("Deve comprar um ticket de um evento")
  public void testReserveTicket() throws Exception {

    var event = new NewEventDTO("Disney on Ice", "2021-01-01", 100, disney.partnerId());

    final var createResult =
        this.mvc
            .perform(
                MockMvcRequestBuilders.post("/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(event)))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").isString())
            .andReturn()
            .getResponse()
            .getContentAsByteArray();

    var eventId = mapper.readValue(createResult, CreateEventUseCase.Output.class).id();

    var sub = new SubscribeDTO(eventId.toString(), johnDoe.customerId().value());

    this.mvc
        .perform(
            MockMvcRequestBuilders.post("/events/{id}/subscribe", eventId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(sub)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsByteArray();

    var actualEvent = eventRepository.eventOfId(EventId.with(eventId));
    // var actualEvent = eventRepository.findById(UUID.fromString(eventId)).get();
    Assertions.assertEquals(1, actualEvent.get().allTickets().size());
  }
}
