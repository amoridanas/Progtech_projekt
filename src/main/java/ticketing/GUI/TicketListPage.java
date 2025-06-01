package ticketing.GUI;

import ticketing.App.Session;
import ticketing.Model.Ticket;
import ticketing.Model.User;
import ticketing.Repository.TicketRepo;
import ticketing.Repository.UserRepo;
import ticketing.Strategy.Search.SearchByKeyword;
import ticketing.Strategy.Search.SearchByPriority;
import ticketing.Strategy.Search.SearchByStatus;
import ticketing.Strategy.Search.TicketSearchStrategy;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TicketListPage {

    private final JFrame frame;
    private final JTable table;
    private final DefaultTableModel model;
    private final JComboBox<String> strategyCombo;
    private final JTextField searchField;
    private final JComboBox<String> statusDropdown;
    private final JComboBox<String> priorityDropdown;
    private final JButton searchButton;
    private final JButton refreshButton;

    public TicketListPage() {
        frame = new JFrame("📋 Hibajegyek listája");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1100, 600);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Hibajegyek", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        mainPanel.add(title, BorderLayout.NORTH);

        // Táblázat
        String[] columns = {"ID", "Cím", "Prioritás", "Státusz", "Létrehozta", "Létrehozva", "Határidő", "Hozzárendelve"};

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Alsó panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        strategyCombo = new JComboBox<>(new String[]{"Státusz", "Prioritás", "Kulcsszó"});
        searchField = new JTextField(15);
        statusDropdown = new JComboBox<>(new String[]{"NEW", "IN_PROGRESS", "RESOLVED", "CLOSED"});
        priorityDropdown = new JComboBox<>(new String[]{"LOW", "MEDIUM", "HIGH", "CRITICAL"});
        searchButton = new JButton("🔍 Keresés");
        refreshButton = new JButton("🔄 Frissítés");

        statusDropdown.setVisible(false);
        priorityDropdown.setVisible(false);

        bottomPanel.add(new JLabel("Keresés:"));
        bottomPanel.add(strategyCombo);
        bottomPanel.add(searchField);
        bottomPanel.add(statusDropdown);
        bottomPanel.add(priorityDropdown);
        bottomPanel.add(searchButton);
        bottomPanel.add(refreshButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        frame.add(mainPanel);
        frame.setVisible(true);

        // Táblázat dupla kattintás
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    long ticketId = (long) model.getValueAt(table.getSelectedRow(), 0);
                    new TicketDetailPage(ticketId);
                }
            }
        });

        // Induláskor betöltés
        User currentUser = Session.getLoggedInUser();
        if ("admin".equalsIgnoreCase(currentUser.getRole())) {
            loadData(TicketRepo.getAll());
        } else {
            loadData(TicketRepo.getAllByUser(currentUser.getId()));
        }

        strategyCombo.addActionListener(e -> toggleSearchInputs());

        searchButton.addActionListener(e -> handleSearch());
        refreshButton.addActionListener(e -> {
            if ("admin".equalsIgnoreCase(currentUser.getRole())) {
                loadData(TicketRepo.getAll());
            } else {
                loadData(TicketRepo.getAllByUser(currentUser.getId()));
            }
        });
    }

    private void toggleSearchInputs() {
        String selected = (String) strategyCombo.getSelectedItem();
        searchField.setVisible("Kulcsszó".equals(selected));
        statusDropdown.setVisible("Státusz".equals(selected));
        priorityDropdown.setVisible("Prioritás".equals(selected));
        frame.revalidate();
        frame.repaint();
    }

    private void handleSearch() {
        String selected = (String) strategyCombo.getSelectedItem();
        String keyword;

        switch (selected) {
            case "Státusz" -> keyword = (String) statusDropdown.getSelectedItem();
            case "Prioritás" -> keyword = (String) priorityDropdown.getSelectedItem();
            case "Kulcsszó" -> {
                keyword = searchField.getText().trim();
                if (keyword.isBlank()) {
                    JOptionPane.showMessageDialog(frame, "Kérlek adj meg keresési kifejezést!", "Hiba", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            default -> {
                JOptionPane.showMessageDialog(frame, "Ismeretlen keresési mód!", "Hiba", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        List<Ticket> allTickets = Session.getLoggedInUser().getRole().equalsIgnoreCase("admin")
                ? TicketRepo.getAll()
                : TicketRepo.getAllByUser(Session.getLoggedInUser().getId());

        TicketSearchStrategy strategy = switch (selected) {
            case "Státusz" -> new SearchByStatus();
            case "Prioritás" -> new SearchByPriority();
            case "Kulcsszó" -> new SearchByKeyword();
            default -> null;
        };

        if (strategy != null) {
            List<Ticket> result = strategy.search(allTickets, keyword);
            loadData(result);
        }
    }

    private void loadData(List<Ticket> tickets) {
        model.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm");

        for (Ticket t : tickets) {
            String assignedToName = "-";
            if (t.getAssignedTo() != null) {
                User u = UserRepo.getById(t.getAssignedTo());
                if (u != null) {
                    assignedToName = u.getFullName();
                }
            }

            String createdByName = "-";
            User creator = UserRepo.getById(t.getCreatedBy());
            if (creator != null) {
                createdByName = creator.getFullName();
            }

            Object[] row = {
                    t.getId(),
                    t.getTitle(),
                    t.getPriority(),
                    t.getStatus(),
                    createdByName,
                    t.getCreatedAt() != null ? t.getCreatedAt().format(formatter) : "-",
                    t.getDue() != null ? t.getDue().format(formatter) : "-",
                    assignedToName
            };
            model.addRow(row);
        }
    }
}