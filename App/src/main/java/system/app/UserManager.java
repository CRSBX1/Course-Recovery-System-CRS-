package system.app;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class UserManager {

    private final List<User> users = new ArrayList<>();
    private final Map<String, LoginSession> activeSessions = new HashMap<>();
    private final Map<String, String> recoveryTokens = new HashMap<>();

    // Date formatter matching User class format
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    // Updated: file where users are persisted in existing Data folder
    // Use absolute path to ensure we find the correct Data folder
    private static final Path USERS_FILE = Paths.get(System.getProperty("user.dir"), "app", "Data", "users.txt");

    public UserManager() {
        // DEBUG: Print paths to verify location
        System.out.println("=== UserManager Debug Info ===");
        System.out.println("Working directory: " + System.getProperty("user.dir"));
        System.out.println("Users file path: " + USERS_FILE.toAbsolutePath());
        System.out.println("Data folder exists: " + Files.exists(USERS_FILE.getParent()));
        System.out.println("==============================");

        // Check if Data folder exists, if not warn the user
        Path dataDir = USERS_FILE.getParent();
        if (!Files.exists(dataDir)) {
            System.err.println("WARNING: Data folder not found at " + dataDir);
            System.err.println("Please ensure the Data folder exists in your project root.");
            try {
                Files.createDirectories(dataDir);
                System.out.println("Created Data directory at: " + dataDir);
            } catch (IOException e) {
                System.err.println("Failed to create Data directory: " + e.getMessage());
            }
        }

        // Try to load existing users from file; if none exist, create demo users
        if (!loadUsersFromFile()) {
            users.add(new User("officer1", "officer123", "officer1@apu.edu.my", UserRole.ACADEMIC_OFFICER, "SYSTEM"));
            users.add(new User("courseadmin1", "course123", "courseadmin1@apu.edu.my", UserRole.COURSE_ADMINISTRATOR, "SYSTEM"));
            saveUsersToFile();
        }
    }

    /**
     * Save users to USERS_FILE using each user's toFileFormat() (pipe-separated).
     */
    private synchronized void saveUsersToFile() {
        try {
            List<String> lines = new ArrayList<>();
            // Add header comment for readability
            lines.add("# User Data File - Format: userId|username|password|email|role|isActive|createdAt|lastLogin|createdBy");

            for (User u : users) {
                lines.add(u.toFileFormat());
            }
            Files.write(USERS_FILE, lines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Users saved to " + USERS_FILE);
        } catch (IOException e) {
            System.err.println("Failed to save users: " + e.getMessage());
        }
    }

    /**
     * Load users from USERS_FILE. Returns true if load succeeded and file existed.
     */
    private synchronized boolean loadUsersFromFile() {
        if (!Files.exists(USERS_FILE)) {
            System.out.println("No existing users file found at " + USERS_FILE);
            return false;
        }
        try {
            List<String> lines = Files.readAllLines(USERS_FILE, StandardCharsets.UTF_8);
            users.clear();
            int loadedCount = 0;

            for (String line : lines) {
                // Skip empty lines and comments
                if (line.trim().isEmpty() || line.trim().startsWith("#")) continue;

                // Expected format (from User.toFileFormat):
                // userId|username|password|email|role|isActive|createdAt|lastLogin|createdBy
                String[] parts = line.split("\\|", -1);
                if (parts.length < 9) {
                    System.err.println("Skipping malformed line: " + line);
                    continue;
                }

                String userId = parts[0];
                String username = parts[1];
                String password = parts[2];
                String email = parts[3];
                String roleStr = parts[4];
                boolean isActive = Boolean.parseBoolean(parts[5]);
                LocalDateTime createdAt = LocalDateTime.parse(parts[6], TIME_FORMATTER);
                LocalDateTime lastLogin = "null".equals(parts[7]) || parts[7].isEmpty() ? null : LocalDateTime.parse(parts[7], TIME_FORMATTER);
                String createdBy = parts[8];

                // Map role string to enum
                UserRole role;
                try {
                    role = UserRole.valueOf(roleStr);
                } catch (Exception ex) {
                    System.err.println("Skipping user with unknown role: " + roleStr);
                    continue;
                }

                User u = new User(userId, username, password, email, role, isActive, createdAt, lastLogin, createdBy);
                users.add(u);
                loadedCount++;
            }
            System.out.println("Loaded " + loadedCount + " users from " + USERS_FILE);
            return true;
        } catch (IOException e) {
            System.err.println("Failed to load users: " + e.getMessage());
            return false;
        }
    }

    public boolean addUser(String username, String password, String email, UserRole role, String createdBy) {
        if (username == null || username.isEmpty()) throw new IllegalArgumentException("Username required");
        if (password == null || password.length() < 6) throw new IllegalArgumentException("Password must be >= 6 characters");
        if (email == null || !email.contains("@")) throw new IllegalArgumentException("Invalid email");
        if (users.stream().anyMatch(u -> u.getUsername().equals(username))) throw new IllegalArgumentException("Username already exists");

        User user = new User(username, password, email, role, createdBy);
        users.add(user);
        saveUsersToFile();
        return true;
    }

    public LoginSession login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password) && user.isActive()) {
                LoginSession session = new LoginSession(user);
                activeSessions.put(session.getSessionId(), session);
                user.setLastLogin(LocalDateTime.now());
                saveUsersToFile(); // persist last login
                return session;
            }
        }
        return null;
    }

    public boolean logout(String sessionId) {
        LoginSession session = activeSessions.remove(sessionId);
        if (session != null) {
            session.endSession();
            saveUsersToFile();
            return true;
        }
        return false;
    }

    public List<User> getAllUsers() { return new ArrayList<>(users); }
    public List<User> getActiveUsers() { return users.stream().filter(User::isActive).collect(Collectors.toList()); }

    public boolean updateUser(String userId, String newEmail, UserRole newRole) {
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                if (newEmail != null) user.setEmail(newEmail);
                if (newRole != null) user.setRole(newRole);
                saveUsersToFile();
                return true;
            }
        }
        return false;
    }

    public boolean deactivateUser(String userId) {
        for (User user : users) {
            if (user.getUserId().equals(userId)) { user.deactivate(); saveUsersToFile(); return true; }
        }
        return false;
    }

    public boolean activateUser(String userId) {
        for (User user : users) {
            if (user.getUserId().equals(userId)) { user.activate(); saveUsersToFile(); return true; }
        }
        return false;
    }

    public boolean resetPassword(String userId, String newPassword) {
        for (User user : users) {
            if (user.getUserId().equals(userId)) { user.setPassword(newPassword); saveUsersToFile(); return true; }
        }
        return false;
    }

    public String generateRecoveryToken(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                String token = "TOKEN-" + UUID.randomUUID();
                recoveryTokens.put(token, user.getUserId());
                return token;
            }
        }
        throw new IllegalArgumentException("Email not found");
    }

    public boolean recoverPassword(String token, String newPassword) {
        String userId = recoveryTokens.get(token);
        if (userId == null) return false;
        boolean result = resetPassword(userId, newPassword);
        recoveryTokens.remove(token);
        return result;
    }

    public boolean hasPermission(String permission) {
        for (LoginSession session : activeSessions.values()) {
            if (session.getUser().getRole().hasPermission(permission)) return true;
        }
        return false;
    }
    // =============================
    // Dashboard Statistics Methods
    // =============================

    /** Total number of users */
    public int getTotalUsers() {
        return users.size();
    }

    /** Number of inactive users (isActive == false) */
    public int getInactiveUsers() {
        return (int) users.stream().filter(u -> !u.isActive()).count();
    }

    /** Number of active users (already have getActiveUsers, here is count only) */
    public int getActiveUserCount() {
        return getActiveUsers().size();
    }

    /** Users who logged in within last 24 hours */
    public int getRecentLogins() {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(24);
        return (int) users.stream()
                .filter(u -> u.getLastLogin() != null && u.getLastLogin().isAfter(cutoff))
                .count();
    }

}