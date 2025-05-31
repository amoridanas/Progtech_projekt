package ticketing.Strategy.Priority;

import ticketing.Model.Priority;

public class PriorityStrategyFactory {
    public static PriorityStrategy getStrategy(Priority priority) {
        return switch (priority) {
            case LOW -> new LowPriorityStrategy();
            case MEDIUM -> new MediumPriorityStrategy();
            case HIGH -> new HighPriorityStrategy();
            case CRITICAL -> new CriticalPriorityStrategy();
        };
    }
}
