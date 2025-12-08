package system.app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.IOException;

public class EnrollStudentPanel extends JPanel {
    private JTable studentTable;
    private StudentTableModel model;
    private JScrollPane scrollPane;
    private JButton eligibleBtn;
    private EligibilityChecker check;
    private Student selectedStudent;
    
    // Student details components
    private JLabel idValueLabel;
    private JLabel nameValueLabel;
    private JLabel semesterValueLabel;
    private JLabel majorValueLabel;
    private JLabel cgpaValueLabel;
    private JLabel failedCoursesValueLabel;
    
    public EnrollStudentPanel(JFrame frame) {
        setLayout(new BorderLayout(30, 30));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 50, 40, 50));
        
        model = new StudentTableModel();
        studentTable = new JTable(model.getTableModel());
        check = new EligibilityChecker();
        
        initComponents(frame);
    }
    
    private void initComponents(JFrame frame) {
        // Title at top
        add(createTitlePanel(), BorderLayout.NORTH);
        
        // Main content
        add(createMainContentPanel(frame), BorderLayout.CENTER);
    }
    
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Student Eligibility Check & Enrollment");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(new Color(33, 33, 33));
        panel.add(title);
        
        return panel;
    }
    
    private JPanel createMainContentPanel(JFrame frame) {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 30);
        
        // Left: Student list table (40% width)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.40;
        gbc.weighty = 1.0;
        mainPanel.add(createStudentListPanel(), gbc);
        
        // Right: Student details (60% width)
        gbc.gridx = 1;
        gbc.weightx = 0.60;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(createStudentDetailsPanel(frame), gbc);
        
        return mainPanel;
    }
    
    private JPanel createStudentListPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(Color.WHITE);
        
        // Section title
        JLabel sectionTitle = new JLabel("Unenrolled Students");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        sectionTitle.setForeground(new Color(33, 33, 33));
        panel.add(sectionTitle, BorderLayout.NORTH);
        
        // Table styling
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        studentTable.setRowHeight(50);
        studentTable.setShowGrid(true);
        studentTable.setGridColor(new Color(200, 200, 200));
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.setSelectionBackground(new Color(173, 216, 230));
        studentTable.setSelectionForeground(Color.BLACK);
        
        // Table header styling
        JTableHeader header = studentTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(Color.WHITE);
        header.setForeground(new Color(33, 33, 33));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 200, 200)));
        
        // Center align cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        studentTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        studentTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        
        // Selection listener
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int viewRow = studentTable.getSelectedRow();
                if (viewRow >= 0) {
                    int modelRow = studentTable.convertRowIndexToModel(viewRow);
                    String sID = (String) model.getTableModel().getValueAt(modelRow, 0);
                    selectedStudent = DataRepository.findStudentByID(sID);
                    updateStudentDetails();
                }
            }
        });
        
        scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createStudentDetailsPanel(JFrame frame) {
        JPanel rightPanel = new JPanel(new BorderLayout(0, 20));
        rightPanel.setBackground(Color.WHITE);
        
        // Section title
        JLabel sectionTitle = new JLabel("Student Details");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        sectionTitle.setForeground(new Color(33, 33, 33));
        rightPanel.add(sectionTitle, BorderLayout.NORTH);
        
        // Details card (light blue background)
        JPanel detailsCard = new JPanel();
        detailsCard.setLayout(new BoxLayout(detailsCard, BoxLayout.Y_AXIS));
        detailsCard.setBackground(new Color(173, 216, 230)); // Light blue
        detailsCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 2),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        
        // Initialize value labels
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 15);
        idValueLabel = new JLabel("-");
        nameValueLabel = new JLabel("-");
        semesterValueLabel = new JLabel("-");
        majorValueLabel = new JLabel("-");
        cgpaValueLabel = new JLabel("-");
        failedCoursesValueLabel = new JLabel("-");
        
        idValueLabel.setFont(labelFont);
        nameValueLabel.setFont(labelFont);
        semesterValueLabel.setFont(labelFont);
        majorValueLabel.setFont(labelFont);
        cgpaValueLabel.setFont(labelFont);
        failedCoursesValueLabel.setFont(labelFont);
        
        // Add detail rows
        detailsCard.add(createDetailRow("Student ID:", idValueLabel));
        detailsCard.add(Box.createVerticalStrut(12));
        detailsCard.add(createDetailRow("Full Name:", nameValueLabel));
        detailsCard.add(Box.createVerticalStrut(12));
        detailsCard.add(createDetailRow("Semester:", semesterValueLabel));
        detailsCard.add(Box.createVerticalStrut(12));
        detailsCard.add(createDetailRow("Major:", majorValueLabel));
        detailsCard.add(Box.createVerticalStrut(12));
        detailsCard.add(createDetailRow("CGPA:", cgpaValueLabel));
        detailsCard.add(Box.createVerticalStrut(12));
        
        // Failed courses section (can be multi-line)
        JPanel failedCoursesPanel = new JPanel(new BorderLayout(10, 5));
        failedCoursesPanel.setOpaque(false);
        
        JLabel failedLabel = new JLabel("Failed Courses:");
        failedLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        failedLabel.setForeground(new Color(33, 33, 33));
        
        failedCoursesValueLabel.setVerticalAlignment(SwingConstants.TOP);
        
        failedCoursesPanel.add(failedLabel, BorderLayout.NORTH);
        failedCoursesPanel.add(failedCoursesValueLabel, BorderLayout.CENTER);
        
        detailsCard.add(failedCoursesPanel);
        detailsCard.add(Box.createVerticalStrut(20));
        
        // Check Eligibility button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonPanel.setOpaque(false);
        
        eligibleBtn = createStyledButton("Check Eligibility & Enroll", new Color(46, 204, 113));
        eligibleBtn.setPreferredSize(new Dimension(240, 45));
        eligibleBtn.addActionListener((ActionEvent e) -> {
            if (selectedStudent == null) {
                showInfoDialog(frame, "Please select a student first", "No Selection");
            } else if (check.checkProgressionEligibility(selectedStudent)) {
                try {
                    showSuccessDialog(frame, 
                        selectedStudent.getStudentName() + " is eligible for enrollment.", 
                        "Eligible - Enrolling Student");
                    
                    selectedStudent.setEnrollStatus("Enrolled");
                    FileUtils.writeToFile("Data/Students.txt", FileUtils::writeStudent);
                    EmailPart.sendEnrollmentEmail(selectedStudent);
                    model.refreshTableContents();
                    clearStudentDetails();
                    
                } catch (IOException ex) {
                    showErrorDialog(frame, "Error when enrolling student: " + ex.getMessage());
                }
            } else {
                showInfoDialog(frame, 
                    selectedStudent.getStudentName() + " is not eligible for enrollment yet.\n\n" +
                    "Requirements:\n" +
                    "• CGPA must be ≥ 2.0 (Current: " + String.format("%.2f", selectedStudent.getCGPA()) + ")\n" +
                    "• Failed courses must be ≤ 3 (Current: " + selectedStudent.getFailedCoursesCount() + ")",
                    "Not Eligible");
            }
        });
        
        buttonPanel.add(eligibleBtn);
        detailsCard.add(buttonPanel);
        
        rightPanel.add(detailsCard, BorderLayout.CENTER);
        
        return rightPanel;
    }
    
    private JPanel createDetailRow(String labelText, JLabel valueLabel) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setOpaque(false);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setForeground(new Color(33, 33, 33));
        label.setPreferredSize(new Dimension(130, 25));
        
        row.add(label);
        row.add(valueLabel);
        
        return row;
    }
    
    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.darker());
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
        
        return button;
    }
    
    private void updateStudentDetails() {
        if (selectedStudent != null) {
            idValueLabel.setText(selectedStudent.getStudentID());
            nameValueLabel.setText(selectedStudent.getStudentName());
            semesterValueLabel.setText(String.valueOf(selectedStudent.getSemester()));
            majorValueLabel.setText(selectedStudent.getStudentProgram());
            cgpaValueLabel.setText(String.format("%.2f", selectedStudent.getCGPA()));
            
            ArrayList<String> courseNames = new ArrayList<>();
            for (Course c : selectedStudent.getFailedCourses()) {
                courseNames.add(c.getName());
            }
            
            if (!courseNames.isEmpty()) {
                failedCoursesValueLabel.setText("<html>" + String.join("<br>• ", courseNames) + "</html>");
            } else {
                failedCoursesValueLabel.setText("None");
            }
        }
    }
    
    private void clearStudentDetails() {
        selectedStudent = null;
        idValueLabel.setText("-");
        nameValueLabel.setText("-");
        semesterValueLabel.setText("-");
        majorValueLabel.setText("-");
        cgpaValueLabel.setText("-");
        failedCoursesValueLabel.setText("-");
    }
    
    // Modern dialog methods
    private void showSuccessDialog(JFrame parent, String message, String title) {
        JDialog dialog = new JDialog(parent, title, true);
        dialog.setSize(450, 200);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout(15, 15));
        
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);
        
        // Icon and message
        JLabel iconLabel = new JLabel("✓");
        iconLabel.setFont(new Font("Serif", Font.PLAIN, 48));
        iconLabel.setForeground(new Color(46, 204, 113));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel messageLabel = new JLabel("<html><center>" + message + "</center></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JButton okButton = createStyledButton("OK", new Color(46, 204, 113));
        okButton.setPreferredSize(new Dimension(100, 35));
        okButton.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(okButton);
        
        contentPanel.add(iconLabel, BorderLayout.WEST);
        contentPanel.add(messageLabel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(contentPanel);
        dialog.setVisible(true);
    }
    
    
    private void showInfoDialog(JFrame parent, String message, String title) {
        JDialog dialog = new JDialog(parent, title, true);

        // Check if message is long to adjust dialog size
        boolean isLongMessage = message.length() > 100 || message.contains("\n");
        boolean notChosen = message.equals("Please select a student first");
        dialog.setSize(450, isLongMessage ? 280 : 200);

        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout(15, 15));

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);

        // Icon
        JLabel iconLabel = new JLabel("ℹ");
        iconLabel.setFont(new Font("Serif", Font.PLAIN, 48));
        iconLabel.setForeground(new Color(52, 152, 219));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBackground(Color.WHITE);
        messagePanel.setBorder(new EmptyBorder(10, 20, 10, 10));
        if(notChosen){
            messagePanel.setBorder(new EmptyBorder(10, 80, 10, 10));
        }

        JLabel messageLabel = new JLabel("<html><div style='width: 300px;'>" + 
                                        message.replace("\n", "<br>") + 
                                        "</div></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        messageLabel.setVerticalAlignment(SwingConstants.CENTER);  // Center vertically
        messageLabel.setHorizontalAlignment(SwingConstants.LEFT);

        messagePanel.add(messageLabel, BorderLayout.CENTER);

        // Button
        JButton okButton = createStyledButton("OK", new Color(52, 152, 219));
        okButton.setPreferredSize(new Dimension(100, 35));
        okButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(okButton);

        contentPanel.add(iconLabel, BorderLayout.WEST);
        contentPanel.add(messagePanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    private void showErrorDialog(JFrame parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}