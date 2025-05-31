package ticketing.Controller;
import ticketing.Repository.UserRepo;
import ticketing.Model.User;
import ticketing.GUI.DashboardPage;
import ticketing.GUI.LoginPage;
import ticketing.App.Session;
import ticketing.Utils.LoggerUtil;

import javax.swing.*;

public class LoginController {

    private final LoginPage view;

    public LoginController(LoginPage view) {
        this.view = view;

        // Gombhoz eseménykezelő rendelése
        this.view.getLoginButton().addActionListener(e -> handleLogin());
    }

    private void handleLogin() {

        String username = view.getUsernameField().getText();
        String password = new String(view.getPasswordField().getPassword());
        User user = UserRepo.getUserByUsername(username);
        LoggerUtil.log("Sikeres bejelentkezés: " + user.getUsername());
        if (username.isBlank() || password.isBlank()) {
            JOptionPane.showMessageDialog(view.getFrame(),
                    "Kérlek töltsd ki a felhasználónevet és a jelszót!",
                    "Hiányzó adatok", JOptionPane.WARNING_MESSAGE);

            return;
        }


        if (user == null || !user.getPasswordHash().equals(password)) {
            JOptionPane.showMessageDialog(view.getFrame(),
                    "Hibás felhasználónév vagy jelszó!",
                    "Bejelentkezési hiba", JOptionPane.ERROR_MESSAGE);
            LoggerUtil.log("Sikertelen bejelentkezési kísérlet: " + username);

            return;
        }


        Session.setLoggedInUser(user);

        view.getFrame().dispose();
        new DashboardPage();
    }
}