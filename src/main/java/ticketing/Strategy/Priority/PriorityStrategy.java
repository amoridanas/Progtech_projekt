package ticketing.Strategy.Priority;

import java.time.LocalDateTime;

public interface PriorityStrategy {
    LocalDateTime calculateDueDate(LocalDateTime createdAt);
}
