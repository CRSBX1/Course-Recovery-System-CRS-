package system.app;

import java.util.Set;

public enum UserRole {
    SYSTEM_ADMIN(Set.of("ALL_ACCESS", "USER_MANAGE", "COURSE_MANAGE")),
    ACADEMIC_OFFICER(Set.of("STUDENT_VIEW", "USER_MANAGE")),
    COURSE_ADMINISTRATOR(Set.of("COURSE_MANAGE"));

    private final Set<String> permissions;

    UserRole(Set<String> permissions) { this.permissions = permissions; }

    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }

    public String getDisplayName() {
        switch (this) {
            case SYSTEM_ADMIN: return "System Administrator";
            case ACADEMIC_OFFICER: return "Academic Officer";
            case COURSE_ADMINISTRATOR: return "Course Administrator";
            default: return name();
        }
    }
}
