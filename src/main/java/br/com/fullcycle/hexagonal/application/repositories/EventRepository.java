package br.com.fullcycle.hexagonal.application.repositories;

import java.util.Optional;

import br.com.fullcycle.hexagonal.application.entities.Event;
import br.com.fullcycle.hexagonal.application.entities.EventId;

public interface EventRepository {

    Optional<Event> eventOfId(EventId eventId);

    Optional<Event> eventOfName(String name);

    Optional<Event> eventOfDate(String date);

    Event create(Event event);

    Event update(Event event);

}
