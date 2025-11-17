package system.app;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * User entity class representing system users with role-based access
 * Demonstrates: Encapsulation, Abstraction
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

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
        this.userId = UUID.randomUUID().toString();
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
                isActive, createdAt,
                lastLogin != null ? lastLogin : "null",
                createdBy);
    }

    @Override
    public String toString() {
        return String.format("User[ID=%s, Username=%s, Email=%s, Role=%s, Active=%s]",
                userId, username, email, role, isActive);
    }
}