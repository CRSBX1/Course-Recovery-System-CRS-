/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.app;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.IOException;
import java.util.*;

public class TrackSetStudentProgressPanel extends JPanel {
    private JComboBox<String> cbStudents;
    private JLabel overallGradeLabel;
    private JLabel overallGPLabel;
    private DefaultTableModel enrollmentModel;
    private JTable enrollmentTable;
    private Student selectedStudent;
    private CourseEnrollment selectedEnrollment;
    
    // Student details and grade point spinners
    private JLabel studentIDLabel;
    private JLabel studentNameLabel;
    private JLabel courseIDLabel;
    private JLabel courseNameLabel;
    private JSpinner assignmentSpinner;
    private JSpinner midtermSpinner;
    private JSpinner finalSpinner;
    private JSpinner attemptSpinner;
    private JButton updateButton;
    
    public TrackSetStudentProgressPanel() {
        setLayout(new BorderLayout(30, 30));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 50, 40, 50));
        
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        // Title at top
        add(createTitlePanel(), BorderLayout.NORTH);
        
        // Main content
        add(createMainContentPanel(), BorderLayout.CENTER);
    }
    
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Track & Set Student Progress");
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
        
        // Left: Student selector + Enrollment table (50% width)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.50;
        gbc.weighty = 1.0;
        mainPanel.add(createLeftPanel(), gbc);
        
        // Right: Grade point editor (50% width)
        gbc.gridx = 1;
        gbc.weightx = 0.50;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(createRightPanel(), gbc);
        
        return mainPanel;
    }
    
    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout(0, 20));
        leftPanel.setBackground(Color.WHITE);
        
        // Student selector section
        JPanel selectorPanel = new JPanel(new BorderLayout(0, 10));
        selectorPanel.setBackground(Color.WHITE);
        
        JLabel selectorLabel = new JLabel("Select Student");
        selectorLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        selectorLabel.setForeground(new Color(33, 33, 33));
        
        cbStudents = new JComboBox<>();
        cbStudents.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbStudents.setPreferredSize(new Dimension(0, 40));
        cbStudents.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        selectorPanel.add(selectorLabel, BorderLayout.NORTH);
        selectorPanel.add(cbStudents, BorderLayout.CENTER);
        
        leftPanel.add(selectorPanel, BorderLayout.NORTH);
        
        // Overall grade display section
        JPanel gradeDisplayPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        gradeDisplayPanel.setBackground(Color.WHITE);
        gradeDisplayPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Overall Grade
        JPanel gradePanel = new JPanel(new BorderLayout());
        gradePanel.setBackground(new Color(173, 216, 230));
        gradePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel gradeTitle = new JLabel("Overall Grade");
        gradeTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gradeTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        overallGradeLabel = new JLabel("-");
        overallGradeLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        overallGradeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        gradePanel.add(gradeTitle, BorderLayout.NORTH);
        gradePanel.add(overallGradeLabel, BorderLayout.CENTER);
        
        // Overall Grade Point
        JPanel gpPanel = new JPanel(new BorderLayout());
        gpPanel.setBackground(new Color(173, 216, 230));
        gpPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel gpTitle = new JLabel("CGPA");
        gpTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gpTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        overallGPLabel = new JLabel("-");
        overallGPLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        overallGPLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        gpPanel.add(gpTitle, BorderLayout.NORTH);
        gpPanel.add(overallGPLabel, BorderLayout.CENTER);
        
        gradeDisplayPanel.add(gradePanel);
        gradeDisplayPanel.add(gpPanel);
        
        leftPanel.add(gradeDisplayPanel, BorderLayout.CENTER);
        
        // Enrollment table section
        JPanel tablePanel = new JPanel(new BorderLayout(0, 10));
        tablePanel.setBackground(Color.WHITE);
        
        JLabel tableLabel = new JLabel("Course Enrollments");
        tableLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        tableLabel.setForeground(new Color(33, 33, 33));
        
        enrollmentModel = new DefaultTableModel(
            new Object[]{"Course ID", "Course Name", "Assignment", "Midterm", "Final", "Attempt", "Grade"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        enrollmentTable = new JTable(enrollmentModel);
        enrollmentTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        enrollmentTable.setRowHeight(40);
        enrollmentTable.setShowGrid(true);
        enrollmentTable.setGridColor(new Color(200, 200, 200));
        enrollmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        enrollmentTable.setSelectionBackground(new Color(173, 216, 230));
        enrollmentTable.setSelectionForeground(Color.BLACK);
        
        // Table header styling
        JTableHeader header = enrollmentTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(Color.WHITE);
        header.setForeground(new Color(33, 33, 33));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 200, 200)));
        
        enrollmentTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {

                JTextArea textArea = new JTextArea();
                textArea.setText(value != null ? value.toString() : "");
                textArea.setWrapStyleWord(true);
                textArea.setLineWrap(true);
                textArea.setOpaque(true);
                textArea.setFont(table.getFont());

                if (isSelected) {
                    textArea.setBackground(table.getSelectionBackground());
                    textArea.setForeground(table.getSelectionForeground());
                } else {
                    textArea.setBackground(table.getBackground());
                    textArea.setForeground(table.getForeground());
                }

                // Auto-resize row height based on text content
                int preferredHeight = textArea.getPreferredSize().height;
                if (table.getRowHeight(row) < preferredHeight) {
                    table.setRowHeight(row, preferredHeight);
                }

                return textArea;
            }
        });
        
        // Center align numeric columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        enrollmentTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        enrollmentTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        enrollmentTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        enrollmentTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        enrollmentTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        enrollmentTable.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        
        // Selection listener
        enrollmentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = enrollmentTable.getSelectedRow();
                if (selectedRow >= 0) {
                    loadEnrollmentForEditing(selectedRow);
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(enrollmentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        tablePanel.add(tableLabel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        leftPanel.add(tablePanel, BorderLayout.SOUTH);
        
        return leftPanel;
    }
    
    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout(0, 20));
        rightPanel.setBackground(Color.WHITE);

        // Section title
        JLabel sectionTitle = new JLabel("Edit Component Grades");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        sectionTitle.setForeground(new Color(33, 33, 33));
        rightPanel.add(sectionTitle, BorderLayout.NORTH);

        // Details card - SINGLE PANEL
        JPanel detailsCard = new JPanel();
        detailsCard.setLayout(new BoxLayout(detailsCard, BoxLayout.Y_AXIS));
        detailsCard.setBackground(new Color(240, 248, 255));
        detailsCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 2),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        JPanel studentDetailsPanel = new JPanel(new GridLayout(4, 1, 5, 8));
        studentDetailsPanel.setOpaque(false);
        studentDetailsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0)); // No extra left padding
        studentDetailsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        studentDetailsPanel.setMaximumSize(new Dimension(450, 120)); // Constrain width

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        studentIDLabel = new JLabel("Student ID: -");
        studentNameLabel = new JLabel("Student Name: -");
        courseIDLabel = new JLabel("Course ID: -");
        courseNameLabel = new JLabel("Course Name: -");

        studentIDLabel.setFont(labelFont);
        studentNameLabel.setFont(labelFont);
        courseIDLabel.setFont(labelFont);
        courseNameLabel.setFont(labelFont);

        studentDetailsPanel.add(studentIDLabel);
        studentDetailsPanel.add(studentNameLabel);
        studentDetailsPanel.add(courseIDLabel);
        studentDetailsPanel.add(courseNameLabel);

        detailsCard.add(studentDetailsPanel);
        detailsCard.add(Box.createVerticalStrut(10));

        // Grade point spinners
        JLabel spinnerTitle = new JLabel("Component Grade Points (0-100)");
        spinnerTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        spinnerTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsCard.add(spinnerTitle);
        detailsCard.add(Box.createVerticalStrut(15));

        // Assignment spinner
        JPanel assignmentRow = createSpinnerRow("Assignment:", assignmentSpinner = createGradeSpinner());
        assignmentRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsCard.add(assignmentRow);
        detailsCard.add(Box.createVerticalStrut(12));

        // Midterm spinner
        JPanel midtermRow = createSpinnerRow("Midterm:", midtermSpinner = createGradeSpinner());
        midtermRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsCard.add(midtermRow);
        detailsCard.add(Box.createVerticalStrut(12));

        // Final spinner
        JPanel finalRow = createSpinnerRow("Final:", finalSpinner = createGradeSpinner());
        finalRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsCard.add(finalRow);
        detailsCard.add(Box.createVerticalStrut(12));

        // Attempt spinner
        JPanel attemptRow = createAttemptSpinnerRow("Attempt:", attemptSpinner = createAttemptSpinner());
        attemptRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsCard.add(attemptRow);
        detailsCard.add(Box.createVerticalStrut(25));

        // Update button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        updateButton = createStyledButton("Update Grades", new Color(46, 204, 113));
        updateButton.setPreferredSize(new Dimension(200, 45));
        updateButton.setEnabled(false);
        updateButton.addActionListener(e -> updateGrades());

        buttonPanel.add(updateButton);
        detailsCard.add(buttonPanel);

        rightPanel.add(detailsCard, BorderLayout.CENTER);

        return rightPanel;
    }

    private JSpinner createAttemptSpinner() {
        SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 3, 1); 
        JSpinner spinner = new JSpinner(model);
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setHorizontalAlignment(JTextField.CENTER);
        spinner.setPreferredSize(new Dimension(100, 35));
        return spinner;
    }

    private JPanel createAttemptSpinnerRow(String labelText, JSpinner spinner) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setPreferredSize(new Dimension(120, 35));

        row.add(label);
        row.add(spinner);

        return row;
    }
    
    private JSpinner createGradeSpinner() {
        SpinnerNumberModel model = new SpinnerNumberModel(0.0, 0.0, 100.0, 0.5);
        JSpinner spinner = new JSpinner(model);
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setHorizontalAlignment(JTextField.CENTER);
        spinner.setPreferredSize(new Dimension(100, 35));
        return spinner;
    }
    
    private JPanel createSpinnerRow(String labelText, JSpinner spinner) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setOpaque(false);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setPreferredSize(new Dimension(120, 35));
        
        row.add(label);
        row.add(spinner);
        
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
    
    private void loadData() {
        // Populate student ComboBox
        cbStudents.removeAllItems();
        for (Student s : DataRepository.studentList) {
            cbStudents.addItem(s.getStudentName() + " - " + s.getStudentID());
        }
        
        // Add action listener
        cbStudents.addActionListener(e -> loadStudentEnrollments());
        if (cbStudents.getItemCount() > 0) {
            cbStudents.setSelectedIndex(0);
        }
    }
    
    private void loadStudentEnrollments() {
        String selectedStudentText = (String) cbStudents.getSelectedItem();
        enrollmentModel.setRowCount(0);
        
        if (selectedStudentText == null) return;
        
        // Extract student ID
        String studentID = selectedStudentText.substring(selectedStudentText.lastIndexOf(" - ") + 3);
        
        // Get student
        selectedStudent = DataRepository.findStudentByID(studentID);
        if (selectedStudent == null) return;
        
        // Update overall grade display
        overallGradeLabel.setText(getOverallGrade(selectedStudent.getCGPA()));
        overallGPLabel.setText(String.format("%.2f", selectedStudent.getCGPA()));
        
        // Load enrollments
        java.util.List<CourseEnrollment> enrollments = selectedStudent.getEnrollment();
        
        for (CourseEnrollment enrollment : enrollments) {
            Course course = DataRepository.findCourseByID(enrollment.getCourseID());
            String courseName = course != null ? course.getCourseName() : "Unknown";
            
            enrollmentModel.addRow(new Object[]{
                enrollment.getCourseID(),
                courseName,
                String.format("%.1f", enrollment.getAssignmentGradePoint()),
                String.format("%.1f", enrollment.getMidtermTestGradePoint()),
                String.format("%.1f", enrollment.getFinalTestGradePoint()),
                enrollment.getAttempts(),
                enrollment.getOverallGrade()
            });
        }
        
        // Clear selection
        clearEditor();
    }
    
    private void loadEnrollmentForEditing(int row) {
        if (selectedStudent == null) return;
        
        java.util.List<CourseEnrollment> enrollments = selectedStudent.getEnrollment();
        if (row >= enrollments.size()) return;
        
        selectedEnrollment = enrollments.get(row);
        
        // Update labels
        studentIDLabel.setText("Student ID: " + selectedStudent.getStudentID());
        studentNameLabel.setText("Student Name: " + selectedStudent.getStudentName());
        courseIDLabel.setText("Course ID: " + selectedEnrollment.getCourseID());
        
        Course course = DataRepository.findCourseByID(selectedEnrollment.getCourseID());
        courseNameLabel.setText("Course Name: " + (course != null ? course.getCourseName() : "Unknown"));
        
        // Set spinner values
        assignmentSpinner.setValue(selectedEnrollment.getAssignmentGradePoint());
        midtermSpinner.setValue(selectedEnrollment.getMidtermTestGradePoint());
        finalSpinner.setValue(selectedEnrollment.getFinalTestGradePoint());
        attemptSpinner.setValue(selectedEnrollment.getAttempts());
        
        // Enable update button
        updateButton.setEnabled(true);
    }
    
    private void updateGrades() {
        ArrayList<String> updatedComponents = new ArrayList<>();
        if (selectedStudent == null || selectedEnrollment == null) {
            JOptionPane.showMessageDialog(this,
                "Please select a student and course enrollment first.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Get new values
            double newAssignment = (Double) assignmentSpinner.getValue();
            double newMidterm = (Double) midtermSpinner.getValue();
            double newFinal = (Double) finalSpinner.getValue();
            int newAttempt = (int) attemptSpinner.getValue();
            
            //Failed components update
            if(newAssignment < 50){
                updatedComponents.add("Assignment");
            }
            if(newMidterm < 50){
                updatedComponents.add("Midterms");
            }
            if(newFinal < 50){
                updatedComponents.add("Finals");
            }
            
            // Update the enrollment
            selectedEnrollment.setAssignmentGradePoint(newAssignment);
            selectedEnrollment.setMidtermTestGradePoint(newMidterm);
            selectedEnrollment.setFinalTestGradePoint(newFinal);
            selectedEnrollment.setAttempt(newAttempt);
            selectedEnrollment.setOverallGradePoint();
            selectedEnrollment.setOverallGrade();
            selectedEnrollment.setFailedComponents(updatedComponents);
            if(updatedComponents.isEmpty()){
                selectedEnrollment.setStatus("Passed");
            } else{
                selectedEnrollment.setStatus("Failed");
            }
            
            // Recalculate student's overall CGPA and failed courses
            DataRepository.linkAll();
            
            // Save to file
            FileUtils.writeToFile("Data/Course Enrollment.txt", FileUtils::writeEnroll);
            
            // Refresh display
            loadStudentEnrollments();
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Error updating grades: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearEditor() {
        selectedEnrollment = null;
        studentIDLabel.setText("Student ID: -");
        studentNameLabel.setText("Student Name: -");
        courseIDLabel.setText("Course ID: -");
        courseNameLabel.setText("Course Name: -");
        assignmentSpinner.setValue(0.0);
        midtermSpinner.setValue(0.0);
        finalSpinner.setValue(0.0);
        attemptSpinner.setValue(1);
        updateButton.setEnabled(false);
    }
    
    private String getOverallGrade(double cgpa) {
        if (cgpa >= 3.7) return "A";
        if (cgpa >= 3.3) return "A-";
        if (cgpa >= 3.0) return "B+";
        if (cgpa >= 2.7) return "B";
        if (cgpa >= 2.3) return "B-";
        if (cgpa >= 2.0) return "C+";
        if (cgpa >= 1.7) return "C";
        if (cgpa >= 1.3) return "C-";
        if (cgpa >= 1.0) return "D+";
        return "D";
    }
}
