package br.com.fullcycle.hexagonal.infrastructure.controllers;

import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;
import br.com.fullcycle.hexagonal.application.usecases.customer.CreateCustomerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.customer.GetCustomerByIdUseCase;
import br.com.fullcycle.hexagonal.infrastructure.Main;
import br.com.fullcycle.hexagonal.infrastructure.dtos.NewCustomerDTO;
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

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(classes = Main.class)
public class CustomerControllerTest {

  @Autowired private MockMvc mvc;

  @Autowired private ObjectMapper mapper;

  @Autowired private CustomerRepository customerRepository;

  @AfterEach
  void tearDown() {
    customerRepository.deleteAll();
  }

  @Test
  @DisplayName("Deve criar um cliente")
  public void testCreate() throws Exception {

    var customer = new NewCustomerDTO("John Doe", "12345678901", "john.doe@gmail.com");

    final var result =
        this.mvc
            .perform(
                MockMvcRequestBuilders.post("/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(customer)))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.header().exists("Location"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").isString())
            .andReturn()
            .getResponse()
            .getContentAsByteArray();

    var actualResponse = mapper.readValue(result, CreateCustomerUseCase.Output.class);
    Assertions.assertEquals(customer.name(), actualResponse.name());
    Assertions.assertEquals(customer.cpf(), actualResponse.cpf());
    Assertions.assertEquals(customer.email(), actualResponse.email());
  }

  @Test
  @DisplayName("Não deve cadastrar um cliente com CPF duplicado")
  public void testCreateWithDuplicatedCPFShouldFail() throws Exception {

    var customer = new NewCustomerDTO("John Doe", "99999918901", "john.doe@gmail.com");

    // Cria o primeiro cliente
    this.mvc
        .perform(
            MockMvcRequestBuilders.post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customer)))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.header().exists("Location"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").isString())
        .andReturn()
        .getResponse()
        .getContentAsByteArray();

    customer = new NewCustomerDTO("John Doe", "99999918901", "john2@gmail.com");

    // Tenta criar o segundo cliente com o mesmo CPF
    this.mvc
        .perform(
            MockMvcRequestBuilders.post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customer)))
        .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
        .andExpect(MockMvcResultMatchers.content().string("Customer already exists"));
  }

  @Test
  @DisplayName("Não deve cadastrar um cliente com e-mail duplicado")
  public void testCreateWithDuplicatedEmailShouldFail() throws Exception {

    var customer = new NewCustomerDTO("John Doe", "90303007010", "john.doe400@gmail.com");

    // Cria o primeiro cliente
    this.mvc
        .perform(
            MockMvcRequestBuilders.post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customer)))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.header().exists("Location"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").isString())
        .andReturn()
        .getResponse()
        .getContentAsByteArray();

    customer = new NewCustomerDTO("John Doe", "37072097090", "john.doe400@gmail.com");

    // Tenta criar o segundo cliente com o mesmo CPF
    this.mvc
        .perform(
            MockMvcRequestBuilders.post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customer)))
        .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
        .andExpect(MockMvcResultMatchers.content().string("Customer already exists"));
  }

  @Test
  @DisplayName("Deve obter um cliente por id")
  public void testGet() throws Exception {
    try {
      var customer = new NewCustomerDTO("John Doe", "12345678909", "john.doe4444@gmail.com");

      final var createResult =
          this.mvc
              .perform(
                  MockMvcRequestBuilders.post("/customers")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(mapper.writeValueAsString(customer)))
              .andExpect(MockMvcResultMatchers.status().isCreated())
              .andExpect(MockMvcResultMatchers.header().exists("Location"))
              .andExpect(MockMvcResultMatchers.jsonPath("$.id").isString())
              .andReturn()
              .getResponse()
              .getContentAsByteArray();

      var customerId = mapper.readValue(createResult, CreateCustomerUseCase.Output.class).id();

      final var result =
          this.mvc
              .perform(MockMvcRequestBuilders.get("/customers/{id}", customerId))
              .andExpect(MockMvcResultMatchers.status().isOk())
              .andReturn()
              .getResponse()
              .getContentAsByteArray();

      var actualResponse = mapper.readValue(result, GetCustomerByIdUseCase.Output.class);
      System.out.println("Expected ID: " + customerId);
      System.out.println("Actual ID: " + actualResponse.id());
      Assertions.assertEquals(customerId, actualResponse.id());
      Assertions.assertEquals(customer.name(), actualResponse.name());
      Assertions.assertEquals(customer.cpf(), actualResponse.cpf());
      Assertions.assertEquals(customer.email(), actualResponse.email());
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      throw e;
    }
  }
}
