/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.app;

import javax.swing.*;
import java.awt.*;

/**
 * TitlePanel with improved layout using BorderLayout
 */
public class TitlePanel extends JPanel {
    private JLabel titleLabel;
    private JButton logoutBtn;

    public TitlePanel() {
        // Use BorderLayout for left-right alignment
        setLayout(new BorderLayout(20, 0)); // 20px horizontal gap
        setBackground(new Color(0x7DC3E3));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // top, left, bottom, right padding

        // Title label
        titleLabel = new JLabel("Course Recovery System");
        titleLabel.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // Logout button
        logoutBtn = new JButton("Logout ↪");
        logoutBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(new Color(220, 53, 69));
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setPreferredSize(new Dimension(140, 40));

        // Hover effect
        logoutBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutBtn.setBackground(new Color(200, 35, 51));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutBtn.setBackground(new Color(220, 53, 69));
            }
        });

        // Left panel for title (align left)
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false); // transparent background
        leftPanel.add(titleLabel);

        // Right panel for button (align right)
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(logoutBtn);

        // Add panels to main panel
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }

    public JButton getLogoutButton() {
        return logoutBtn;
    }
}

