/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.app;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 *
 * @author Lenovo
 */
public class OfficerDashboardFrame extends JFrame{
    private TitlePanel title_panel;
    private NavigationPanel naviPanel;
    private Font titleFont;
    private final UserManager userManager;
    private final LoginSession session;
    private JPanel contentPanel;
    private GenerateAcademicReportPanel reportPanel;
    private EnrollStudentPanel enrollPanel;
    private RecoveryPlanPanel planPanel;
    private TrackSetStudentProgressPanel trackPanel;
    
    public OfficerDashboardFrame(UserManager userManager, LoginSession session){
        this.userManager = userManager;
        this.session = session;
        
        title_panel = new TitlePanel();
        titleFont = new Font("Arial",Font.BOLD,16);
        
        setSize(1200,800);
        setLayout(new BorderLayout());

        // Title panel at top
        title_panel.setPreferredSize(new Dimension(1200, 80));
        //Add logoutBtn listener
        title_panel.getLogoutButton().addActionListener(e -> {
            userManager.logout(session.getSessionId());
            dispose();
            new LoginFrame(userManager);
        });
        add(title_panel, BorderLayout.NORTH);

        // Navigation panel on left
        naviPanel = new NavigationPanel();
        naviPanel.setPreferredSize(new Dimension(220, 720));
        add(naviPanel, BorderLayout.WEST);

        // Content panel that will hold different views
        contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        
        // Load dashboard by default
        showDashboard();
        
        //Button Action Listeners
        naviPanel.getDashboardButton().addActionListener((ActionEvent e) -> {
            naviPanel.updateButtonStyle(naviPanel.getDashboardButton(), true);
            naviPanel.updateButtonStyle(naviPanel.getCourseRecoveryButton(), false);
            naviPanel.updateButtonStyle(naviPanel.getAcademicReportButton(), false);
            naviPanel.updateButtonStyle(naviPanel.getEnrollButton(), false);
            naviPanel.updateButtonStyle(naviPanel.getTrackButton(), false);
            showDashboard();
        });
        
        naviPanel.getCourseRecoveryButton().addActionListener((ActionEvent e) -> {
            naviPanel.updateButtonStyle(naviPanel.getDashboardButton(), false);
            naviPanel.updateButtonStyle(naviPanel.getCourseRecoveryButton(), true);
            naviPanel.updateButtonStyle(naviPanel.getAcademicReportButton(), false);
            naviPanel.updateButtonStyle(naviPanel.getEnrollButton(), false);
            naviPanel.updateButtonStyle(naviPanel.getTrackButton(), false);
            showCourseRecovery();
        });
        
        naviPanel.getAcademicReportButton().addActionListener((ActionEvent e) -> {
            naviPanel.updateButtonStyle(naviPanel.getDashboardButton(), false);
            naviPanel.updateButtonStyle(naviPanel.getCourseRecoveryButton(), false);
            naviPanel.updateButtonStyle(naviPanel.getAcademicReportButton(), true);
            naviPanel.updateButtonStyle(naviPanel.getEnrollButton(), false);
            naviPanel.updateButtonStyle(naviPanel.getTrackButton(), false);
            showAcademicReport();
        });
        
        naviPanel.getEnrollButton().addActionListener((ActionEvent e) -> {
            naviPanel.updateButtonStyle(naviPanel.getDashboardButton(), false);
            naviPanel.updateButtonStyle(naviPanel.getCourseRecoveryButton(), false);
            naviPanel.updateButtonStyle(naviPanel.getAcademicReportButton(), false);
            naviPanel.updateButtonStyle(naviPanel.getEnrollButton(), true);
            naviPanel.updateButtonStyle(naviPanel.getTrackButton(), false);
            showEnrollment();
        });
        
        naviPanel.getTrackButton().addActionListener((ActionEvent e) -> {
            naviPanel.updateButtonStyle(naviPanel.getDashboardButton(), false);
            naviPanel.updateButtonStyle(naviPanel.getCourseRecoveryButton(), false);
            naviPanel.updateButtonStyle(naviPanel.getAcademicReportButton(), false);
            naviPanel.updateButtonStyle(naviPanel.getEnrollButton(), false);
            naviPanel.updateButtonStyle(naviPanel.getTrackButton(), true);
            showTrack();
        });
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    private void showDashboard() {
        // Clear existing content
        contentPanel.removeAll();
        
        // Create dashboard panel
        JPanel dashboardPanel = new JPanel(new BorderLayout(0, 20));
        dashboardPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 0, 20));
        dashboardPanel.setBackground(Color.white);
        
        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome, " + session.getUser().getRole().getDisplayName());
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        welcomeLabel.setBorder(new EmptyBorder(0, 0, 30, 0));

        // Stats panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setOpaque(false);

        int sickStudents = DataRepository.getRecoveringStudent();
        int pendingApprovals = DataRepository.getPendingApproval();
        int healedStudents = DataRepository.getRecoveredStudents();

        statsPanel.add(createStatCard("Recovering Students", String.valueOf(sickStudents), "👥", new Color(200, 200, 200)));
        statsPanel.add(createStatCard("Pending Enrollment Approval", String.valueOf(pendingApprovals), "✓", new Color(200, 200, 200)));
        statsPanel.add(createStatCard("Recovered Students", String.valueOf(healedStudents), "✨", new Color(200, 200, 200)));

        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setOpaque(false);
        topSection.add(welcomeLabel, BorderLayout.NORTH);
        topSection.add(statsPanel, BorderLayout.CENTER);

        dashboardPanel.add(topSection, BorderLayout.NORTH);
        
        // Add to content panel
        contentPanel.add(dashboardPanel, BorderLayout.CENTER);
        
        // Refresh the display
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showCourseRecovery() {
        contentPanel.removeAll();
        planPanel = new RecoveryPlanPanel();
        contentPanel.add(planPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showAcademicReport() {
        contentPanel.removeAll();
        reportPanel = new GenerateAcademicReportPanel();
        contentPanel.add(reportPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showEnrollment() {
        contentPanel.removeAll();
        enrollPanel = new EnrollStudentPanel(this);
        contentPanel.add(enrollPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showTrack(){
        contentPanel.removeAll();
        trackPanel = new TrackSetStudentProgressPanel();
        contentPanel.add(trackPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
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
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(80, 80, 80));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

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