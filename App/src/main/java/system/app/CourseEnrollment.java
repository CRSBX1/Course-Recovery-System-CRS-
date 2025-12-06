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
    private String overallGrade;
    private double overallGradePoint;
    private double assignmentGP;
    private double midtermTestGP;
    private double finalTestGP;
    private int attemptNumber;
    private ArrayList<String> failedComponents = new ArrayList<>();
 //enrollmentID,studentID,courseID,enrollmentDate,status,grade,gradePoint,attemptNumber,failedcomponents

    
    public CourseEnrollment(String id, String student, String course, LocalDate date, String status, double asg_score, double mid_score, double final_score, int attempt, ArrayList<String> failed){
        enrollmentID = id;
        studentID = student;
        courseID = course;
        enrollmentDate = date;
        this.status = status;
        assignmentGP = asg_score;
        midtermTestGP = mid_score;
        finalTestGP = final_score;
        overallGradePoint = (asg_score + mid_score + final_score)/3;
        attemptNumber = attempt;
        failedComponents = failed;
        setOverallGrade(); //Sets grade based on overall course grade point
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
    
    public void setStatus(String status){
        this.status = status;
    }
    
    public void setAssignmentGradePoint(double score){
        assignmentGP = score;
    }
    
    public void setMidtermTestGradePoint(double score){
        midtermTestGP = score;
    }
    
    public void setFinalTestGradePoint(double score){
        finalTestGP = score;
    }
    
    public void setOverallGradePoint(){
        overallGradePoint = (assignmentGP + midtermTestGP + finalTestGP)/3;
    }
    
    public final void setOverallGrade(){
        if(overallGradePoint == 4.0){
            overallGrade = "A+";
        }
        else if(overallGradePoint>=3.7){
            overallGrade = "A";
        }
        else if(overallGradePoint>=3.3){
            overallGrade = "A-";
        }
        else if(overallGradePoint>=3){
            overallGrade = "B+";
        }
        else if(overallGradePoint>=2.7){
            overallGrade = "B";
        }
        else if(overallGradePoint>=2.3){
            overallGrade = "B-";
        }
        else if(overallGradePoint>=2){
            overallGrade = "C+";
        }
        else if(overallGradePoint>=1.7){
            overallGrade = "C";
        }
        else if(overallGradePoint>=1.3){
            overallGrade = "C-";
        }
        else if(overallGradePoint>=1){
            overallGrade = "D+";
        }
        else if(overallGradePoint>0){
            overallGrade = "D";
        }
        else{
            overallGrade = "F";
        }
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
    
    public double getAssignmentGradePoint(){
        return assignmentGP;
    }
    
    public double getMidtermTestGradePoint(){
        return midtermTestGP;
    }
    
    public double getFinalTestGradePoint(){
        return finalTestGP;
    }
    
    public String getOverallGrade(){
        return overallGrade;
    }
    
    public double getOverallGradePoint(){
        return overallGradePoint;
    }
}
