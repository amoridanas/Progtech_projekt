package ticketing.Strategy.Search;

import ticketing.Model.Ticket;

import java.util.List;
import java.util.stream.Collectors;

public class SearchByKeyword implements TicketSearchStrategy {

    @Override
    public List<Ticket> search(List<Ticket> tickets, String keyword) {
        return tickets.stream()
                .filter(t -> t.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}