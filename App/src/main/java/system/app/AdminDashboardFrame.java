package system.app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminDashboardFrame extends JFrame {

    private final UserManager userManager;
    private final LoginSession session;
    private JButton dashboardBtn;
    private JButton userMgmtBtn;
    private JPanel mainContent;
    private DefaultTableModel tableModel;
    private JTable table;
    private TitlePanel titlePanel;

    public AdminDashboardFrame(UserManager userManager, LoginSession session) {
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
        showDashboardContent();

        setVisible(true);
    }

    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setPreferredSize(new Dimension(0, 80));
        
        titlePanel = new TitlePanel();
        /*// Left side - Logo and title
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
        });*/

        titlePanel.getLogoutButton().addActionListener(e -> {
            userManager.logout(session.getSessionId());
            dispose();
            new LoginFrame(userManager);
        });

        topBar.add(titlePanel);

        return topBar;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BorderLayout());
        sidebar.setBackground(Color.WHITE);
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(230, 230, 230)));

        // Menu panel (top section)
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.WHITE);

        // Dashboard button
        dashboardBtn = createSidebarButton("📊  Dashboard", true);
        dashboardBtn.addActionListener(e -> {
            setActiveButton(dashboardBtn);
            showDashboardContent();
        });

        // User Management button
        userMgmtBtn = createSidebarButton("👥  User Management", false);
        userMgmtBtn.addActionListener(e -> {
            setActiveButton(userMgmtBtn);
            showUserManagementContent();
        });

        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(dashboardBtn);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(userMgmtBtn);
        menuPanel.add(Box.createVerticalGlue());

        // Bottom user info panel
        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        userInfo.setBackground(Color.WHITE);
        userInfo.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)));

        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font("Serif", Font.PLAIN, 24));

        JLabel userName = new JLabel(session.getUser().getUsername());
        userName.setFont(new Font("SansSerif", Font.BOLD, 14));

        userInfo.add(userIcon);
        userInfo.add(userName);

        sidebar.add(menuPanel, BorderLayout.CENTER);
        sidebar.add(userInfo, BorderLayout.SOUTH);

        return sidebar;
    }

    private JButton createSidebarButton(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setMaximumSize(new Dimension(200, 45));
        btn.setPreferredSize(new Dimension(200, 45));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(0, 15, 0, 0));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        updateButtonStyle(btn, active);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!isButtonActive(btn)) {
                    btn.setBackground(new Color(245, 245, 245));
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!isButtonActive(btn)) {
                    btn.setBackground(Color.WHITE);
                }
            }
        });

        return btn;
    }

    private void updateButtonStyle(JButton btn, boolean active) {
        if (active) {
            btn.setBackground(new Color(230, 240, 255));
            btn.setForeground(new Color(0, 100, 255));
        } else {
            btn.setBackground(Color.WHITE);
            btn.setForeground(Color.BLACK);
        }
    }

    private boolean isButtonActive(JButton btn) {
        return btn.getBackground().equals(new Color(230, 240, 255));
    }

    private void setActiveButton(JButton activeBtn) {
        updateButtonStyle(dashboardBtn, false);
        updateButtonStyle(userMgmtBtn, false);
        updateButtonStyle(activeBtn, true);
    }

    private void showDashboardContent() {
        if (mainContent != null) {
            remove(mainContent);
        }

        mainContent = new JPanel();
        mainContent.setLayout(new BorderLayout());
        mainContent.setBackground(Color.white);
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

        add(mainContent, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void showUserManagementContent() {
        if (mainContent != null) {
            remove(mainContent);
        }

        mainContent = new JPanel();
        mainContent.setLayout(new BorderLayout());
        mainContent.setBackground(Color.white);
        mainContent.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Title
        JLabel titleLabel = new JLabel("User Management");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Table setup
        String[] columns = {"User ID", "Username", "Email", "Role", "Status", "Last Login"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(35);
        table.setSelectionBackground(new Color(230, 240, 255));
        table.setSelectionForeground(Color.BLACK);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(230, 230, 230));

        // Style table header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(Color.BLACK);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        // Center align columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        refreshTable();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        buttonPanel.setOpaque(false);

        JButton addBtn = createActionButton("➕ Add User", new Color(40, 167, 69));
        JButton updateBtn = createActionButton("✏️ Update User", new Color(0, 123, 255));
        JButton activateBtn = createActionButton("✓ Activate", new Color(23, 162, 184));
        JButton deactivateBtn = createActionButton("✗ Deactivate", new Color(255, 193, 7));

        addBtn.addActionListener(e -> addUser());
        updateBtn.addActionListener(e -> updateUser());
        activateBtn.addActionListener(e -> activateUser());
        deactivateBtn.addActionListener(e -> deactivateUser());

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(activateBtn);
        buttonPanel.add(deactivateBtn);

        // Top section with title
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainContent.add(topPanel, BorderLayout.NORTH);
        mainContent.add(scrollPane, BorderLayout.CENTER);

        add(mainContent, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private JButton createActionButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(130, 40));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });

        return btn;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<User> users = userManager.getAllUsers();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (User u : users) {
            tableModel.addRow(new Object[]{
                    u.getUserId(),
                    u.getUsername(),
                    u.getEmail(),
                    u.getRole().getDisplayName(),
                    u.isActive() ? "Active" : "Inactive",
                    u.getLastLogin() != null ? u.getLastLogin().format(formatter) : "Never"
            });
        }
    }

    private void addUser() {
        JTextField usernameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JComboBox<UserRole> roleBox = new JComboBox<>(UserRole.values());

        Object[] message = {
                "Username:", usernameField,
                "Email:", emailField,
                "Password:", passwordField,
                "Role:", roleBox
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add New User", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                userManager.addUser(
                        usernameField.getText(),
                        new String(passwordField.getPassword()),
                        emailField.getText(),
                        (UserRole) roleBox.getSelectedItem(),
                        session.getUser().getUsername()
                );
                refreshTable();
                JOptionPane.showMessageDialog(this, "User added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateUser() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to update", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String userId = (String) tableModel.getValueAt(row, 0);

        JTextField emailField = new JTextField((String) tableModel.getValueAt(row, 2));
        JComboBox<UserRole> roleBox = new JComboBox<>(UserRole.values());

        String currentRole = (String) tableModel.getValueAt(row, 3);
        for (UserRole role : UserRole.values()) {
            if (role.getDisplayName().equals(currentRole)) {
                roleBox.setSelectedItem(role);
                break;
            }
        }

        Object[] message = {
                "Email:", emailField,
                "Role:", roleBox
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Update User", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            userManager.updateUser(userId, emailField.getText(), (UserRole) roleBox.getSelectedItem());
            refreshTable();
            JOptionPane.showMessageDialog(this, "User updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deactivateUser() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to deactivate", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String userId = (String) tableModel.getValueAt(row, 0);
        String username = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to deactivate user: " + username + "?",
                "Confirm Deactivation",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            userManager.deactivateUser(userId);
            refreshTable();
            JOptionPane.showMessageDialog(this, "User deactivated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void activateUser() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to activate", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String userId = (String) tableModel.getValueAt(row, 0);
        userManager.activateUser(userId);
        refreshTable();
        JOptionPane.showMessageDialog(this, "User activated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel createStatCard(String title, String value, String icon, Color bgColor) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(25, 20, 25, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(80, 80, 80));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Serif", Font.PLAIN, 48));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

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