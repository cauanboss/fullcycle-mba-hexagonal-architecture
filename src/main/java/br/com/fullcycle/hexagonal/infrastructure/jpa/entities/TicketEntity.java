package br.com.fullcycle.hexagonal.infrastructure.jpa.entities;

import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.event.EventId;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.Ticket;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.TicketId;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.TicketStatus;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "Tickets")
@Table(name = "tickets")
public class TicketEntity {

  @Id private UUID id;

  @Column(name = "customer_id")
  private UUID customerId;

  @Column(name = "event_id")
  private UUID eventId;

  @Enumerated(EnumType.STRING)
  private TicketStatus status;

  private Instant paidAt;

  private Instant reservedAt;

  public TicketEntity() {}

  public TicketEntity(
      UUID id,
      UUID customerId,
      UUID eventId,
      TicketStatus status,
      Instant paidAt,
      Instant reservedAt) {
    this.id = id;
    this.customerId = customerId;
    this.eventId = eventId;
    this.status = status;
    this.paidAt = paidAt;
    this.reservedAt = reservedAt;
  }

  public static TicketEntity of(final Ticket ticket) {
    return new TicketEntity(
        UUID.fromString(ticket.ticketId().value()),
        UUID.fromString(ticket.customerId().value()),
        UUID.fromString(ticket.eventId().value()),
        ticket.status(),
        ticket.paidAt(),
        ticket.reservedAt());
  }

  public Ticket toTicket() {
    return new Ticket(
        TicketId.with(this.id.toString()),
        EventId.with(this.eventId.toString()),
        CustomerId.with(this.customerId.toString()),
        this.status,
        this.paidAt,
        this.reservedAt);
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getCustomerId() {
    return customerId;
  }

  public void setCustomerId(UUID customerId) {
    this.customerId = customerId;
  }

  public UUID getEventId() {
    return eventId;
  }

  public void setEventId(UUID eventId) {
    this.eventId = eventId;
  }

  public TicketStatus getStatus() {
    return status;
  }

  public void setStatus(TicketStatus status) {
    this.status = status;
  }

  public Instant getPaidAt() {
    return paidAt;
  }

  public void setPaidAt(Instant paidAt) {
    this.paidAt = paidAt;
  }

  public Instant getReservedAt() {
    return reservedAt;
  }

  public void setReservedAt(Instant reservedAt) {
    this.reservedAt = reservedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TicketEntity ticket = (TicketEntity) o;
    return Objects.equals(customerId, ticket.customerId) && Objects.equals(eventId, ticket.eventId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customerId, eventId);
  }
}
