/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.app;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.Desktop;
import java.io.File;
import java.util.Map;
import java.util.List;
import java.util.LinkedHashMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author alfredwilliamjulianto
 */
public class GenerateAcademicReportPanel extends JPanel {

    // Components
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JLabel studentIDValueLabel;
    private JLabel fullNameValueLabel;
    private JLabel intakeYearValueLabel;
    private JLabel majorValueLabel;
    private JButton viewReportButton;
    private JButton downloadButton;
    private JButton sendEmailButton;
    private JPanel chartContainerPanel; // Container for the chart

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

        // Main content: Student list + Student details + Chart
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

        // Left: Student list table (takes 40% width)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.40;
        gbc.weighty = 1.0;
        mainPanel.add(createStudentListPanel(), gbc);

        // Right: Student details + Chart (takes 60% width)
        gbc.gridx = 1;
        gbc.weightx = 0.60;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(createRightPanel(), gbc);

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

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // Student Details (takes less space now)
        gbc.gridy = 0;
        gbc.weighty = 0.45;
        rightPanel.add(createStudentDetailsPanel(), gbc);

        // Grade Chart (takes more space)
        gbc.gridy = 1;
        gbc.weighty = 0.55;
        gbc.insets = new Insets(20, 0, 0, 0);
        rightPanel.add(createChartPanel(), gbc);

