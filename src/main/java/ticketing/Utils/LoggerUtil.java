package ticketing.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerUtil {
    private static final String LOG_FILE = "logs/app.log";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void log(String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        String fullMessage = "[" + timestamp + "] " + message;

        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(fullMessage + "\n");
        } catch (IOException e) {
            System.err.println("⚠️ Nem sikerült naplózni: " + e.getMessage());
        }
    }

    public static void logError(String message, Exception ex) {
        log(message + " | Hiba: " + ex.getMessage());
        ex.printStackTrace();
    }
}
