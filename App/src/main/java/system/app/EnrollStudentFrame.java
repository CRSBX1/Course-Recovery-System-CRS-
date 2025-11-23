/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.app;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.IOException;

/**
 *
 * @author Lenovo
 */
public class EnrollStudentFrame{
    private JFrame frame;
    private JTable studentTable;
    private StudentTableModel model;
    private TitlePanel title_panel;
    private NavigationPanel naviPanel;
    private StudentDetailsPanel studentDetailsPanel;
    private JScrollPane scrollPane;
    private JButton eligibleBtn;
    private JLabel tableTitle;
    private JLabel studentDetailsTitle;
    private EnrollStudentDialog dialog;
    private EligibilityChecker check;
    private Student s;
    private Font titleFont;
    
    public EnrollStudentFrame(){
        frame = new JFrame("Check Eligibility and Enroll Student");
        title_panel = new TitlePanel();
        naviPanel = new NavigationPanel();
        studentDetailsPanel = new StudentDetailsPanel();
        eligibleBtn = new JButton("Check Eligibility");
        model = new StudentTableModel();
        studentTable = new JTable(model.getTableModel()); //create a table with student table model as source
        scrollPane = new JScrollPane(studentTable); //wrap table with scrollpane
        tableTitle = new JLabel("List of Unenrolled Students");
        studentDetailsTitle = new JLabel("Student Details");
        check = new EligibilityChecker();
        titleFont = new Font("Arial",Font.BOLD,16);
        
        //Frame configurations
        frame.setSize(1200, 800);
        frame.setVisible(true);
        frame.setLayout(null);
        
        //Title panel configurations
        title_panel.setBounds(0, 0, 1200, 80);
        
        //Navigation panel configurations
        naviPanel.setBounds(0,80,275,720);
        
        //Student details panel configurations
        studentDetailsPanel.setBounds(800,175,350,450);
        
        //Table Configurations
        studentTable.setRowHeight(50);
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()){
                int viewRow = studentTable.getSelectedRow();
                if(viewRow>=0){
                    int modelRow = studentTable.convertRowIndexToModel(viewRow);
                    String sID = (String) model.getTableModel().getValueAt(modelRow,0);
                    s = DataRepository.findStudentByID(sID);
                    ArrayList<String> courseNames = new ArrayList<>();
                    studentDetailsPanel.setID(s.getStudentID());
                    studentDetailsPanel.setName(s.getStudentName());
                    studentDetailsPanel.setSemester(String.valueOf(s.getSemester()));
                    studentDetailsPanel.setMajor(s.getStudentProgram());
                    studentDetailsPanel.setCGPA(String.format("%.2f", s.getCGPA()));
                    for(Course c: s.getFailedCourses()){
                        courseNames.add(c.getName());
                    }
                    if(!courseNames.isEmpty()){
                        studentDetailsPanel.setFailedCourses("<html>" + String.join(", ",courseNames) + "</html>");
                    } else {
                        studentDetailsPanel.setFailedCourses("None");
                    }
                }
            }
        });
        scrollPane.setBounds(400, 150, 300, 550);
        
        //Table title configurations
        tableTitle.setBounds(410, 110, 300, 25);
        tableTitle.setFont(titleFont);
        
        //Student details panel title configuration
        studentDetailsTitle.setBounds(810, 130, 300, 25);
        studentDetailsTitle.setFont(titleFont);
       
        //Check Eligibility btn configurations
        eligibleBtn.setBounds(875, 650, 200, 30);
        eligibleBtn.addActionListener((ActionEvent e) -> {
            if(s == null){ //No student is chosen
                dialog = new EnrollStudentDialog(frame, "You need to choose a student first","Ok");
            }
            else if(check.checkProgressionEligibility(s)){
                try {
                    dialog = new EnrollStudentDialog(frame, s.getStudentName() + " is eligible for enrollment.","Enroll");
                    s.setEnrollStatus("Enrolled");
                    FileUtils.writeToFile("Data/Students.txt", FileUtils::writeStudent);
                    model.refreshTableContents();
                } catch (IOException ex) {
                   System.out.println("Error when enrolling student: " + ex.getMessage());
                }
            } else{
                dialog = new EnrollStudentDialog(frame,s.getStudentName() + " isn't eligible for enrollment yet","Ok");
            }
            //call eligibilty function, display dialog according to result
        });
        
        frame.add(title_panel);
        frame.add(naviPanel);
        frame.add(studentDetailsPanel);
        frame.add(eligibleBtn);
        frame.add(scrollPane);
        frame.add(tableTitle);
        frame.add(studentDetailsTitle);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
}
