package ticketing.Observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationService {
    private final Map<Long, List<String>> notifications = new HashMap<>();

    public void addNotification(Long userId, String message) {
        if (userId == null) return;
        notifications.computeIfAbsent(userId, k -> new ArrayList<>()).add(message);
    }

    public List<String> getNotificationsFor(Long userId) {
        return notifications.getOrDefault(userId, new ArrayList<>());
    }

    public void clearNotifications(Long userId) {
        notifications.remove(userId);
    }
}
