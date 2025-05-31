package ticketing.Strategy.Priority;

import java.time.LocalDateTime;

public class LowPriorityStrategy implements PriorityStrategy {
    @Override
    public LocalDateTime calculateDueDate(LocalDateTime createdAt) {
        return createdAt.plusWeeks(1);
    }
}