package system.app;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class UserManager {

    private final List<User> users = new ArrayList<>();
    private final Map<String, LoginSession> activeSessions = new HashMap<>();
    private final Map<String, String> recoveryTokens = new HashMap<>();

    public UserManager() {
        // Pre-create users for demo login
        users.add(new User("admin", "admin123", "admin@example.com", UserRole.SYSTEM_ADMIN, "SYSTEM"));
        users.add(new User("officer1", "officer123", "officer1@apu.edu.my", UserRole.ACADEMIC_OFFICER, "SYSTEM"));
        users.add(new User("courseadmin1", "course123", "courseadmin1@apu.edu.my", UserRole.COURSE_ADMINISTRATOR, "SYSTEM"));
    }

    public boolean addUser(String username, String password, String email, UserRole role, String createdBy) {
        if (username == null || username.isEmpty()) throw new IllegalArgumentException("Username required");
        if (password == null || password.length() < 6) throw new IllegalArgumentException("Password must be >= 6 characters");
        if (email == null || !email.contains("@")) throw new IllegalArgumentException("Invalid email");
        if (users.stream().anyMatch(u -> u.getUsername().equals(username))) throw new IllegalArgumentException("Username already exists");

        User user = new User(username, password, email, role, createdBy);
        users.add(user);
        return true;
    }

    public LoginSession login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password) && user.isActive()) {
                LoginSession session = new LoginSession(user);
                activeSessions.put(session.getSessionId(), session);
                user.setLastLogin(LocalDateTime.now());
                return session;
            }
        }
        return null;
    }

    public boolean logout(String sessionId) {
        LoginSession session = activeSessions.remove(sessionId);
        if (session != null) {
            session.endSession();
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
                return true;
            }
        }
        return false;
    }

    public boolean deactivateUser(String userId) {
        for (User user : users) {
            if (user.getUserId().equals(userId)) { user.deactivate(); return true; }
        }
        return false;
    }

    public boolean activateUser(String userId) {
        for (User user : users) {
            if (user.getUserId().equals(userId)) { user.activate(); return true; }
        }
        return false;
    }

    public boolean resetPassword(String userId, String newPassword) {
        for (User user : users) {
            if (user.getUserId().equals(userId)) { user.setPassword(newPassword); return true; }
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
}
