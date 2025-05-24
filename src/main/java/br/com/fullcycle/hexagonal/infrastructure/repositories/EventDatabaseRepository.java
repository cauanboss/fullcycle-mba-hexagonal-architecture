package br.com.fullcycle.hexagonal.infrastructure.repositories;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.fullcycle.hexagonal.application.domain.event.Event;
import br.com.fullcycle.hexagonal.application.domain.event.EventId;
import br.com.fullcycle.hexagonal.application.domain.person.Name;
import br.com.fullcycle.hexagonal.application.repositories.EventRepository;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.EventEntity;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.EventJpaRepository;

@Component
public class EventDatabaseRepository implements EventRepository {

    private final EventJpaRepository eventJpaRepository;

    public EventDatabaseRepository(EventJpaRepository eventJpaRepository) {
        this.eventJpaRepository = eventJpaRepository;
    }

    @Override
    public Optional<Event> eventOfId(EventId eventId) {
        return this.eventJpaRepository.findById(UUID.fromString(eventId.value()))
                .map(it -> it.toEvent());
    }

    @Override
    public Optional<Event> eventOfName(Name name) {
        return this.eventJpaRepository.findByName(name.value())
                .map(it -> it.toEvent());
    }

    @Override
    public Optional<Event> eventOfDate(String date) {
        return this.eventJpaRepository.findByDate(LocalDate.parse(date))
                .map(it -> it.toEvent());
    }

    @Override
    @Transactional
    public Event create(Event event) {
        return this.eventJpaRepository.save(EventEntity.of(event))
                .toEvent();
    }

    @Override
    @Transactional
    public Event update(Event event) {
        return this.eventJpaRepository.save(EventEntity.of(event))
                .toEvent();
    }

    @Override
    @Transactional
    public void deleteAll() {
        this.eventJpaRepository.deleteAll();
    }
}
