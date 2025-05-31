package ticketing.Strategy.Priority;

import java.time.LocalDateTime;

public class HighPriorityStrategy implements PriorityStrategy {
    @Override
    public LocalDateTime calculateDueDate(LocalDateTime createdAt) {
        return createdAt.plusDays(1);
    }
}