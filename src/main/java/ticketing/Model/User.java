package ticketing.Model;

import java.time.LocalDateTime;



public class User {
    private long id;
    private String username;
    private String passwordHash;
    private String fullName;
    private LocalDateTime createdAt;
    private String role;

    public User() {

    }

    public User(long id, String username, String passwordHash, String fullName, LocalDateTime createdAt, String role) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.createdAt = createdAt;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public String getRole() {
        return role;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public void setRole(String role) {
        this.role = role;
    }
    @Override
    public String toString() {
        return fullName + " (" + username + ")";
    }
}