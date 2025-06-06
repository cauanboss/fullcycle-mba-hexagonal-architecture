package br.com.fullcycle.hexagonal.application.repository;

import br.com.fullcycle.hexagonal.application.domain.event.Event;
import br.com.fullcycle.hexagonal.application.domain.event.EventId;
import br.com.fullcycle.hexagonal.application.domain.person.Name;
import br.com.fullcycle.hexagonal.application.repositories.EventRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class inMemoryEventRepository implements EventRepository {

  private final Map<String, Event> events;

  public inMemoryEventRepository() {
    this.events = new HashMap<>();
  }

  @Override
  public Optional<Event> eventOfId(EventId eventId) {
    return Optional.ofNullable(this.events.get(Objects.requireNonNull(eventId).value()));
  }

  @Override
  public Optional<Event> eventOfName(Name name) {
    return Optional.ofNullable(this.events.get(Objects.requireNonNull(name).value()));
  }

  @Override
  public Optional<Event> eventOfDate(String date) {
    return Optional.ofNullable(this.events.get(Objects.requireNonNull(date)));
  }

  @Override
  public Event create(Event event) {
    this.events.put(event.eventId().value(), event);
    return event;
  }

  @Override
  public Event update(Event event) {
    this.events.put(event.eventId().value(), event);
    return event;
  }

  @Override
  public void deleteAll() {
    this.events.clear();
  }
}
