package ticketing.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Notification {
    private long id;
    private long userId;
    private String message;
    private LocalDateTime createdAt;

    public Notification(long id, long userId, String message, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.createdAt = createdAt;
    }

    public long getId() { return id; }
    public long getUserId() { return userId; }
    public String getMessage() { return message; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public String getCreatedAtFormatted() {
        return createdAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm"));
    }
}
