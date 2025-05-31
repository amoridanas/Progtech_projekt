package ticketing.Factory;

import ticketing.Model.Priority;
import ticketing.Model.Ticket;

public interface TicketAbstractFactory {
    Ticket create(String title, String description, Priority priority, long createdBy);
}
