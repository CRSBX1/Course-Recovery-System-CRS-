/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.app;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Lenovo
 */
public class StudentDetailsPanel extends JPanel{
    private JLabel idField;
    private JLabel nameField;
    private JLabel semesterField;
    private JLabel majorField;
    private JLabel cgpaField;
    private JLabel failedCoursesField;
    private JLabel id;
    private JLabel name;
    private JLabel semester;
    private JLabel major;
    private JLabel cgpa;
    private JLabel failedCourses;
    
    public StudentDetailsPanel(){
        setLayout(null);
        setBackground(new Color(0x7DC3E3));
        setBounds(600,175,300,300);
        
        // Field Labels (bold)
        Font fieldFont = new Font("Arial", Font.BOLD, 15);

        // Value Labels (plain)
        Font valueFont = new Font("Arial", Font.PLAIN, 15);

        // StudentID label
        idField = new JLabel("Student ID:");
        idField.setBounds(30, 40, 120, 20);
        idField.setFont(fieldFont);

        // StudentName label
        nameField = new JLabel("Name:");
        nameField.setBounds(30, 80, 120, 20);
        nameField.setFont(fieldFont);

        // Semester label
        semesterField = new JLabel("Semester:");
        semesterField.setBounds(30, 120, 120, 20);
        semesterField.setFont(fieldFont);

        // Major label
        majorField = new JLabel("Major:");
        majorField.setBounds(30, 160, 120, 20);
        majorField.setFont(fieldFont);

        // CGPA label
        cgpaField = new JLabel("CGPA:");
        cgpaField.setBounds(30, 200, 120, 20);
        cgpaField.setFont(fieldFont);

        // Failed Courses label
        failedCoursesField = new JLabel("Failed Courses:");
        failedCoursesField.setBounds(30, 240, 150, 20);
        failedCoursesField.setFont(fieldFont);

        // VALUE LABELS (right side)
        id = new JLabel();
        id.setBounds(140, 40, 150, 20);
        id.setFont(valueFont);

        name = new JLabel();
        name.setBounds(140, 80,150, 20);
        name.setFont(valueFont);

        semester = new JLabel();
        semester.setBounds(140, 120, 150, 20);
        semester.setFont(valueFont);

        major = new JLabel();
        major.setBounds(140, 160, 170, 20);
        major.setFont(valueFont);

        cgpa = new JLabel();
        cgpa.setBounds(140, 200, 150, 20);
        cgpa.setFont(valueFont);

        failedCourses = new JLabel();
        failedCourses.setBounds(30, 240, 300, 130);
        failedCourses.setFont(valueFont);
        
        add(idField);
        add(nameField);
        add(semesterField);
        add(majorField);
        add(cgpaField);
        add(failedCoursesField);
        add(id);
        add(name);
        add(semester);
        add(major);
        add(cgpa);
        add(failedCourses);
    }
    
    public void setID(String id){
        this.id.setText(id);
    }
    
    public void setName(String name){
        this.name.setText(name);
    }
    
    public void setSemester(String sem){
        this.semester.setText(sem);
    }
    
    public void setMajor(String major){
        this.major.setText(major);
    }
    
    public void setCGPA(String cgpa){
        this.cgpa.setText(cgpa);
    }
    
    public void setFailedCourses(String fcourse){
        failedCourses.setText(fcourse);
    }
}
