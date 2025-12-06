package system.app;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.UUID;

public class LoginSession {

    private final String sessionId;
    private final User user;
    private final LocalDateTime loginTime;
    private boolean active;

    public LoginSession(User user) {
        this.user = user;
        this.sessionId = UUID.randomUUID().toString();
        this.loginTime = LocalDateTime.now();
        this.active = true;
    }

    public String getSessionId() {
        return sessionId;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public boolean isActive() {
        return active;
    }

    public long getSessionDuration() {
        return Duration.between(loginTime, LocalDateTime.now()).toMinutes();
    }

    public void endSession() {
        this.active = false;
    }
}
