package br.com.fullcycle.hexagonal.application.usecases.event;

import br.com.fullcycle.hexagonal.application.UseCase;
import br.com.fullcycle.hexagonal.application.domain.event.Event;
import br.com.fullcycle.hexagonal.application.domain.partner.PartnerId;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.repositories.EventRepository;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;

public class CreateEventUseCase
    extends UseCase<CreateEventUseCase.Input, CreateEventUseCase.Output> {

  private final EventRepository eventRepository;
  private final PartnerRepository partnerRepository;

  public CreateEventUseCase(EventRepository eventRepository, PartnerRepository partnerRepository) {
    this.eventRepository = eventRepository;
    this.partnerRepository = partnerRepository;
  }

  @Override
  public Output execute(Input input) {
    final var partner = partnerRepository.partnerOfId(PartnerId.with(input.partnerId))
        .orElseThrow(() -> new ValidationException("Partner not found", null));

    final var event = eventRepository.create(Event.newEvent(input.name, input.date, input.totalSpots, partner));

    return new Output(event.eventId().value(), event.name(), event.date().toString(),
        event.totalSpots(), partner.partnerId());
  }

  public record Input(String name, String date, Integer totalSpots, String partnerId) {
  }

  public record Output(String id, String name, String date, Integer totalSpots, String partnerId) {
  }
}
