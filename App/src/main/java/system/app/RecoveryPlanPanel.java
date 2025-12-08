package system.app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class RecoveryPlanPanel extends JPanel {

    private JComboBox<String> cbStudents;
    private DefaultTableModel failedModel;
    private JTable failedTable;

    private DefaultTableModel milestoneModel;
    private JTable milestoneTable;

    private JTextArea txtDescription;
    private JButton btnEditMilestone;

    public RecoveryPlanPanel() {
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

        JLabel title = new JLabel("Create Course Recovery Plan");
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

        // Left: Student selector + Failed courses (40% width)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.40;
        gbc.weighty = 1.0;
        mainPanel.add(createLeftPanel(), gbc);

        // Right: Plan details (60% width)
        gbc.gridx = 1;
        gbc.weightx = 0.60;
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

        // Failed courses table section
        JPanel tablePanel = new JPanel(new BorderLayout(0, 10));
        tablePanel.setBackground(Color.WHITE);

        JLabel tableLabel = new JLabel("Failed Courses");
        tableLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        tableLabel.setForeground(new Color(33, 33, 33));

        failedModel = new DefaultTableModel(
            new Object[]{"Course ID", "Course Name", "Component", "Component GP", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        failedTable = new JTable(failedModel);
        failedTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        failedTable.setRowHeight(40);
        failedTable.setShowGrid(true);
        failedTable.setGridColor(new Color(200, 200, 200));
        failedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        failedTable.setSelectionBackground(new Color(173, 216, 230));
        failedTable.setSelectionForeground(Color.BLACK);
        
        failedTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            JTextArea textArea = new JTextArea();
            textArea.setText(value != null ? value.toString() : "");
            textArea.setWrapStyleWord(true);
            textArea.setLineWrap(true);
            textArea.setOpaque(true);
            textArea.setFont(table.getFont());
            textArea.setBorder(getBorder());

            if (isSelected) {
                textArea.setBackground(table.getSelectionBackground());
                textArea.setForeground(table.getSelectionForeground());
            } else {
                textArea.setBackground(table.getBackground());
                textArea.setForeground(table.getForeground());
            }

            // Auto-resize row height based on text content
            int lineCount = textArea.getLineCount();
            int preferredHeight = textArea.getPreferredSize().height;

            if (table.getRowHeight(row) < preferredHeight) {
                table.setRowHeight(row, preferredHeight);
            }

            return textArea;
        }
    });


        // Table header styling
        JTableHeader header = failedTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(Color.WHITE);
        header.setForeground(new Color(33, 33, 33));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 200, 200)));

        // Center align cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        failedTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Course ID
        failedTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Component
        failedTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Component GP
        failedTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Status

        JScrollPane scrollPane = new JScrollPane(failedTable); 
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(tableLabel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        leftPanel.add(tablePanel, BorderLayout.CENTER);

        return leftPanel;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout(0, 20));
        rightPanel.setBackground(Color.WHITE);

        // Plan description section
        JPanel descPanel = new JPanel(new BorderLayout(0, 10));
        descPanel.setBackground(Color.WHITE);

        JLabel descLabel = new JLabel("Plan Description");
        descLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        descLabel.setForeground(new Color(33, 33, 33));

        txtDescription = new JTextArea("Enter a general description for the recovery plan here (e.g., reason for failure, resit strategy, required effort).");
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        txtDescription.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDescription.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JScrollPane descScroll = new JScrollPane(txtDescription);
        descScroll.setPreferredSize(new Dimension(0, 120));
        descScroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        descPanel.add(descLabel, BorderLayout.NORTH);
        descPanel.add(descScroll, BorderLayout.CENTER);

        rightPanel.add(descPanel, BorderLayout.NORTH);

        // Milestones section
        JPanel milestonePanel = new JPanel(new BorderLayout(0, 10));
        milestonePanel.setBackground(Color.WHITE);

        // Milestone header with edit button
        JPanel milestoneHeader = new JPanel(new BorderLayout());
        milestoneHeader.setBackground(Color.WHITE);

        JLabel milestoneLabel = new JLabel("Recovery Milestones");
        milestoneLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        milestoneLabel.setForeground(new Color(33, 33, 33));

        btnEditMilestone = createStyledButton("Edit Milestones", new Color(0, 123, 255));
        btnEditMilestone.addActionListener(e -> editMilestones());

        milestoneHeader.add(milestoneLabel, BorderLayout.WEST);
        milestoneHeader.add(btnEditMilestone, BorderLayout.EAST);

        milestoneModel = new DefaultTableModel(
            new Object[]{"Task ID", "Study Week", "Task Description"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Make table non-editable
            }
        };

        milestoneTable = new JTable(milestoneModel);
        milestoneTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        milestoneTable.setRowHeight(40);
        milestoneTable.setShowGrid(true);
        milestoneTable.setGridColor(new Color(200, 200, 200));
        milestoneTable.setSelectionBackground(new Color(173, 216, 230));
        milestoneTable.setSelectionForeground(Color.BLACK);

        // Table header styling
        JTableHeader milestoneHeader2 = milestoneTable.getTableHeader();
        milestoneHeader2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        milestoneHeader2.setBackground(Color.WHITE);
        milestoneHeader2.setForeground(new Color(33, 33, 33));
        milestoneHeader2.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 200, 200)));

        JScrollPane milestoneScroll = new JScrollPane(milestoneTable);
        milestoneScroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        milestoneScroll.getViewport().setBackground(Color.WHITE);

        milestonePanel.add(milestoneHeader, BorderLayout.NORTH);
        milestonePanel.add(milestoneScroll, BorderLayout.CENTER);

        rightPanel.add(milestonePanel, BorderLayout.CENTER);

        // Bottom buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnSave = createStyledButton("Save & Send Recovery Plan", new Color(46, 204, 113));
        btnSave.setPreferredSize(new Dimension(240, 40));
        btnSave.addActionListener(e -> savePlan());

        buttonPanel.add(btnSave);

        rightPanel.add(buttonPanel, BorderLayout.SOUTH);

        return rightPanel;
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(150, 35));
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

    private void loadData() {
        // Use DataRepository directly
        cbStudents.removeAllItems();
        for (Student s : DataRepository.studentList) {
            cbStudents.addItem(s.getStudentName() + " - " + s.getStudentID());
        }

        // Add action listener to load failed courses
        cbStudents.addActionListener(e -> loadFailedCourses());
        if (cbStudents.getItemCount() > 0) {
            cbStudents.setSelectedIndex(0);
        }
    }

    private void loadFailedCourses() {
        String selectedStudentText = (String) cbStudents.getSelectedItem();
        failedModel.setRowCount(0);

        if (selectedStudentText == null) return;

        // Extract student ID from the combined string
        String studentID = selectedStudentText.substring(selectedStudentText.lastIndexOf(" - ") + 3);

        // Use DataRepository to find student
        Student student = DataRepository.findStudentByID(studentID);
        if (student == null) return;

        // NEW: Get enrollments and iterate through them to find failed components
        List<CourseEnrollment> enrollments = student.getEnrollment();

        for (CourseEnrollment enrollment : enrollments) {
            // Only process failed enrollments
            if (!enrollment.getStatus().equals("Failed")) continue;

            String courseID = enrollment.getCourseID();
            Course course = DataRepository.findCourseByID(courseID);
            String courseName = course != null ? course.getCourseName() : "Unknown";

            List<String> failedComponents = enrollment.getFailedComponent();

            // Add a row for each failed component
            for (String component : failedComponents) {
                double componentGP = 0.0;

                // Get the grade point for this specific component
                if (component.equalsIgnoreCase("Assignment")) {
                    componentGP = enrollment.getAssignmentGradePoint();
                } else if (component.equalsIgnoreCase("Midterms") || component.equalsIgnoreCase("Midterm")) {
                    componentGP = enrollment.getMidtermTestGradePoint();
                } else if (component.equalsIgnoreCase("Finals") || component.equalsIgnoreCase("Final")) {
                    componentGP = enrollment.getFinalTestGradePoint();
                }

                failedModel.addRow(new Object[]{
                    courseID,
                    courseName,
                    component,
                    String.format("%.1f", componentGP),
                    "Failed"
                });
            }
        }

        // Enable/disable edit button
        btnEditMilestone.setEnabled(failedModel.getRowCount() > 0);
    }
    
    private void editMilestones() {
        if (failedModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, 
                "Select a student and failed course first.", 
                "Warning", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Convert current table data to a list of Milestone objects
        List<Milestone> initialMilestones = new ArrayList<>();
        for (int r = 0; r < milestoneModel.getRowCount(); r++) {
            String id = (String) milestoneModel.getValueAt(r, 0);
            String week = (String) milestoneModel.getValueAt(r, 1);
            String task = (String) milestoneModel.getValueAt(r, 2);
            initialMilestones.add(new Milestone(id, week, task, false));
        }

        MilestoneEditorDialog dialog = new MilestoneEditorDialog(
            (Frame) SwingUtilities.getWindowAncestor(this), 
            initialMilestones
        );
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            milestoneModel.setRowCount(0);
            for (Milestone m : dialog.getMilestones()) {
                milestoneModel.addRow(new Object[]{
                    m.getMilestoneID(), 
                    m.getWeekRange(), 
                    m.getTaskDescription()
                });
            }
        }
    }

    private List<String[]> getMilestoneData() {
        List<String[]> list = new ArrayList<>();
        for (int r = 0; r < milestoneModel.getRowCount(); r++) {
            String id = (String) milestoneModel.getValueAt(r, 0);
            String week = (String) milestoneModel.getValueAt(r, 1);
            String task = (String) milestoneModel.getValueAt(r, 2);
            list.add(new String[]{id, week, task});
        }
        return list;
    }

    private void savePlan() {
        String selectedStudentText = (String) cbStudents.getSelectedItem();
        if (selectedStudentText == null || failedModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, 
                "Please select a student and ensure they have a failed course.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Extract student ID
        String studentID = selectedStudentText.substring(selectedStudentText.lastIndexOf(" - ") + 3);
        
        // Get student from DataRepository
        Student student = DataRepository.findStudentByID(studentID);
        if (student == null) {
            JOptionPane.showMessageDialog(this, 
                "Student not found!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        List<String[]> milestoneList = getMilestoneData();

        if (milestoneList.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Milestones list cannot be empty. Please edit the milestones.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Get course data from table
        String courseID = (String) failedModel.getValueAt(0, 0);
        String courseName = (String) failedModel.getValueAt(0, 1);

        try {
            
            // Send email with PDF attachment
            RecoveryPlanEmailer.sendPlanToStudent(
                student.getStudentID(),
                courseID,
                courseName,
                txtDescription.getText(),
                milestoneList,
                (Frame) SwingUtilities.getWindowAncestor(this)
            );

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error saving recovery plan: " + ex.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}