package br.com.fullcycle.hexagonal.infrastructure.configuration;

import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;
import br.com.fullcycle.hexagonal.application.repositories.EventRepository;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;
import br.com.fullcycle.hexagonal.application.usecases.CreateCustomerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.CreateEventUseCase;
import br.com.fullcycle.hexagonal.application.usecases.CreatePartnerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.GetCustomerByIdUseCase;
import br.com.fullcycle.hexagonal.application.usecases.GetPartnerByIdUseCase;
import br.com.fullcycle.hexagonal.application.usecases.SubscribeCustomerToEventUseCase;
import br.com.fullcycle.hexagonal.infrastructure.services.CustomerService;
import br.com.fullcycle.hexagonal.infrastructure.services.EventService;
import br.com.fullcycle.hexagonal.infrastructure.services.PartnerService;

import java.util.Objects;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

  private final CustomerRepository customerRepository;
  private final EventRepository eventRepository;
  private final PartnerRepository partnerRepository;
  private final CustomerService customerService;
  private final EventService eventService;

  public UseCaseConfig(final CustomerRepository customerRepository,
      final EventRepository eventRepository, final PartnerRepository partnerRepository,
      final CustomerService customerService, final EventService eventService,
      final PartnerService partnerService) {
    this.customerRepository = Objects.requireNonNull(customerRepository);
    this.eventRepository = Objects.requireNonNull(eventRepository);
    this.partnerRepository = Objects.requireNonNull(partnerRepository);
    this.customerService = Objects.requireNonNull(customerService);
    this.eventService = Objects.requireNonNull(eventService);
  }

  @Bean
  public CreateCustomerUseCase createCustomerUseCase() {
    return new CreateCustomerUseCase(customerRepository);
  }

  @Bean
  public CreatePartnerUseCase createPartnerUseCase() {
    return new CreatePartnerUseCase(partnerRepository);
  }

  @Bean
  public CreateEventUseCase createEventUseCase() {
    return new CreateEventUseCase(eventRepository, partnerRepository);
  }

  @Bean
  public SubscribeCustomerToEventUseCase subscribeCustomerToEvent() {
    return new SubscribeCustomerToEventUseCase(eventService, customerService);
  }

  @Bean
  public GetCustomerByIdUseCase getCustomerByIdUseCase() {
    return new GetCustomerByIdUseCase(customerRepository);
  }

  @Bean
  public GetPartnerByIdUseCase getPartnerByIdUseCase() {
    return new GetPartnerByIdUseCase(partnerRepository);
  }

}
