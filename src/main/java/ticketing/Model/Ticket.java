package ticketing.Model;

import java.time.LocalDateTime;

public class Ticket {
    private long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private long createdBy;
    private LocalDateTime createdAt;
    private Long assignedTo;
    private LocalDateTime due;
    private String type;  // lehet "REQUEST" vagy "FAILURE"

    // Konstruktor új jegy létrehozásához (alap, ha nincs type megadva)
    public Ticket(String title, String description, String priority, long createdBy) {
        this.title = title;
        this.description = description;
        this.status = "NEW";
        this.priority = priority;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
        this.assignedTo = null;

        // Ha a prioritás CRITICAL, akkor határidő 2 órán belül
        if ("CRITICAL".equalsIgnoreCase(priority)) {
            this.due = createdAt.plusHours(2);
        } else {
            this.due = null;
        }

        this.type = null;
    }

    // Konstruktor új jegyhez type-pal (Factory számára)
    public Ticket(String title, String description, String priority, long createdBy, String type) {
        this(title, description, priority, createdBy);
        this.type = type;
    }

    // Konstruktor adatbázisból való betöltéshez
    public Ticket(long id, String title, String description, String status, String priority, long createdBy,
                  LocalDateTime createdAt, Long assignedTo, LocalDateTime due, String type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.assignedTo = assignedTo;
        this.due = due;
        this.type = type;
    }

    // Getters
    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getPriority() {
        return priority;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getAssignedTo() {
        return assignedTo;
    }

    public LocalDateTime getDue() {
        return due;
    }

    public String getType() {
        return type;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setAssignedTo(Long assignedTo) {
        this.assignedTo = assignedTo;
    }

    public void setDue(LocalDateTime due) {
        this.due = due;
    }

    public void setType(String type) {
        this.type = type;
    }
    public void setId(long id) {
        this.id = id;
    }
}