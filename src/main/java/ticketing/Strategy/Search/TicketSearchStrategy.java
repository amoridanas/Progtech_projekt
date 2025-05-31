package ticketing.Strategy.Search;

import ticketing.Model.Ticket;

import java.util.List;

public interface TicketSearchStrategy {
    List<Ticket> search(List<Ticket> tickets, String keyword);
}