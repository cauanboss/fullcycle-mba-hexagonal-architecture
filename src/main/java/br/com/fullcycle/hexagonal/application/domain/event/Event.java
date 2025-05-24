package br.com.fullcycle.hexagonal.application.domain.event;

import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.Ticket;
import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.domain.partner.PartnerId;
import br.com.fullcycle.hexagonal.application.domain.person.Name;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Event {

  private final EventId eventId;
  private Name name;
  private LocalDate date;
  private int totalSpots;
  private PartnerId partnerId;
  private Set<EventTicket> tickets;

  public Event(
      final EventId eventId,
      final String name,
      final String date,
      final Integer totalSpots,
      final PartnerId partnerId,
      final Set<EventTicket> tickets) {

    this(eventId, tickets);
    this.setName(name);
    this.setDate(date);
    this.setTotalSpots(totalSpots);
    this.setPartnerId(partnerId);
    this.tickets = tickets;
  }

  private Event(final EventId eventId, final Set<EventTicket> tickets) {
    if (eventId == null) {
      throw new ValidationException("Invalid value for EventId", null);
    }
    this.eventId = eventId;
    this.tickets = tickets != null ? tickets : new HashSet<>(0);
  }

  public static Event newEvent(
      final String name, final String date, final Integer totalSpots, final Partner partner) {
    return new Event(
        EventId.unique(),
        name,
        date,
        totalSpots,
        PartnerId.with(partner.partnerId()),
        new HashSet<>(0));
  }

  public Ticket resetTicket(final CustomerId customerId) {
    this.allTickets().stream()
        .filter(ticket -> ticket.customerId().equals(customerId))
        .findFirst()
        .ifPresent(
            ticket -> {
              throw new ValidationException("Customer already registered for this event", null);
            });

    if (this.totalSpots() < this.allTickets().size() + 1) {
      throw new ValidationException("Event sold out", null);
    }

    var newTicket = Ticket.newTicket(this.eventId(), customerId);

    this.tickets.add(
        new EventTicket(
            newTicket.ticketId(), this.eventId(), customerId, this.allTickets().size() + 1));

    return newTicket;
  }

  public static Event restore(
      final String eventId,
      final String name,
      final String date,
      final Integer totalSpots,
      final String partnerId,
      final Set<EventTicket> tickets) {
    return new Event(
        EventId.with(eventId), name, date, totalSpots, PartnerId.with(partnerId), tickets);
  }

  public EventId eventId() {
    return this.eventId;
  }

  public String name() {
    return this.name.value();
  }

  public LocalDate date() {
    return this.date;
  }

  public int totalSpots() {
    return this.totalSpots;
  }

  public PartnerId partnerId() {
    return this.partnerId;
  }

  public Set<EventTicket> allTickets() {
    return Collections.unmodifiableSet(this.tickets);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Event event = (Event) o;
    return Objects.equals(eventId, event.eventId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId);
  }

  private void setName(final String name) {
    if (name == null) {
      throw new ValidationException("Invalid value for name", null);
    }
    this.name = new Name(name);
  }

  private void setDate(final String date) {
    if (date == null || date.isEmpty()) {
      throw new ValidationException("Invalid value for date", null);
    }
    this.date = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
  }

  private void setTotalSpots(final Integer totalSpots) {
    if (totalSpots == null) {
      throw new ValidationException("Invalid value for totalSpots", null);
    }
    this.totalSpots = totalSpots;
  }

  private void setPartnerId(final PartnerId partnerId) {
    if (partnerId == null) {
      throw new ValidationException("Invalid value for partnerId", null);
    }
    this.partnerId = partnerId;
  }
}
