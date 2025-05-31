package ticketing.Repository;

import ticketing.App.Session;
import ticketing.Database.DatabaseConnection;
import ticketing.Model.Ticket;
import ticketing.Model.User;
import ticketing.Utils.LoggerUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketRepo {
    public static boolean insert(Ticket ticket) {
        String sql = "INSERT INTO tickets (title, description, status, priority, created_by, assigned_to, due_to, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, ticket.getTitle());
            stmt.setString(2, ticket.getDescription());
            stmt.setString(3, ticket.getStatus());
            stmt.setString(4, ticket.getPriority());
            stmt.setLong(5, ticket.getCreatedBy());

            if (ticket.getAssignedTo() != null) {
                stmt.setLong(6, ticket.getAssignedTo());
            } else {
                stmt.setNull(6, java.sql.Types.BIGINT);
            }

            if (ticket.getDue() != null) {
                stmt.setTimestamp(7, Timestamp.valueOf(ticket.getDue()));
            } else {
                stmt.setNull(7, java.sql.Types.TIMESTAMP);
            }

            stmt.setString(8, ticket.getType());

            boolean success = stmt.executeUpdate() > 0;

            if (success) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    long ticketId = rs.getLong(1);
                    ticket.setId(ticketId);
                }

                LoggerUtil.log("Új jegy létrehozva: \"" + ticket.getTitle() + "\" | Típus: " + ticket.getType() + " | Létrehozta: "
                        + Session.getLoggedInUser().getUsername() + " (ID: " + ticket.getCreatedBy() + ")");

                // Értesítés küldése minden adminnak
                List<User> admins = UserRepo.getAllAdmins();
                for (User admin : admins) {
                    String message = "Új jegy érkezett: \"" + ticket.getTitle() + "\" #" + ticket.getId();
                    NotificationRepo.create(admin.getId(), message);
                }
            }

            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static List<Ticket> getAll() {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM tickets ORDER BY created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Ticket ticket = new Ticket(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("status"),
                        rs.getString("priority"),
                        rs.getLong("created_by"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getObject("assigned_to") != null ? rs.getLong("assigned_to") : null,
                        rs.getObject("due_to") != null ? rs.getTimestamp("due_to").toLocalDateTime() : null,
                        rs.getString("type")
                );
                tickets.add(ticket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tickets;
    }

    public static List<Ticket> getAllByUser(long userId) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM tickets WHERE created_by = ? ORDER BY created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Ticket ticket = new Ticket(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("status"),
                        rs.getString("priority"),
                        rs.getLong("created_by"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getObject("assigned_to") != null ? rs.getLong("assigned_to") : null,
                        rs.getObject("due_to") != null ? rs.getTimestamp("due_to").toLocalDateTime() : null,
                        rs.getString("type")
                );
                tickets.add(ticket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tickets;
    }

    public static Ticket getById(long id) {
        String sql = "SELECT * FROM tickets WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Ticket(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("status"),
                        rs.getString("priority"),
                        rs.getLong("created_by"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getObject("assigned_to") != null ? rs.getLong("assigned_to") : null,
                        rs.getObject("due_to") != null ? rs.getTimestamp("due_to").toLocalDateTime() : null,
                        rs.getString("type")
                );
            } else {
                System.err.println("Nincs találat az ID-re: " + id);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean update(Ticket ticket) {
        String sql = "UPDATE tickets SET title=?, description=?, status=?, priority=?, assigned_to=?, due_to=?, type=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ticket.getTitle());
            stmt.setString(2, ticket.getDescription());
            stmt.setString(3, ticket.getStatus());
            stmt.setString(4, ticket.getPriority());

            if (ticket.getAssignedTo() != null) {
                stmt.setLong(5, ticket.getAssignedTo());
            } else {
                stmt.setNull(5, java.sql.Types.BIGINT);
            }

            if (ticket.getDue() != null) {
                stmt.setTimestamp(6, Timestamp.valueOf(ticket.getDue()));
            } else {
                stmt.setNull(6, java.sql.Types.TIMESTAMP);
            }

            stmt.setString(7, ticket.getType());
            stmt.setLong(8, ticket.getId());

            boolean updated = stmt.executeUpdate() > 0;

            if (updated) {
                LoggerUtil.log("Jegy frissítve (ID: " + ticket.getId() + "), típus: " + ticket.getType() +
                        " – felhasználó: " + Session.getLoggedInUser().getUsername());

                // Értesítések csak admin által végzett módosításnál
                if ("admin".equalsIgnoreCase(Session.getLoggedInUser().getRole())) {

                    // Értesítés a létrehozónak
                    long creatorId = ticket.getCreatedBy();
                    String messageToUser = "A jegyed módosult (ID: " + ticket.getId() + ", cím: " + ticket.getTitle() + ").";
                    NotificationRepo.create(creatorId, messageToUser);

                    // Értesítés az új hozzárendelt adminnak (ha van)
                    if (ticket.getAssignedTo() != null) {
                        long assignedId = ticket.getAssignedTo();
                        String messageToAdmin = "Új jegy hozzád rendelve (ID: " + ticket.getId() + ", cím: " + ticket.getTitle() + ").";
                        NotificationRepo.create(assignedId, messageToAdmin);
                    }
                }
            }

            return updated;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
