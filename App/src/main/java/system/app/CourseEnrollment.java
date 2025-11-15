/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.app;
import java.time.*;
import java.util.*;

/**
 *
 * @author Lenovo
 */
public class CourseEnrollment {
    private String enrollmentID;
    private String studentID;
    private String courseID;
    private LocalDate enrollmentDate;
    private String status;
    private String grade;
    private double gradePoint;
    private int attemptNumber;
    private ArrayList<String> failedComponents = new ArrayList<>();
 //enrollmentID,studentID,courseID,enrollmentDate,status,grade,gradePoint,attemptNumber,failedcomponents

    
    public CourseEnrollment(String id, String student, String course, LocalDate date, String status, String grade, double point, int attempt, ArrayList<String> failed){
        enrollmentID = id;
        studentID = student;
        courseID = course;
        enrollmentDate = date;
        this.status = status;
        this.grade = grade;
        gradePoint = point;
        attemptNumber = attempt;
        failedComponents = failed;
    }
    
    public void updateStatus(String status){
        this.status = status;
    }
    
    public void updateGrade(String grade, double gradePoint){
        this.grade = grade;
        this.gradePoint = gradePoint;
    }
    
    public void incrementAttempt(){
       attemptNumber += 1; 
    }
    
    public boolean isMaxAttemptsReached(){
        return attemptNumber == 3;
    }
    
    public void addFailedComponent(String component){
        failedComponents.add(component);
    }
    
    public boolean requiresFullRetake(){
        return failedComponents.size() == 3 || attemptNumber >= 2;    
        //3 components = assignment, midterms, and finals
        //If failed all components, require full course retake
    }

    public String getStudentID() {
        return studentID;
    }
    
    public String getCourseID() {
        return courseID;
    }
    
    public List<String> getFailedComponent(String component){
        return failedComponents;
    }
    
    public String getStatus(){
        return status;
    }
    
    public double getGradePoint(){
        return gradePoint;
    }
}