        return rightPanel;
    }

    private JPanel createStudentDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(Color.WHITE);

        // Section title
        JLabel sectionTitle = new JLabel("Student Details");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        sectionTitle.setForeground(new Color(33, 33, 33));
        panel.add(sectionTitle, BorderLayout.NORTH);

        // Details card (light blue background)
        JPanel detailsCard = new JPanel();
        detailsCard.setLayout(new BoxLayout(detailsCard, BoxLayout.Y_AXIS));
        detailsCard.setBackground(new Color(173, 216, 230)); // Light blue
        detailsCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
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
        detailsCard.add(Box.createVerticalStrut(15));
        detailsCard.add(createDetailRow("Full Name:", fullNameValueLabel));
        detailsCard.add(Box.createVerticalStrut(15));
        detailsCard.add(createDetailRow("Intake Year:", intakeYearValueLabel));
        detailsCard.add(Box.createVerticalStrut(15));
        detailsCard.add(createDetailRow("Major:", majorValueLabel));
        detailsCard.add(Box.createVerticalStrut(20));

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);

        // View Report button (blue)
        viewReportButton = createStyledButton("View", new Color(52, 152, 219));
        viewReportButton.addActionListener(e -> viewReport());
        viewReportButton.setEnabled(false);

        // Download button (green)
        downloadButton = createStyledButton("Download", new Color(46, 204, 113));
        downloadButton.addActionListener(e -> downloadReport());
        downloadButton.setEnabled(false);

        // Send email button (red)
        sendEmailButton = createStyledButton("Send Email", new Color(231, 76, 60));
        sendEmailButton.addActionListener(e -> sendEmail());
        sendEmailButton.setEnabled(false);

        buttonPanel.add(viewReportButton);
        buttonPanel.add(downloadButton);
        buttonPanel.add(sendEmailButton);

        detailsCard.add(buttonPanel);

        panel.add(detailsCard, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createChartPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(Color.WHITE);

        // Section title
        JLabel sectionTitle = new JLabel("Grade Distribution");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        sectionTitle.setForeground(new Color(33, 33, 33));
        panel.add(sectionTitle, BorderLayout.NORTH);

        // Chart container (will be updated when student is selected)
        chartContainerPanel = new JPanel(new BorderLayout());
        chartContainerPanel.setBackground(Color.WHITE);
        chartContainerPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        // Initial placeholder
        JLabel placeholder = new JLabel("Select a student to view grade distribution", SwingConstants.CENTER);
        placeholder.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        placeholder.setForeground(new Color(128, 128, 128));
        chartContainerPanel.add(placeholder, BorderLayout.CENTER);

        panel.add(chartContainerPanel, BorderLayout.CENTER);

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
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(110, 40));
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
            viewReportButton.setEnabled(true);
            downloadButton.setEnabled(true);
            sendEmailButton.setEnabled(true);

            // Update chart
            updateGradeDistributionChart();

            System.out.println("✅ Student selected: " + selectedStudent.getName());
            System.out.println("✅ Report generated: " + currentReport.getReportID());
            System.out.println("✅ Buttons enabled!");
            System.out.println("📊 Chart updated!");
        }
    }

    // ===== GRADE DISTRIBUTION CHART METHODS =====
    /**
     * Count grades from student's enrollments
     */
    private Map<String, Integer> countGrades(List<CourseEnrollment> enrollments) {
        Map<String, Integer> gradeCount = new LinkedHashMap<>();

        // Initialize all possible grades
        gradeCount.put("A+", 0);
        gradeCount.put("A", 0);
        gradeCount.put("A-", 0);
        gradeCount.put("B+", 0);
        gradeCount.put("B", 0);
        gradeCount.put("B-", 0);
        gradeCount.put("C+", 0);
        gradeCount.put("C", 0);
        gradeCount.put("C-", 0);
        gradeCount.put("D+", 0);
        gradeCount.put("D", 0);
        gradeCount.put("F", 0);

        // Count each grade
        for (CourseEnrollment enrollment : enrollments) {
            String grade = enrollment.getOverallGrade();
            if (gradeCount.containsKey(grade)) {
                gradeCount.put(grade, gradeCount.get(grade) + 1);
            }
        }

        return gradeCount;
    }

    /**
     * Create the grade distribution bar chart
     */
    private JPanel createGradeDistributionChartPanel() {
        if (selectedStudent == null) {
            JPanel emptyPanel = new JPanel(new BorderLayout());
            emptyPanel.setBackground(Color.WHITE);
            JLabel label = new JLabel("No student selected", SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            emptyPanel.add(label, BorderLayout.CENTER);
            return emptyPanel;
        }

        // Get enrollments
        List<CourseEnrollment> enrollments = selectedStudent.getEnrollment();

        if (enrollments == null || enrollments.isEmpty()) {
            JPanel emptyPanel = new JPanel(new BorderLayout());
            emptyPanel.setBackground(Color.WHITE);
            JLabel label = new JLabel("No course enrollments found", SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            emptyPanel.add(label, BorderLayout.CENTER);
            return emptyPanel;
        }

        // Count grades
        Map<String, Integer> gradeCount = countGrades(enrollments);

        // Create dataset (only include grades that exist) - single series for thick bars
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry : gradeCount.entrySet()) {
            if (entry.getValue() > 0) {
                dataset.addValue(entry.getValue(), "Courses", entry.getKey());
            }
        }

        // Create chart
        JFreeChart chart = ChartFactory.createBarChart(
                null, // No title (we have it above)
                "Grade",
                "Number of Courses",
                dataset,
                PlotOrientation.VERTICAL,
                false, // No legend
                true, // Tooltips
                false // URLs
        );

        // Customize chart appearance
        chart.setBackgroundPaint(Color.WHITE);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(245, 245, 245));
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);
        plot.setOutlineVisible(false);

        // Set Y-axis to display only INTEGER values (no decimals for course count)
        org.jfree.chart.axis.NumberAxis rangeAxis = (org.jfree.chart.axis.NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(org.jfree.chart.axis.NumberAxis.createIntegerTickUnits());

        // Customize bar colors - different color for each grade
        // Color map for grades
        final Map<String, Color> gradeColors = new LinkedHashMap<>();
        gradeColors.put("A+", new Color(46, 204, 113));   // Bright Green
        gradeColors.put("A", new Color(39, 174, 96));      // Green
        gradeColors.put("A-", new Color(88, 214, 141));    // Light Green
        gradeColors.put("B+", new Color(52, 152, 219));    // Bright Blue
        gradeColors.put("B", new Color(41, 128, 185));     // Blue
        gradeColors.put("B-", new Color(93, 173, 226));    // Light Blue
        gradeColors.put("C+", new Color(241, 196, 15));    // Yellow
        gradeColors.put("C", new Color(243, 156, 18));     // Orange
        gradeColors.put("C-", new Color(230, 126, 34));    // Dark Orange
        gradeColors.put("D+", new Color(231, 76, 60));     // Red
        gradeColors.put("D", new Color(192, 57, 43));      // Dark Red
        gradeColors.put("F", new Color(127, 140, 141));    // Gray

        // Create custom renderer that colors each bar based on grade
        BarRenderer renderer = new BarRenderer() {
            @Override
            public Paint getItemPaint(int row, int column) {
                // Get the grade (category) for this bar
                String grade = (String) dataset.getColumnKey(column);
                // Return the corresponding color
                return gradeColors.getOrDefault(grade, new Color(52, 152, 219));
            }
        };

        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
        plot.setRenderer(renderer);

        // Create chart panel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 300));
        chartPanel.setBackground(Color.WHITE);

        // Add statistics label
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(Color.WHITE);
        wrapperPanel.add(chartPanel, BorderLayout.CENTER);

        // Add stats at bottom
        JLabel statsLabel = new JLabel(
                String.format("Total Courses: %d  |  CGPA: %.2f  |  Average Grade: %s",
                        enrollments.size(),
                        selectedStudent.getCGPA(),
                        getAverageGrade(selectedStudent.getCGPA())
                ),
                SwingConstants.CENTER
        );
        statsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statsLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        wrapperPanel.add(statsLabel, BorderLayout.SOUTH);

        return wrapperPanel;
    }

    /**
     * Update the chart when a new student is selected
     */
    private void updateGradeDistributionChart() {
        chartContainerPanel.removeAll();
        JPanel newChart = createGradeDistributionChartPanel();
        chartContainerPanel.add(newChart, BorderLayout.CENTER);
        chartContainerPanel.revalidate();
        chartContainerPanel.repaint();
    }

    /**
     * Convert CGPA to average grade letter
     */
    private String getAverageGrade(double cgpa) {
        if (cgpa >= 3.7) {
            return "A";
        }
        if (cgpa >= 3.3) {
            return "A-";
        }
        if (cgpa >= 3.0) {
            return "B+";
        }
        if (cgpa >= 2.7) {
            return "B";
        }
        if (cgpa >= 2.3) {
            return "B-";
        }
        if (cgpa >= 2.0) {
            return "C+";
        }
        if (cgpa >= 1.7) {
            return "C";
        }
        if (cgpa >= 1.3) {
            return "C-";
        }
        if (cgpa >= 1.0) {
            return "D+";
        }
        return "D";
    }

    // ===== BUTTON ACTION METHODS =====
    private void viewReport() {
        if (currentReport == null || selectedStudent == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a student first!",
                    "No Student Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        System.out.println("👁️ View Report button clicked!");

        try {
            // Check if Desktop is supported
            if (!java.awt.Desktop.isDesktopSupported()) {
                JOptionPane.showMessageDialog(this,
                        "Desktop operations are not supported on this system.\n"
                        + "Please use the Download button instead.",
                        "Feature Not Supported",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

            // Check if OPEN action is supported
            if (!desktop.isSupported(java.awt.Desktop.Action.OPEN)) {
                JOptionPane.showMessageDialog(this,
                        "Opening files is not supported on this system.\n"
                        + "Please use the Download button instead.",
                        "Feature Not Supported",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Show loading message
            JOptionPane loadingPane = new JOptionPane(
                    "Generating report preview...\nPlease wait.",
                    JOptionPane.INFORMATION_MESSAGE,
                    JOptionPane.DEFAULT_OPTION,
                    null,
                    new Object[]{},
                    null
            );
            JDialog loadingDialog = loadingPane.createDialog(this, "Loading");

            // Create temporary file
            String tempDir = System.getProperty("java.io.tmpdir");
            String fileName = selectedStudent.getName().replace(" ", "_") + "_Report_Preview.pdf";
            String tempFilePath = tempDir + File.separator + fileName;
            File tempFile = new File(tempFilePath);

            // Generate PDF in background
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    System.out.println("📄 Generating temporary PDF at: " + tempFilePath);
                    currentReport.exportToPDF(tempFilePath);
                    return null;
                }

                @Override
                protected void done() {
                    loadingDialog.dispose();
                    try {
                        get(); // Check for exceptions

                        // Open the PDF with system default viewer
                        System.out.println("👁️ Opening PDF with default viewer...");
                        desktop.open(tempFile);

                        JOptionPane.showMessageDialog(
                                GenerateAcademicReportPanel.this,
                                "Report opened in your default PDF viewer.\n\n"
                                + "Note: This is a temporary preview file.",
                                "Report Opened",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        System.out.println("✅ PDF opened successfully!");

                    } catch (Exception ex) {
                        System.err.println("❌ Error opening PDF: " + ex.getMessage());
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(
                                GenerateAcademicReportPanel.this,
                                "Error opening report:\n" + ex.getMessage() + "\n\n"
                                + "Please try the Download button instead.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            };

            worker.execute();
            loadingDialog.setVisible(true);

        } catch (Exception ex) {
            System.err.println("❌ Error in viewReport: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error viewing report:\n" + ex.getMessage() + "\n\n"
                    + "Please use the Download button instead.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
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
