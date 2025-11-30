package system.app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;

public class MainFrame extends JFrame {

    private final UserManager userManager;
    private final LoginSession session;

    public MainFrame(UserManager userManager, LoginSession session) {
        this.userManager = userManager;
        this.session = session;

        setTitle("Course Recovery Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        // Main layout
        setLayout(new BorderLayout());

        // TOP BAR
        JPanel topBar = createTopBar();
        add(topBar, BorderLayout.NORTH);

        // SIDEBAR
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        // MAIN CONTENT
        JPanel mainContent = createMainContent();
        add(mainContent, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(0, 60, 113));
        topBar.setPreferredSize(new Dimension(0, 60));
        topBar.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Left side - Logo and title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        leftPanel.setOpaque(false);

        JLabel iconLabel = new JLabel("🎓");
        iconLabel.setFont(new Font("Serif", Font.PLAIN, 30));
        iconLabel.setForeground(Color.WHITE);

        JLabel titleLabel = new JLabel("Course Recovery Management System");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        leftPanel.add(iconLabel);
        leftPanel.add(titleLabel);

        // Right side - Logout button
        JButton logoutBtn = new JButton("Logout  ↪");
        logoutBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(new Color(220, 53, 69));
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setPreferredSize(new Dimension(120, 40));

        logoutBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutBtn.setBackground(new Color(200, 35, 51));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutBtn.setBackground(new Color(220, 53, 69));
            }
        });

        logoutBtn.addActionListener(e -> {
            userManager.logout(session.getSessionId());
            dispose();
            new LoginFrame(userManager);
        });

        topBar.add(leftPanel, BorderLayout.WEST);
        topBar.add(logoutBtn, BorderLayout.EAST);

        return topBar;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.WHITE);
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(230, 230, 230)));

        // Dashboard button
        JButton dashboardBtn = createSidebarButton("📊  Dashboard", true);
        dashboardBtn.addActionListener(e -> {
            // Already on dashboard
        });

        // User Management button
        JButton userMgmtBtn = createSidebarButton("👥  User Management", false);
        userMgmtBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "User Management feature coming soon!");
        });

        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(dashboardBtn);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(userMgmtBtn);

        // Bottom user info
        sidebar.add(Box.createVerticalGlue());
        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        userInfo.setBackground(Color.WHITE);
        userInfo.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)));

        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font("Serif", Font.PLAIN, 24));

        JLabel userName = new JLabel(session.getUser().getUsername());
        userName.setFont(new Font("SansSerif", Font.BOLD, 14));

        userInfo.add(userIcon);
        userInfo.add(userName);
        sidebar.add(userInfo);

        return sidebar;
    }

    private JButton createSidebarButton(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setMaximumSize(new Dimension(200, 45));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (active) {
            btn.setBackground(new Color(230, 240, 255));
            btn.setForeground(new Color(0, 100, 255));
        } else {
            btn.setBackground(Color.WHITE);
            btn.setForeground(Color.BLACK);
        }

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!active) {
                    btn.setBackground(new Color(245, 245, 245));
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!active) {
                    btn.setBackground(Color.WHITE);
                }
            }
        });

        return btn;
    }

    private JPanel createMainContent() {
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BorderLayout());
        mainContent.setBackground(new Color(248, 249, 250));
        mainContent.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome, " + session.getUser().getRole().getDisplayName());
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        welcomeLabel.setBorder(new EmptyBorder(0, 0, 30, 0));

        // Stats panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setOpaque(false);

        int totalUsers = userManager.getAllUsers().size();
        int activeUsers = userManager.getActiveUsers().size();
        int inactiveUsers = totalUsers - activeUsers;
        int recentLogins = (int) userManager.getAllUsers().stream()
                .filter(u -> u.getLastLogin() != null &&
                        u.getLastLogin().isAfter(LocalDateTime.now().minusDays(7)))
                .count();

        statsPanel.add(createStatCard("Total Users", String.valueOf(totalUsers), "👥", new Color(200, 200, 200)));
        statsPanel.add(createStatCard("Active Account", String.valueOf(activeUsers), "✓", new Color(200, 200, 200)));
        statsPanel.add(createStatCard("Inactive Accounts", String.valueOf(inactiveUsers), "👤", new Color(200, 200, 200)));
        statsPanel.add(createStatCard("Recent Logins", String.valueOf(recentLogins), "🔔", new Color(200, 200, 200)));

        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setOpaque(false);
        topSection.add(welcomeLabel, BorderLayout.NORTH);
        topSection.add(statsPanel, BorderLayout.CENTER);

        mainContent.add(topSection, BorderLayout.NORTH);

        return mainContent;
    }

    private JPanel createStatCard(String title, String value, String icon, Color bgColor) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(25, 20, 25, 20)
        ));

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(80, 80, 80));

        // Icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Serif", Font.PLAIN, 48));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Value
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(iconLabel, BorderLayout.CENTER);

        card.add(topPanel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }
}