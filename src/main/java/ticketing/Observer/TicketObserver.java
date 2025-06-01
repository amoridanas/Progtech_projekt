package ticketing.Observer;

import ticketing.Model.Ticket;

public interface TicketObserver {
    void update(Ticket ticket, String message);
}
