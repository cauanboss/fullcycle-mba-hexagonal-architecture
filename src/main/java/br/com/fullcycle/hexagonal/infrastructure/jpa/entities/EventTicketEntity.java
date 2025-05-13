package br.com.fullcycle.hexagonal.infrastructure.jpa.entities;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.UUID;

import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.event.EventId;
import br.com.fullcycle.hexagonal.application.domain.event.EventTicket;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.TicketId;

@Entity
@Table(name = "tickets")
public class EventTicketEntity {

  @Id
  private UUID ticketId;

  private UUID customerId;

  private Integer ordering;

  @ManyToOne(fetch = FetchType.LAZY)
  private EventEntity event;

  public EventTicketEntity() {
  }

  public EventTicketEntity(
      UUID ticketId,
      UUID customerId,
      final EventEntity event,
      final Integer ordering) {
    this.ticketId = ticketId;
    this.customerId = customerId;
    this.event = event;
    this.ordering = ordering;
  }

  public static EventTicketEntity of(final EventEntity event, final EventTicket eventTicket) {
    return new EventTicketEntity(
        UUID.fromString(eventTicket.ticketId().value()),
        UUID.fromString(eventTicket.customerId().value()),
        event,
        eventTicket.ordering());
  }

  public EventTicket toEventTicket() {
    return new EventTicket(
        TicketId.with(this.ticketId.toString()),
        EventId.with(this.event.getId().toString()),
        CustomerId.with(this.customerId.toString()),
        this.ordering);
  }

  public void setTicketId(UUID ticketId) {
    this.ticketId = ticketId;
  }

  public UUID getCustomerId() {
    return customerId;
  }

  public int getOrdering() {
    return ordering;
  }

  public void setOrdering(int ordering) {
    this.ordering = ordering;
  }

  public void setCustomerId(UUID customerId) {
    this.customerId = customerId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    EventTicketEntity eventTicket = (EventTicketEntity) o;
    return Objects.equals(customerId, eventTicket.customerId)
        && Objects.equals(event.getId(), eventTicket.event.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(customerId, event.getId());
  }
}
