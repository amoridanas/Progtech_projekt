package ticketing.GUI;

import javax.swing.*;
import java.awt.*;

public class DashboardPage {

    private final JFrame frame;

    public DashboardPage() {
        frame = new JFrame("Hibajegy rendszer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null); // Képernyő közepére


        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        JLabel titleLabel = new JLabel("Üdvözlünk a hibajegy rendszerben!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);


        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 15, 15));

        JButton createButton = new JButton(" Új hibajegy létrehozása");
        JButton viewButton = new JButton(" Hibajegyek megtekintése");
        JButton logoutButton = new JButton(" Kijelentkezés");

        buttonPanel.add(createButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(logoutButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setVisible(true);


        createButton.addActionListener(e -> new TicketPage());

        viewButton.addActionListener(e -> new TicketListPage());
        logoutButton.addActionListener(e -> {
            frame.dispose();
            new LoginPage(); // vagy visszalépés a főmenübe
        });
    }

    public JFrame getFrame() {
        return frame;
    }
}