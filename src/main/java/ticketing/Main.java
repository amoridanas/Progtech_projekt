package ticketing;
import ticketing.Database.DatabaseConnection;
import java.sql.Connection;
import ticketing.Database.DatabaseConnection;
import ticketing.GUI.LoginPage;
import ticketing.Utils.LoggerUtil;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            System.out.println(" Sikeres kapcsolat az adatbázishoz!");
            DatabaseConnection.closeConnection();
        } catch (Exception e) {
            System.err.println("Sikertelen kapcsolat: " + e.getMessage());
            e.printStackTrace();
        }

        new LoginPage();
        LoggerUtil.log("Alkalmazás elindult.");
    }
}
