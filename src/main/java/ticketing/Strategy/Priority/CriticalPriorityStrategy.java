package ticketing.Strategy.Priority;

import java.time.LocalDateTime;

public class CriticalPriorityStrategy implements PriorityStrategy {
    @Override
    public LocalDateTime calculateDueDate(LocalDateTime createdAt) {
        return createdAt.plusHours(2);
    }
}