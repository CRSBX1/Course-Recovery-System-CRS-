package system.app;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * User entity class representing system users with role-based access
 * Demonstrates: Encapsulation, Abstraction
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    // Static counter for generating sequential IDs starting from 100
    private static int idCounter = 100;

    // Date formatter for readable timestamps (HH:mm:ss format)
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    // Private fields - Encapsulation
    private String userId;
    private String username;
    private String password;
    private String email;
    private UserRole role;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private String createdBy;

    // Constructor
    public User(String username, String password, String email, UserRole role, String createdBy) {
        this.userId = generateUserId();
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
        this.createdBy = createdBy;
    }

    // Constructor for loading from file
    public User(String userId, String username, String password, String email,
                UserRole role, boolean isActive, LocalDateTime createdAt,
                LocalDateTime lastLogin, String createdBy) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
        this.createdBy = createdBy;

        // Update counter if loaded ID is higher
        updateCounterFromId(userId);
    }

    /**
     * Generate new user ID in format: ID100, ID101, ID102, etc.
     * Range: ID100 to ID500
     */
    private static synchronized String generateUserId() {
        if (idCounter > 500) {
            throw new IllegalStateException("Maximum user limit reached (ID500)");
        }
        return "ID" + (idCounter++);
    }

    /**
     * Update the static counter when loading users from file
     * to prevent ID conflicts
     */
    private static synchronized void updateCounterFromId(String userId) {
        if (userId != null && userId.startsWith("ID")) {
            try {
                int idNum = Integer.parseInt(userId.substring(2));
                if (idNum >= idCounter && idNum < 500) {
                    idCounter = idNum + 1;
                }
            } catch (NumberFormatException e) {
                // Ignore invalid format
            }
        }
    }

    /**
     * Reset counter to 100 (useful for testing)
     */
    public static synchronized void resetIdCounter() {
        idCounter = 100;
    }

    /**
     * Get current counter value (useful for debugging)
     */
    public static synchronized int getCurrentCounter() {
        return idCounter;
    }

    // Getters and Setters - Encapsulation
    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    // Business methods
    public boolean authenticate(String inputPassword) {
        return this.password.equals(inputPassword) && this.isActive;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }

    // Convert to file format
    public String toFileFormat() {
        return String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s",
                userId, username, password, email, role.name(),
                isActive,
                createdAt.format(TIME_FORMATTER),
                lastLogin != null ? lastLogin.format(TIME_FORMATTER) : "null",
                createdBy);
    }

    @Override
    public String toString() {
        return String.format("User[ID=%s, Username=%s, Email=%s, Role=%s, Active=%s]",
                userId, username, email, role, isActive);
    }
}