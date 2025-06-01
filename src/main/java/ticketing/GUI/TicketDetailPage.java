package ticketing.GUI;

import ticketing.App.Session;
import ticketing.Model.Ticket;
import ticketing.Model.User;
import ticketing.Repository.TicketRepo;
import ticketing.Repository.UserRepo;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TicketDetailPage {

    private final JFrame frame;
    private final JTextField titleField;
    private final JTextArea descriptionArea;
    private final JComboBox<String> statusCombo;
    private final JComboBox<String> priorityCombo;
    private final JComboBox<User> assignedToCombo;
    private final JButton saveButton;

    private final Ticket ticket;

    public TicketDetailPage(long ticketId) {
        ticket = TicketRepo.getById(ticketId);
        User currentUser = Session.getLoggedInUser();

        frame = new JFrame("Jegy #" + ticketId);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel header = new JLabel("Hibajegy részletei", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        mainPanel.add(header, BorderLayout.NORTH);

        // űrlap mezők
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 12, 12));
        titleField = new JTextField(ticket.getTitle());
        descriptionArea = new JTextArea(ticket.getDescription(), 4, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descriptionArea);

        statusCombo = new JComboBox<>(new String[]{"NEW", "IN_PROGRESS", "RESOLVED", "CLOSED"});
        statusCombo.setSelectedItem(ticket.getStatus());

        priorityCombo = new JComboBox<>(new String[]{"LOW", "MEDIUM", "HIGH", "CRITICAL"});
        priorityCombo.setSelectedItem(ticket.getPriority());

        formPanel.add(new JLabel("Cím:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Leírás:"));
        formPanel.add(descScroll);
        formPanel.add(new JLabel("Státusz:"));
        formPanel.add(statusCombo);
        formPanel.add(new JLabel("Prioritás:"));
        formPanel.add(priorityCombo);

        assignedToCombo = new JComboBox<>();
        if (currentUser.getRole().equalsIgnoreCase("admin")) {
            List<User> users = UserRepo.getAllUsers();
            for (User user : users) {
                if (user.getRole().equalsIgnoreCase("admin")) {
                    assignedToCombo.addItem(user);
                    if (ticket.getAssignedTo() != null && user.getId() == ticket.getAssignedTo()) {
                        assignedToCombo.setSelectedItem(user);
                    }
                }
            }
            formPanel.add(new JLabel("Hozzárendelve:"));
            formPanel.add(assignedToCombo);
        }

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Mentés gomb
        saveButton = new JButton("Mentés");
        saveButton.setBackground(new Color(51, 102, 204));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Csak admin módosíthat
        boolean isAdmin = currentUser.getRole().equalsIgnoreCase("admin");
        titleField.setEditable(isAdmin);
        descriptionArea.setEditable(isAdmin);
        statusCombo.setEnabled(isAdmin);
        priorityCombo.setEnabled(isAdmin);
        if (!isAdmin) saveButton.setVisible(false);

        frame.add(mainPanel);
        frame.setVisible(true);

        // esemény
        saveButton.addActionListener(e -> {
            String newTitle = titleField.getText().trim();
            String newDesc = descriptionArea.getText().trim();
            String newStatus = (String) statusCombo.getSelectedItem();
            String newPriority = (String) priorityCombo.getSelectedItem();

            if (newTitle.isBlank() || newDesc.isBlank()) {
                JOptionPane.showMessageDialog(frame, "A cím és leírás nem lehet üres!", "Hiba", JOptionPane.WARNING_MESSAGE);
                return;
            }

            ticket.setTitle(newTitle);
            ticket.setDescription(newDesc);
            ticket.setStatus(newStatus);
            ticket.setPriority(newPriority);

            if (isAdmin && assignedToCombo.getSelectedItem() != null) {
                User selectedUser = (User) assignedToCombo.getSelectedItem();
                ticket.setAssignedTo(selectedUser.getId());
            }

            boolean success = TicketRepo.update(ticket);
            if (success) {
                JOptionPane.showMessageDialog(frame, "Jegy frissítve!", "Siker", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Hiba a mentés során!", "Hiba", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
