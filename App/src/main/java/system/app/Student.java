/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.app;
import java.util.ArrayList;

/**
 *
 * @author Lenovo
 */
public class Student {
    private String studentID;
    private String fullName;
    private String email;
    private String program;
    private int year;
    private int semester;
    private double cgpa;
    private int totalCreditHours;
    private double totalGradePoints;
    private ArrayList<Course> failedCourses;
    private Course course;
    
    public Student(String id, String name, String email, String program, int year, int semester){
        studentID = id;
        fullName = name;
        this.email = email;
        this.program = program;
        this.year = year;
        this.semester = semester;
    }
    
    public double calculateCGPA(){
        cgpa = totalGradePoints/totalCreditHours;
        return cgpa;
    }
    
    public double getFailedCoursesCount() {
        return failedCourses.size();
    }
    
    public void addFailedCourse(Course course) {
        //Counts as extra feature, let's figure out the essentials first
    }
    
    public String getName(){
        return fullName;
    }
    
    public double getTotalGradePoints() {
        return totalGradePoints;
    }
    
    public int getTotalCreditHours() {
        return totalCreditHours;
    }
    
    public void updateAcademicInfo() {
        //I don't know what this method is for ngl. Kind of a scope creep maybe?
    }
    
    
}
