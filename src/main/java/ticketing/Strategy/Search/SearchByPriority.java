package ticketing.Strategy.Search;

import ticketing.Model.Ticket;

import java.util.List;
import java.util.stream.Collectors;

public class SearchByPriority implements TicketSearchStrategy {

    @Override
    public List<Ticket> search(List<Ticket> tickets, String priority) {
        return tickets.stream()
                .filter(t -> t.getPriority().equalsIgnoreCase(priority))
                .collect(Collectors.toList());
    }
}