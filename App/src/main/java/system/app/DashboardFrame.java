package system.app;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class DashboardFrame extends JFrame {

    private final UserManager userManager;
    private final LoginSession session;

    public DashboardFrame(UserManager userManager, LoginSession session) {
        this.userManager = userManager;
        this.session = session;

        setTitle("CRS Dashboard - " + session.getUser().getRole().getDisplayName());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        if (session.getUser().getRole() == UserRole.COURSE_ADMINISTRATOR) {
            initCourseAdminDashboard();
        } else if (session.getUser().getRole() == UserRole.ACADEMIC_OFFICER) {
            initAcademicOfficerDashboard();
        } else {
            JOptionPane.showMessageDialog(this, "Access denied for your role", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        }

        setVisible(true);
    }

    private void initCourseAdminDashboard() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));

        int totalUsers = userManager.getAllUsers().size();
        int activeUsers = userManager.getActiveUsers().size();
        int inactiveUsers = totalUsers - activeUsers;
        int recentLogins = (int) userManager.getAllUsers().stream()
                .filter(u -> u.getLastLogin() != null && u.getLastLogin().isAfter(LocalDateTime.now().minusDays(7)))
                .count();

        statsPanel.add(createStatBox("Total Users", totalUsers));
        statsPanel.add(createStatBox("Active Accounts", activeUsers));
        statsPanel.add(createStatBox("Inactive Accounts", inactiveUsers));
        statsPanel.add(createStatBox("Recent Logins", recentLogins));

        JButton userMgmtBtn = new JButton("User Management");
        userMgmtBtn.addActionListener(e -> new UserManagementFrame(userManager));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            userManager.logout(session.getSessionId());
            dispose();
            new LoginFrame(userManager);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(userMgmtBtn);
        buttonPanel.add(logoutBtn);

        panel.add(statsPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
    }

    private void initAcademicOfficerDashboard() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Welcome " + session.getUser().getUsername() + " (Academic Officer)", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            userManager.logout(session.getSessionId());
            dispose();
            new LoginFrame(userManager);
        });

        panel.add(label, BorderLayout.CENTER);
        panel.add(logoutBtn, BorderLayout.SOUTH);
        add(panel);
    }

    private JPanel createStatBox(String title, int value) {
        JPanel box = new JPanel(new BorderLayout());
        box.setBorder(BorderFactory.createTitledBorder(title));
        JLabel label = new JLabel(String.valueOf(value), SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        box.add(label, BorderLayout.CENTER);
        return box;
    }
}
