package br.com.fullcycle.hexagonal.infrastructure.jpa.entities;

import br.com.fullcycle.hexagonal.application.domain.event.Event;
import br.com.fullcycle.hexagonal.application.domain.event.EventTicket;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity(name = "Events")
@Table(name = "events")
public class EventEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String name;

  private LocalDate date;

  private int totalSpots;

  @Column(name = "partner_id")
  private UUID partnerId;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "event")
  private Set<EventTicketEntity> tickets;

  public EventEntity() {
    this.tickets = new HashSet<>();
  }

  public EventEntity(UUID id, String name, LocalDate date, int totalSpots, UUID partnerId) {
    this();
    this.id = id;
    this.name = name;
    this.date = date;
    this.totalSpots = totalSpots;
    this.partnerId = partnerId;
  }

  public static EventEntity of(final Event event) {
    final var eventEntity =
        new EventEntity(
            UUID.fromString(event.eventId().value()),
            event.name(),
            event.date(),
            event.totalSpots(),
            UUID.fromString(event.partnerId().value()));

    event
        .allTickets()
        .forEach(
            ticket -> {
              eventEntity.addTicket(ticket);
            });

    return eventEntity;
  }

  private void addTicket(final EventTicket ticket) {
    this.tickets.add(EventTicketEntity.of(this, ticket));
  }

  public Event toEvent() {
    return Event.restore(
        id.toString(),
        name,
        date.toString(),
        totalSpots,
        partnerId.toString(),
        this.tickets.stream().map(EventTicketEntity::toEventTicket).collect(Collectors.toSet()));
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public int getTotalSpots() {
    return totalSpots;
  }

  public void setTotalSpots(int totalSpots) {
    this.totalSpots = totalSpots;
  }

  public UUID getPartnerId() {
    return partnerId;
  }

  public void setPartnerId(UUID partnerId) {
    this.partnerId = partnerId;
  }

  public Set<EventTicketEntity> getTickets() {
    return tickets;
  }

  public void setTickets(Set<EventTicketEntity> tickets) {
    this.tickets = tickets;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EventEntity event = (EventEntity) o;
    return Objects.equals(id, event.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
