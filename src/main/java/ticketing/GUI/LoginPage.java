package ticketing.GUI;

import ticketing.Controller.LoginController;
import ticketing.Utils.StyleUtil;

import javax.swing.*;
import java.awt.*;

public class LoginPage {

    private final JFrame frame;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;

    public LoginPage() {
        // Megjelenés beállítása (Nimbus)
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.err.println("Nem sikerült beállítani a Look & Feel-t: " + e.getMessage());
        }

        frame = new JFrame("Bejelentkezés");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Cím
        JLabel titleLabel = new JLabel("Ticketing System", SwingConstants.CENTER);
        titleLabel.setFont(StyleUtil.TITLE_FONT);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Középső rész (űrlap)
        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 10));

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        formPanel.add(new JLabel("Felhasználónév:"));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Jelszó:"));
        formPanel.add(passwordField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Gomb
        loginButton = new JButton(" Bejelentkezés");
        loginButton.setBackground(StyleUtil.PRIMARY_COLOR);
        loginButton.setForeground(StyleUtil.BUTTON_TEXT);
        loginButton.setFont(StyleUtil.LABEL_FONT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);

        // Vezérlő összekötése
        new LoginController(this);
    }

    public JFrame getFrame() {
        return frame;
    }

    public JTextField getUsernameField() {
        return usernameField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JButton getLoginButton() {
        return loginButton;
    }
}