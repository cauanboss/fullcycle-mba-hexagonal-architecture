package br.com.fullcycle.hexagonal.infrastructure.jpa.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.time.Instant;

import br.com.fullcycle.hexagonal.application.domain.event.Event;
import br.com.fullcycle.hexagonal.application.domain.event.EventId;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.TicketStatus;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.Ticket;

@Entity
@Table(name = "events")
public class EventEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String name;

  private LocalDate date;

  private int totalSpots;

  @ManyToOne(fetch = FetchType.LAZY)
  private PartnerEntity partner;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
  private Set<TicketEntity> tickets;

  public EventEntity() {
    this.tickets = new HashSet<>();
  }

  public EventEntity(UUID id, String name, LocalDate date, int totalSpots, Set<TicketEntity> tickets) {
    this.id = id;
    this.name = name;
    this.date = date;
    this.totalSpots = totalSpots;
    this.tickets = tickets != null ? tickets : new HashSet<>();
  }

  public static EventEntity of(final Event event) {
    return new EventEntity(
        UUID.fromString(event.eventId().value()),
        event.name(),
        event.date(),
        event.totalSpots(),
        event.allTickets().stream()
            .map(eventTicket -> {
              var ticket = new Ticket(
                  eventTicket.ticketId(),
                  eventTicket.eventId(),
                  eventTicket.customerId(),
                  TicketStatus.PENDING,
                  null,
                  Instant.now());
              return TicketEntity.of(ticket);
            })
            .collect(Collectors.toSet()));
  }

  public Event toEvent() {
    return new Event(EventId.with(id.toString()), name, date.toString(), totalSpots, partner.toPartner().partnerId());
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

  public PartnerEntity getPartner() {
    return partner;
  }

  public void setPartner(PartnerEntity partner) {
    this.partner = partner;
  }

  public Set<TicketEntity> getTickets() {
    return tickets;
  }

  public void setTickets(Set<TicketEntity> tickets) {
    this.tickets = tickets;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    EventEntity event = (EventEntity) o;
    return Objects.equals(id, event.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
