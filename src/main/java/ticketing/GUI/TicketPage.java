package ticketing.GUI;

import ticketing.App.Session;
import ticketing.Factory.FailureTicketFactory;
import ticketing.Factory.RequestTicketFactory;
import ticketing.Factory.TicketAbstractFactory;
import ticketing.Model.Priority;
import ticketing.Model.Ticket;
import ticketing.Repository.TicketRepo;

import javax.swing.*;
import java.awt.*;

public class TicketPage {

    private final JFrame frame;
    private final JTextField titleField;
    private final JTextArea descriptionArea;
    private final JComboBox<String> priorityCombo;
    private final JComboBox<String> typeCombo;
    private final JButton saveButton;

    public TicketPage() {
        frame = new JFrame("Új hibajegy létrehozása");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 450);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Cím
        JLabel titleLabel = new JLabel("Új hibajegy");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Mezők
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JLabel titleLbl = new JLabel("Cím:");
        titleField = new JTextField();

        JLabel descLbl = new JLabel("Leírás:");
        descriptionArea = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);

        JLabel priorityLbl = new JLabel("Prioritás:");
        priorityCombo = new JComboBox<>(new String[]{"LOW", "MEDIUM", "HIGH", "CRITICAL"});

        JLabel typeLbl = new JLabel("Típus:");
        typeCombo = new JComboBox<>(new String[]{"REQUEST", "FAILURE"});

        formPanel.add(titleLbl);
        formPanel.add(titleField);
        formPanel.add(descLbl);
        formPanel.add(scrollPane);
        formPanel.add(priorityLbl);
        formPanel.add(priorityCombo);
        formPanel.add(typeLbl);
        formPanel.add(typeCombo);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Mentés gomb
        saveButton = new JButton("✅ Mentés");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);

        // Gomb eseménykezelő
        saveButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String desc = descriptionArea.getText().trim();
            String priorityStr = (String) priorityCombo.getSelectedItem();
            String type = (String) typeCombo.getSelectedItem();

            if (title.isBlank() || desc.isBlank()) {
                JOptionPane.showMessageDialog(frame, "Minden mező kitöltése kötelező!", "Hiba", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Priority priority = Priority.valueOf(priorityStr);
            long userId = Session.getLoggedInUser().getId();

            TicketAbstractFactory factory = switch (type) {
                case "REQUEST" -> new RequestTicketFactory();
                case "FAILURE" -> new FailureTicketFactory();
                default -> null;
            };

            if (factory == null) {
                JOptionPane.showMessageDialog(frame, "Érvénytelen típus!", "Hiba", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Ticket ticket = factory.create(title, desc, priority, userId);
            boolean success = TicketRepo.insert(ticket);

            if (success) {
                JOptionPane.showMessageDialog(frame, "Hibajegy sikeresen mentve!", "Siker", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Mentés sikertelen!", "Hiba", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
