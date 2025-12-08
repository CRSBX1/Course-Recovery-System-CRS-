
package system.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NavigationPanel extends JPanel{
    private JButton btnDashboard;
    private JButton btnCourseRecovery;
    private JButton btnAcademicReport;
    private JButton btnEnroll;
    private Font btnFont;
    
    public NavigationPanel(){
        //Main Panel content
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(220, 0));
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(230, 230, 230)));
        
        //Configure button text font
        btnFont = new Font("SansSerif", Font.BOLD, 12);
        
        //Configure dashboard button
        btnDashboard = createSidebarButton("📊  Dashboard", true);
        
        //Configure Course Recovery Plan Button
        btnCourseRecovery = createSidebarButton("📚  Course Recovery Plan", false);
        
        //Configure Academic Report Button
        btnAcademicReport = createSidebarButton("📝  Generate Academic Report", false);
        
        //Configure Enrollment Button
        btnEnroll = createSidebarButton("✓  Eligibility Check", false);
        
        // Add buttons with spacing
        add(Box.createVerticalStrut(20));
        add(btnDashboard);
        add(Box.createVerticalStrut(5));
        add(btnCourseRecovery);
        add(Box.createVerticalStrut(5));
        add(btnAcademicReport);
        add(Box.createVerticalStrut(5));
        add(btnEnroll);
        
        // Push everything to top, leaving bottom space
        add(Box.createVerticalGlue());
        
        /*
        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        userInfo.setBackground(Color.WHITE);
        userInfo.setMaximumSize(new Dimension(220, 60));
        userInfo.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)));
        
        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font("Serif", Font.PLAIN, 24));
        
        JLabel userName = new JLabel("Admin User");
        userName.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        userInfo.add(userIcon);
        userInfo.add(userName);
        
        add(userInfo);
        */
    }
    
    public void updateButtonStyle(JButton btn, boolean active) {
        if (active) {
            btn.setBackground(new Color(240, 248, 255)); // Light blue
            btn.setForeground(new Color(30, 144, 255)); // Dodger blue
            btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 3, 0, 0, new Color(30, 144, 255)),
                BorderFactory.createEmptyBorder(15, 20, 15, 10)
            ));
        } else {
            btn.setBackground(Color.WHITE);
            btn.setForeground(new Color(60, 60, 60));
            btn.setBorder(BorderFactory.createEmptyBorder(15, 23, 15, 10));
        }
    }
    
    private JButton createSidebarButton(String text, boolean isActive) {
        JButton button = new JButton(text);
        button.setFont(btnFont);
        button.setMaximumSize(new Dimension(220, 60));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Style based on active state
        if (isActive) {
            button.setBackground(new Color(240, 248, 255)); // Light blue
            button.setForeground(new Color(30, 144, 255)); // Dodger blue
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 3, 0, 0, new Color(30, 144, 255)),
                BorderFactory.createEmptyBorder(15, 20, 15, 10)
            ));
        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(new Color(60, 60, 60));
            button.setBorder(BorderFactory.createEmptyBorder(15, 23, 15, 10));
        }
        
        return button;
    }
    
    // Getter methods to access buttons for action listeners
    public JButton getDashboardButton() {
        return btnDashboard;
    }
    
    public JButton getCourseRecoveryButton() {
        return btnCourseRecovery;
    }
    
    public JButton getAcademicReportButton() {
        return btnAcademicReport;
    }
    
    public JButton getEnrollButton() {
        return btnEnroll;
    }
}