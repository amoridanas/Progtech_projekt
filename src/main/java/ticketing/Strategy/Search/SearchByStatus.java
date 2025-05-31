package ticketing.Strategy.Search;

import ticketing.Model.Ticket;

import java.util.List;
import java.util.stream.Collectors;

public class SearchByStatus implements TicketSearchStrategy {

    @Override
    public List<Ticket> search(List<Ticket> tickets, String status) {
        return tickets.stream()
                .filter(t -> t.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }
}