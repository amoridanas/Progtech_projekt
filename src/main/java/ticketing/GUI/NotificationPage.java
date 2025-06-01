package ticketing.GUI;

import ticketing.App.Session;
import ticketing.Model.Notification;
import ticketing.Repository.NotificationRepo;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class NotificationPage {

    private final JFrame frame;
    private final DefaultListModel<String> listModel;
    private final JList<String> notificationList;

    public NotificationPage() {
        frame = new JFrame("🔔 Értesítések");
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Legutóbbi értesítések", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(title, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        notificationList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(notificationList);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("❌ Bezárás");
        closeButton.addActionListener(e -> frame.dispose());
        panel.add(closeButton, BorderLayout.SOUTH);

        frame.add(panel);
        loadNotifications();
        frame.setVisible(true);
    }

    private void loadNotifications() {
        long userId = Session.getLoggedInUser().getId();
        List<Notification> notifications = NotificationRepo.getByUserId(userId);

        if (notifications.isEmpty()) {
            listModel.addElement("Nincsenek új értesítések.");
        } else {
            for (Notification n : notifications) {
                listModel.addElement("[" + n.getCreatedAtFormatted() + "] " + n.getMessage());
            }
        }
    }
}
