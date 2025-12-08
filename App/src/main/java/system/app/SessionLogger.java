package system.app;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logs user login and logout events to a file
 */
public class SessionLogger {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Path LOG_FILE = Paths.get("Data", "session_logs.txt");

    /**
     * Log a login event
     */
    public static void logLogin(String username, String userId) {
        String logEntry = String.format("%s | LOGIN  | User: %s | UserID: %s%n",
                LocalDateTime.now().format(FORMATTER),
                username,
                userId);
        writeLog(logEntry);
    }

    /**
     * Log a logout event
     */
    public static void logLogout(String username, String userId) {
        String logEntry = String.format("%s | LOGOUT | User: %s | UserID: %s%n",
                LocalDateTime.now().format(FORMATTER),
                username,
                userId);
        writeLog(logEntry);
    }

    /**
     * Write log entry to file
     */
    private static synchronized void writeLog(String logEntry) {
        try {
            // Create Data directory if it doesn't exist
            Path dataDir = LOG_FILE.getParent();
            if (!Files.exists(dataDir)) {
                Files.createDirectories(dataDir);
            }

            // Create file with header if it doesn't exist
            if (!Files.exists(LOG_FILE)) {
                String header = "=== User Session Log ===%n" +
                        "Format: Timestamp | Event | User | UserID%n" +
                        "========================%n";
                Files.write(LOG_FILE, String.format(header).getBytes(StandardCharsets.UTF_8),
                        StandardOpenOption.CREATE);
            }

            // Append log entry
            Files.write(LOG_FILE, logEntry.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        } catch (IOException e) {
            System.err.println("Failed to write session log: " + e.getMessage());
        }
    }
}