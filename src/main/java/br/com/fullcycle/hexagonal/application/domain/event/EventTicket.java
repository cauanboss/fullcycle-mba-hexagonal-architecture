package br.com.fullcycle.hexagonal.application.domain.event;

import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.TicketId;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import java.util.Objects;

public class EventTicket {
    private final TicketId ticketId;
    private final EventId eventId;
    private final CustomerId customerId;
    private Integer ordering;

    public EventTicket(final TicketId ticketId, final EventId eventId, final CustomerId customerId,
            final Integer ordering) {
        this.ticketId = ticketId;
        this.eventId = eventId;
        this.customerId = customerId;
        this.setOrdering(ordering);
    }

    public static EventTicket newEventTicket(final EventId eventId, final CustomerId customerId,
            final Integer ordering) {
        return new EventTicket(TicketId.unique(), eventId, customerId, ordering);
    }

    public TicketId ticketId() {
        return ticketId;
    }

    public EventId eventId() {
        return eventId;
    }

    public Integer ordering() {
        return ordering;
    }

    public CustomerId customerId() {
        return customerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        EventTicket that = (EventTicket) o;
        return Objects.equals(ticketId, that.ticketId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketId);
    }

    private void setOrdering(final Integer ordering) {
        if (ordering == null) {
            throw new ValidationException("Ordering is required", null);
        }
        this.ordering = ordering;
    }

}