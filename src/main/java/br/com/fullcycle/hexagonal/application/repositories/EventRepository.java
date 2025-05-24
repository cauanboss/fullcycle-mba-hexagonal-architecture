package br.com.fullcycle.hexagonal.application.repositories;

import java.util.Optional;

import br.com.fullcycle.hexagonal.application.domain.event.Event;
import br.com.fullcycle.hexagonal.application.domain.event.EventId;
import br.com.fullcycle.hexagonal.application.domain.person.Name;

public interface EventRepository {

    Optional<Event> eventOfId(EventId eventId);

    Optional<Event> eventOfName(Name name);

    Optional<Event> eventOfDate(String date);

    Event create(Event event);

    Event update(Event event);

    void deleteAll();

}
