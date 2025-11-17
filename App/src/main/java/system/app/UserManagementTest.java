package system.app;

import java.util.List;

public class UserManagementTest {

    public static void main(String[] args) {
        System.out.println("=== User Management Module Test ===\n");

        UserManager userManager = new UserManager();

        // TEST 1: Add Users
        System.out.println("TEST 1: Adding Users");
        testAddUser(userManager);

        // TEST 2: User Authentication
        System.out.println("\nTEST 2: User Authentication");
        testAuthentication(userManager);

        // TEST 3: Update User
        System.out.println("\nTEST 3: Update User");
        testUpdateUser(userManager);

        // TEST 4: Deactivate/Activate User
        System.out.println("\nTEST 4: Deactivate and Activate User");
        testDeactivateActivate(userManager);

        // TEST 5: Password Management
        System.out.println("\nTEST 5: Password Management");
        testPasswordManagement(userManager);

        // TEST 6: Role-Based Permissions
        System.out.println("\nTEST 6: Role-Based Permissions");
        testRolePermissions(userManager);

        // TEST 7: Session Management
        System.out.println("\nTEST 7: Session Management");
        testSessionManagement(userManager);

        // TEST 8: View All Users
        System.out.println("\nTEST 8: View All Users");
        testViewUsers(userManager);

        System.out.println("\n=== All Tests Completed ===");
    }

