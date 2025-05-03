package br.com.fullcycle.hexagonal.infrastructure.services;

import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;
import br.com.fullcycle.hexagonal.infrastructure.models.Event;
import br.com.fullcycle.hexagonal.infrastructure.models.Ticket;
import br.com.fullcycle.hexagonal.infrastructure.repositories.EventRepository;
import br.com.fullcycle.hexagonal.infrastructure.repositories.TicketRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private EventRepository eventRepository;

  @Autowired
  private TicketRepository ticketRepository;

  @Transactional
  public Event save(Event event) {
    return eventRepository.save(event);
  }

  @Transactional(readOnly = true)
  public Optional<Event> findById(Long id) {
    return eventRepository.findById(id);
  }

  @Transactional(readOnly = true)
  public Optional<Ticket> findTicketByEventIdAndCustomerId(Long id, Long customerId) {
    return ticketRepository.findByEventIdAndCustomerId(id, customerId);
  }
}
