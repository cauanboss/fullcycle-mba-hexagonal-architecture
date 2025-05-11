package br.com.fullcycle.hexagonal.application.domain.event.ticket;

import java.time.Instant;
import java.util.Objects;

import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.event.EventId;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public class Ticket {
    private final TicketId ticketId;
    private CustomerId customerId;
    private EventId eventId;
    private TicketStatus status;
    private Instant paidAt;
    private Instant reservedAt;

    public Ticket(
            final TicketId ticketId,
            final EventId eventId,
            final CustomerId customerId,
            final TicketStatus status,
            final Instant paidAt,
            final Instant reservedAt) {
        this.ticketId = ticketId;
        this.setEventId(eventId);
        this.setCustomerId(customerId);
        this.setStatus(status);
        this.setPaidAt(paidAt);
        this.setReservedAt(reservedAt);
    }

    public static Ticket newTicket(final EventId eventId, final CustomerId customerId) {
        return new Ticket(TicketId.unique(), eventId, customerId, TicketStatus.PENDING, null, Instant.now());
    }

    public TicketId ticketId() {
        return this.ticketId;
    }

    public EventId eventId() {
        return this.eventId;
    }

    public CustomerId customerId() {
        return this.customerId;
    }

    public TicketStatus status() {
        return this.status;
    }

    public Instant paidAt() {
        return this.paidAt;
    }

    public Instant reservedAt() {
        return this.reservedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(ticketId, ticket.ticketId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketId);
    }

    private void setCustomerId(final CustomerId customerId) {
        if (customerId == null) {
            throw new ValidationException("Invalid customerId for ticket", null);
        }
        this.customerId = customerId;
    }

    private void setEventId(final EventId eventId) {
        if (eventId == null) {
            throw new ValidationException("Invalid eventId for ticket", null);
        }
        this.eventId = eventId;
    }

    private void setStatus(final TicketStatus status) {
        if (status == null) {
            throw new ValidationException("Invalid status for ticket", null);
        }
        this.status = status;
    }

    private void setPaidAt(final Instant paidAt) {
        this.paidAt = paidAt;
    }

    private void setReservedAt(final Instant reservedAt) {
        if (reservedAt == null) {
            throw new ValidationException("Invalid reservedAt for ticket", null);
        }
        this.reservedAt = reservedAt;
    }

}
