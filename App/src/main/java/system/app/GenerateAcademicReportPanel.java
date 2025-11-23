/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.app;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.File;

/**
 *
 * @author alfredwilliamjulianto
 */
public class GenerateAcademicReportPanel extends JPanel {

    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JLabel studentIDValueLabel;
    private JLabel fullNameValueLabel;
    private JLabel intakeYearValueLabel;
    private JLabel majorValueLabel;
    private JButton downloadButton;
    private JButton sendEmailButton;

    private Student selectedStudent;
    private PerformanceReport currentReport;

    public GenerateAcademicReportPanel() {
        setLayout(new BorderLayout(30, 30));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        setPreferredSize(new Dimension(1440, 1024));

        initComponents();
    }

    private void initComponents() {
        // Title at top
        add(createTitlePanel(), BorderLayout.NORTH);

        // Main content: Student list + Student details
        add(createMainContentPanel(), BorderLayout.CENTER);
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Generate an Academic Report");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(new Color(33, 33, 33));
        panel.add(title);

        return panel;
    }

    private JPanel createMainContentPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 30);

        // Left: Student list table (takes 45% width)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.45;
        gbc.weighty = 1.0;
        mainPanel.add(createStudentListPanel(), gbc);

        // Right: Student details (takes 55% width)
        gbc.gridx = 1;
        gbc.weightx = 0.55;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(createStudentDetailsPanel(), gbc);

        return mainPanel;
    }

    private JPanel createStudentListPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(Color.WHITE);

        // Section title
        JLabel sectionTitle = new JLabel("List of Students");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        sectionTitle.setForeground(new Color(33, 33, 33));
        panel.add(sectionTitle, BorderLayout.NORTH);

        // Table
        String[] columns = {"Student ID", "Full Name"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(tableModel);
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        studentTable.setRowHeight(40);
        studentTable.setShowGrid(true);
        studentTable.setGridColor(new Color(200, 200, 200));
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.setSelectionBackground(new Color(173, 216, 230));
        studentTable.setSelectionForeground(Color.BLACK);

        // Table header styling
        studentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        studentTable.getTableHeader().setBackground(Color.WHITE);
        studentTable.getTableHeader().setForeground(new Color(33, 33, 33));
        studentTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 200, 200)));

        // Center align cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        studentTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        studentTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        // Load students
        loadStudentsIntoTable();

        // Selection listener
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String studentID = (String) tableModel.getValueAt(selectedRow, 0);
                    displayStudentDetails(studentID);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStudentDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(Color.WHITE);

        // Section title
        JLabel sectionTitle = new JLabel("Student Details");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        sectionTitle.setForeground(new Color(33, 33, 33));
        panel.add(sectionTitle, BorderLayout.NORTH);

        // Create a wrapper panel for details card + buttons
        JPanel centerWrapper = new JPanel(new BorderLayout(0, 0));
        centerWrapper.setBackground(Color.WHITE);

        // Details card (light blue background)
        JPanel detailsCard = new JPanel();
        detailsCard.setLayout(new BoxLayout(detailsCard, BoxLayout.Y_AXIS));
        detailsCard.setBackground(new Color(173, 216, 230)); // Light blue
        detailsCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 2),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        // Student info labels - Initialize as JLabels
        studentIDValueLabel = new JLabel("-");
        fullNameValueLabel = new JLabel("-");
        intakeYearValueLabel = new JLabel("-");
        majorValueLabel = new JLabel("-");

        // Style the labels
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
        studentIDValueLabel.setFont(labelFont);
        fullNameValueLabel.setFont(labelFont);
        intakeYearValueLabel.setFont(labelFont);
        majorValueLabel.setFont(labelFont);

        detailsCard.add(createDetailRow("Student ID:", studentIDValueLabel));
        detailsCard.add(Box.createVerticalStrut(20));
        detailsCard.add(createDetailRow("Full Name:", fullNameValueLabel));
        detailsCard.add(Box.createVerticalStrut(20));
        detailsCard.add(createDetailRow("Intake Year:", intakeYearValueLabel));
        detailsCard.add(Box.createVerticalStrut(20));
        detailsCard.add(createDetailRow("Major:", majorValueLabel));
        detailsCard.add(Box.createVerticalStrut(40)); // Extra space before buttons

        // ✅ ADD BUTTONS DIRECTLY INSIDE THE BLUE CARD
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false); // Make background transparent

        // Download button (green)
        downloadButton = new JButton("Download to Device");
        downloadButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        downloadButton.setForeground(Color.WHITE);
        downloadButton.setBackground(new Color(46, 204, 113));
        downloadButton.setFocusPainted(false);
        downloadButton.setBorderPainted(false);
        downloadButton.setOpaque(true); // ✅ IMPORTANT
        downloadButton.setPreferredSize(new Dimension(180, 45));
        downloadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        downloadButton.addActionListener(e -> downloadReport());
        downloadButton.setEnabled(false);

        // Send email button (red)
        sendEmailButton = new JButton("Send to Email");
        sendEmailButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendEmailButton.setForeground(Color.WHITE);
        sendEmailButton.setBackground(new Color(231, 76, 60));
        sendEmailButton.setFocusPainted(false);
        sendEmailButton.setBorderPainted(false);
        sendEmailButton.setOpaque(true); // ✅ IMPORTANT
        sendEmailButton.setPreferredSize(new Dimension(180, 45));
        sendEmailButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendEmailButton.addActionListener(e -> sendEmail());
        sendEmailButton.setEnabled(false);

        buttonPanel.add(downloadButton);
        buttonPanel.add(sendEmailButton);

        detailsCard.add(buttonPanel); // Add buttons inside the blue card

        centerWrapper.add(detailsCard, BorderLayout.CENTER);
        panel.add(centerWrapper, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createDetailRow(String labelText, JLabel valueLabel) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(new Color(33, 33, 33));
        label.setPreferredSize(new Dimension(120, 25));

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
        button.setPreferredSize(new Dimension(180, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(backgroundColor.darker());
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });

        return button;
    }

    private void loadStudentsIntoTable() {
        tableModel.setRowCount(0);

        if (DataRepository.studentList != null) {
            for (Student student : DataRepository.studentList) {
                tableModel.addRow(new Object[]{
                    student.getStudentID(),
                    student.getName()
                });
            }
        }
    }

    private void displayStudentDetails(String studentID) {
        // Find student
        selectedStudent = null;
        for (Student s : DataRepository.studentList) {
            if (s.getStudentID().equals(studentID)) {
                selectedStudent = s;
                break;
            }
        }

        if (selectedStudent != null) {
            // Update labels directly
            studentIDValueLabel.setText(selectedStudent.getStudentID());
            fullNameValueLabel.setText(selectedStudent.getName());
            intakeYearValueLabel.setText(String.valueOf(selectedStudent.getYear()));
            majorValueLabel.setText(selectedStudent.getProgram());

            // Generate report
            currentReport = new PerformanceReport(
                    "R" + System.currentTimeMillis(),
                    selectedStudent,
                    selectedStudent.getSemester(),
                    String.valueOf(selectedStudent.getYear())
            );

            // Enable buttons
            downloadButton.setEnabled(true);
            sendEmailButton.setEnabled(true);

            System.out.println("✅ Student selected: " + selectedStudent.getName());
            System.out.println("✅ Report generated: " + currentReport.getReportID());
            System.out.println("✅ Buttons enabled!");
        }
    }

    private void downloadReport() {
        if (currentReport == null || selectedStudent == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a student first!",
                    "No Student Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        System.out.println("📥 Download button clicked!");

        // File chooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Report as PDF");

        // Default filename
        String defaultName = selectedStudent.getName().replace(" ", "_") + "_Report.pdf";
        fileChooser.setSelectedFile(new File(defaultName));

        // Set file filter for PDF only
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".pdf");
            }

            public String getDescription() {
                return "PDF Documents (*.pdf)";
            }
        });

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".pdf")) {
                filePath += ".pdf";
            }

            try {
                System.out.println("💾 Exporting to: " + filePath);
                currentReport.exportToPDF(filePath);
                JOptionPane.showMessageDialog(this,
                        "Report saved successfully!\n\nLocation: " + filePath,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                System.out.println("✅ PDF exported successfully!");
            } catch (Exception ex) {
                System.err.println("❌ Error exporting PDF: " + ex.getMessage());
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Error saving report: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void sendEmail() {
        if (currentReport == null || selectedStudent == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a student first!",
                    "No Student Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        System.out.println("📧 Send Email button clicked!");

        // PLACEHOLDER: Email functionality
        String email = selectedStudent.getEmail();
        JOptionPane.showMessageDialog(this,
                "📧 Email Feature - Placeholder\n\n"
                + "This feature will send the report to:\n"
                + email + "\n\n"
                + "Email functionality will be implemented in future version.\n"
                + "(Requires Java Mail API integration)",
                "Email Placeholder",
                JOptionPane.INFORMATION_MESSAGE);

        System.out.println("📧 Email placeholder shown for: " + email);
    }

    /**
     * Refresh the student list (call after data changes)
     */
    public void refreshStudentList() {
        loadStudentsIntoTable();
    }
}
