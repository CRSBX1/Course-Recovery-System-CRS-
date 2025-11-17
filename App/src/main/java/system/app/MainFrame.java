package system.app;

import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame(UserManager userManager, LoginSession session) {
        setTitle("CRS - Main Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel(
                "<html>Welcome, <b>" + session.getUser().getUsername() + "</b><br>"
                        + "Role: " + session.getUser().getRole().getDisplayName() + "</html>",
                SwingConstants.CENTER
        );

        add(label);
    }
}