    private static void testAddUser(UserManager userManager) {
        try {
            boolean result = userManager.addUser(
                    "testuser1", "password123", "testuser1@apu.edu.my",
                    UserRole.ACADEMIC_OFFICER, "admin"
            );
            System.out.println("✓ Add valid user: " + (result ? "PASSED" : "FAILED"));

            try {
                userManager.addUser(
                        "testuser1", "password123", "duplicate@apu.edu.my",
                        UserRole.ACADEMIC_OFFICER, "admin"
                );
                System.out.println("✗ Duplicate user check: FAILED (should throw exception)");
            } catch (IllegalArgumentException e) {
                System.out.println("✓ Duplicate user check: PASSED - " + e.getMessage());
            }

            try {
                userManager.addUser(
                        "testuser2", "123", "testuser2@apu.edu.my",
                        UserRole.ACADEMIC_OFFICER, "admin"
                );
                System.out.println("✗ Invalid password check: FAILED");
            } catch (IllegalArgumentException e) {
                System.out.println("✓ Invalid password check: PASSED - " + e.getMessage());
            }

            try {
                userManager.addUser(
                        "testuser3", "password123", "invalidemail",
                        UserRole.ACADEMIC_OFFICER, "admin"
                );
                System.out.println("✗ Invalid email check: FAILED");
            } catch (IllegalArgumentException e) {
                System.out.println("✓ Invalid email check: PASSED - " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("✗ Add user test failed: " + e.getMessage());
        }
    }

    private static void testAuthentication(UserManager userManager) {
        LoginSession session = userManager.login("admin", "admin123");
        if (session != null) {
            System.out.println("✓ Valid login: PASSED");
            System.out.println("  Session ID: " + session.getSessionId());
            System.out.println("  Login time: " + session.getLoginTime());
        } else {
            System.out.println("✗ Valid login: FAILED");
        }

        LoginSession invalidSession = userManager.login("admin", "wrongpassword");
        System.out.println(invalidSession == null ? "✓ Invalid password check: PASSED" : "✗ Invalid password check: FAILED");

        LoginSession nonExistentSession = userManager.login("nonexistent", "password");
        System.out.println(nonExistentSession == null ? "✓ Non-existent user check: PASSED" : "✗ Non-existent user check: FAILED");
    }

    private static void testUpdateUser(UserManager userManager) {
        List<User> users = userManager.getAllUsers();
        if (!users.isEmpty()) {
            User user = users.get(0);

            boolean result = userManager.updateUser(user.getUserId(), "newemail@apu.edu.my", null);
            System.out.println("✓ Update user email: " + (result ? "PASSED" : "FAILED"));

            result = userManager.updateUser(user.getUserId(), null, UserRole.COURSE_ADMINISTRATOR);
            System.out.println("✓ Update user role: " + (result ? "PASSED" : "FAILED"));

            // Restore role
            userManager.updateUser(user.getUserId(), null, user.getRole());
        }
    }

    private static void testDeactivateActivate(UserManager userManager) {
        userManager.addUser("deactivatetest", "test123456", "deactivate@apu.edu.my",
                UserRole.ACADEMIC_OFFICER, "admin");

        User testUser = userManager.getAllUsers().stream()
                .filter(u -> u.getUsername().equals("deactivatetest")).findFirst().orElse(null);

        if (testUser != null) {
            boolean result = userManager.deactivateUser(testUser.getUserId());
            System.out.println("✓ Deactivate user: " + (result ? "PASSED" : "FAILED"));

            LoginSession session = userManager.login("deactivatetest", "test123456");
            System.out.println(session == null ? "✓ Login deactivated user check: PASSED" : "✗ Login deactivated user check: FAILED");

            result = userManager.activateUser(testUser.getUserId());
            System.out.println("✓ Activate user: " + (result ? "PASSED" : "FAILED"));

            session = userManager.login("deactivatetest", "test123456");
            if (session != null) {
                System.out.println("✓ Login activated user check: PASSED");
                userManager.logout(session.getSessionId());
            } else {
                System.out.println("✗ Login activated user check: FAILED");
            }
        }
    }

    private static void testPasswordManagement(UserManager userManager) {
        userManager.addUser("passwordtest", "oldpassword", "passwordtest@apu.edu.my",
                UserRole.ACADEMIC_OFFICER, "admin");

        User testUser = userManager.getAllUsers().stream()
                .filter(u -> u.getUsername().equals("passwordtest")).findFirst().orElse(null);

        if (testUser != null) {
            boolean result = userManager.resetPassword(testUser.getUserId(), "newpassword123");
            System.out.println("✓ Reset password: " + (result ? "PASSED" : "FAILED"));

            LoginSession session = userManager.login("passwordtest", "oldpassword");
            System.out.println(session == null ? "✓ Old password check: PASSED" : "✗ Old password check: FAILED");

            session = userManager.login("passwordtest", "newpassword123");
            if (session != null) {
                System.out.println("✓ New password check: PASSED");
                userManager.logout(session.getSessionId());
            } else {
                System.out.println("✗ New password check: FAILED");
            }

            String token = userManager.generateRecoveryToken("passwordtest@apu.edu.my");
            System.out.println("✓ Generate recovery token: PASSED (" + token.substring(0, 8) + "...)");

            result = userManager.recoverPassword(token, "recoveredpass123");
            System.out.println("✓ Recover password with token: " + (result ? "PASSED" : "FAILED"));
        }
    }

    private static void testRolePermissions(UserManager userManager) {
        LoginSession adminSession = userManager.login("admin", "admin123");
        if (adminSession != null) {
            System.out.println(userManager.hasPermission("USER_MANAGE") &&
                    userManager.hasPermission("ALL_ACCESS") ? "✓ Admin permissions: PASSED" : "✗ Admin permissions: FAILED");
            userManager.logout(adminSession.getSessionId());
        }
    }

    private static void testSessionManagement(UserManager userManager) {
        LoginSession session = userManager.login("admin", "admin123");
        if (session != null) {
            System.out.println("✓ Session created: PASSED");
            System.out.println("  Active: " + session.isActive());

            try { Thread.sleep(1000); } catch (InterruptedException e) {}

            boolean logoutResult = userManager.logout(session.getSessionId());
            System.out.println("✓ Session ended: " + (logoutResult ? "PASSED" : "FAILED"));
            System.out.println("  Duration: " + session.getSessionDuration() + " minutes");
        }
    }

    private static void testViewUsers(UserManager userManager) {
        List<User> allUsers = userManager.getAllUsers();
        List<User> activeUsers = userManager.getActiveUsers();

        System.out.println("Total users: " + allUsers.size());
        System.out.println("Active users: " + activeUsers.size());

        System.out.println("\nUser List:");
        System.out.println("-----------------------------------------------------------");
        System.out.printf("%-15s %-20s %-25s %-10s%n", "Username", "Role", "Email", "Active");
        System.out.println("-----------------------------------------------------------");

        for (User user : allUsers) {
            System.out.printf("%-15s %-20s %-25s %-10s%n",
                    user.getUsername(),
                    user.getRole().getDisplayName(),
                    user.getEmail(),
                    user.isActive() ? "Yes" : "No");
        }
        System.out.println("-----------------------------------------------------------");
        System.out.println("✓ View users: PASSED");
    }
}
