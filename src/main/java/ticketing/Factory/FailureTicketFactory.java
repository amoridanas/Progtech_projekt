package ticketing.Factory;

import ticketing.Model.Priority;
import ticketing.Model.Ticket;
import ticketing.Strategy.Priority.PriorityStrategyFactory;

public class FailureTicketFactory implements TicketAbstractFactory {
    @Override
    public Ticket create(String title, String description, Priority priority, long createdBy) {
        Ticket t = new Ticket(title, description, priority.name(), createdBy);
        t.setType("Failure");
        t.setDue(PriorityStrategyFactory.getStrategy(priority).calculateDueDate(t.getCreatedAt()));
        return t;
    }
}