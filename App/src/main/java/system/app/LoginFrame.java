package system.app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {

    private final UserManager userManager;

    public LoginFrame(UserManager userManager) {
        this.userManager = userManager;
        setTitle("Course Recovery Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 650);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with light gray background
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(220, 220, 220));

        // Center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(220, 220, 220));
        centerPanel.setBorder(new EmptyBorder(60, 50, 60, 50));

        // Title
        JLabel titleLabel1 = new JLabel("Course Recovery");
        titleLabel1.setFont(new Font("Serif", Font.PLAIN, 42));
        titleLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel2 = new JLabel("Management");
        titleLabel2.setFont(new Font("Serif", Font.PLAIN, 42));
        titleLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username field
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField userField = new JTextField();
        userField.setMaximumSize(new Dimension(400, 80));
        userField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        userField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 255), 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        // Password field
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPasswordField passField = new JPasswordField();
        passField.setMaximumSize(new Dimension(400, 50));
        passField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        passField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setMaximumSize(new Dimension(400, 50));
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(0, 123, 255));
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Hover effect
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(0, 105, 217));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(0, 123, 255));
            }
        });

        // Forgot password link
        JButton forgotButton = new JButton("<html><u>Forgot Password?</u></html>");
        forgotButton.setFont(new Font("SansSerif", Font.PLAIN, 13));
        forgotButton.setForeground(new Color(0, 100, 255));
        forgotButton.setBorderPainted(false);
        forgotButton.setContentAreaFilled(false);
        forgotButton.setFocusPainted(false);
        forgotButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // LOGIN BUTTON
        loginButton.addActionListener((ActionEvent e) -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            LoginSession session = userManager.login(username, password);

            if (session != null) {
                dispose();
                
                
                new AdminDashboardFrame(userManager, session);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid username or password",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // FORGOT PASSWORD BUTTON
        forgotButton.addActionListener((ActionEvent e) -> {
            String email = JOptionPane.showInputDialog(this,
                    "Enter your email to receive a recovery token:",
                    "Password Recovery",
                    JOptionPane.PLAIN_MESSAGE);

            if (email == null || email.trim().isEmpty()) return;

            try {
                String token = userManager.generateRecoveryToken(email.trim());
                new TokenWindow(this, token).setVisible(true);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add components with spacing
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(titleLabel1);
        centerPanel.add(titleLabel2);
        centerPanel.add(Box.createVerticalStrut(60));
        centerPanel.add(userLabel);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(userField);
        centerPanel.add(Box.createVerticalStrut(25));
        centerPanel.add(passLabel);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(passField);
        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(loginButton);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(forgotButton);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);
        setVisible(true);
    }

    // TOKEN WINDOW (COPYABLE TOKEN + CONTINUE BUTTON)
    private class TokenWindow extends JDialog {
        public TokenWindow(Frame parent, String token) {
            super(parent, "Your Recovery Token", true);
            setSize(400, 200);
            setLocationRelativeTo(parent);
            setLayout(new BorderLayout(10, 10));

            JTextArea tokenArea = new JTextArea(token);
            tokenArea.setEditable(false);
            tokenArea.setFont(new Font("Monospaced", Font.BOLD, 16));
            tokenArea.setLineWrap(true);
            tokenArea.setWrapStyleWord(true);

            JScrollPane scrollPane = new JScrollPane(tokenArea);

            JButton continueBtn = new JButton("Continue");
            continueBtn.addActionListener(e -> {
                dispose();
                new TokenVerifyWindow(parent, token).setVisible(true);
            });

            JPanel bottom = new JPanel();
            bottom.add(continueBtn);

            add(new JLabel("  Copy your token below:"), BorderLayout.NORTH);
            add(scrollPane, BorderLayout.CENTER);
            add(bottom, BorderLayout.SOUTH);
        }
    }

    // TOKEN VERIFICATION WINDOW (BACK + RESET PASSWORD)
    private class TokenVerifyWindow extends JDialog {
        public TokenVerifyWindow(Frame parent, String token) {
            super(parent, "Verify Recovery Token", true);
            setSize(400, 230);
            setLocationRelativeTo(parent);
            setLayout(new GridLayout(5, 1, 5, 5));

            JTextField tokenField = new JTextField();
            JPasswordField newPassField = new JPasswordField();

            JButton resetBtn = new JButton("Reset Password");
            JButton backBtn = new JButton("Back to Token");

            // RESET PASSWORD LOGIC
            resetBtn.addActionListener(e -> {
                String enteredToken = tokenField.getText().trim();
                String newPass = new String(newPassField.getPassword()).trim();

                if (enteredToken.isEmpty() || newPass.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Both fields are required.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean ok = userManager.recoverPassword(enteredToken, newPass);
                if (ok) {
                    JOptionPane.showMessageDialog(this,
                            "Password reset successful!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Invalid token or token expired.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            });

            // BACK BUTTON
            backBtn.addActionListener(e -> {
                dispose();
                new TokenWindow(parent, token).setVisible(true);
            });

            JPanel bottom = new JPanel();
            bottom.add(resetBtn);
            bottom.add(backBtn);

            add(new JLabel("  Enter Recovery Token:"));
            add(tokenField);
            add(new JLabel("  New Password:"));
            add(newPassField);
            add(bottom);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame(new UserManager()));
    }
}