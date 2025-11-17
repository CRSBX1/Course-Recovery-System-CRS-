package system.app;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserManagementFrame extends JFrame {

    private final UserManager userManager;
    private final DefaultTableModel tableModel;
    private final JTable table;

    public UserManagementFrame(UserManager userManager) {
        this.userManager = userManager;
        setTitle("User Management");
        setSize(800, 400);
        setLocationRelativeTo(null);

        String[] columns = {"User ID", "Username", "Email", "Role", "Status", "Last Login"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0 && column != 5; // UserID and LastLogin not editable
            }
        };

        table = new JTable(tableModel);
        refreshTable();

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton addBtn = new JButton("Add User");
        addBtn.addActionListener(e -> addUser());

        JButton updateBtn = new JButton("Update User");
        updateBtn.addActionListener(e -> updateUser());

        JButton deactivateBtn = new JButton("Deactivate User");
        deactivateBtn.addActionListener(e -> deactivateUser());

        JButton activateBtn = new JButton("Activate User");
        activateBtn.addActionListener(e -> activateUser());

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deactivateBtn);
        buttonPanel.add(activateBtn);

        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<User> users = userManager.getAllUsers();
        for (User u : users) {
            tableModel.addRow(new Object[]{
                    u.getUserId(),
                    u.getUsername(),
                    u.getEmail(),
                    u.getRole().getDisplayName(),
                    u.isActive() ? "Active" : "Inactive",
                    u.getLastLogin() != null ? u.getLastLogin() : ""
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

        int option = JOptionPane.showConfirmDialog(this, message, "Add User", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                userManager.addUser(usernameField.getText(), new String(passwordField.getPassword()),
                        emailField.getText(), (UserRole) roleBox.getSelectedItem(), "CourseAdmin");
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateUser() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        String userId = (String) tableModel.getValueAt(row, 0);

        JTextField emailField = new JTextField((String) tableModel.getValueAt(row, 2));
        JComboBox<UserRole> roleBox = new JComboBox<>(UserRole.values());
        roleBox.setSelectedItem(UserRole.valueOf(((String) tableModel.getValueAt(row, 3)).toUpperCase().replace(" ", "_")));

        Object[] message = {
                "Email:", emailField,
                "Role:", roleBox
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Update User", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            userManager.updateUser(userId, emailField.getText(), (UserRole) roleBox.getSelectedItem());
            refreshTable();
        }
    }

    private void deactivateUser() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        String userId = (String) tableModel.getValueAt(row, 0);
        userManager.deactivateUser(userId);
        refreshTable();
    }

    private void activateUser() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        String userId = (String) tableModel.getValueAt(row, 0);
        userManager.activateUser(userId);
        refreshTable();
    }
}
