package br.com.fullcycle.hexagonal.infrastructure.configuration;

import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;
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

  private final CustomerService customerService;
  private final EventService eventService;
  private final PartnerService partnerService;
  private final CustomerRepository customerRepository;
  private final PartnerRepository partnerRepository;

  public UseCaseConfig(final CustomerService customerService, final EventService eventService,
      final PartnerService partnerService, final CustomerRepository customerRepository,
      final PartnerRepository partnerRepository) {
    this.customerService = Objects.requireNonNull(customerService);
    this.eventService = Objects.requireNonNull(eventService);
    this.partnerService = Objects.requireNonNull(partnerService);
    this.customerRepository = Objects.requireNonNull(customerRepository);
    this.partnerRepository = Objects.requireNonNull(partnerRepository);
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
    return new CreateEventUseCase(eventService, partnerService);
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
