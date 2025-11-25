package system.app;
import java.time.*;
import java.util.*;

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
        setOverallGrade();
    }

    public CourseEnrollment(String id, String student, String course, LocalDate date, String status, String overallGrade, double overallGradePoint, int attempt, ArrayList<String> failed){
        enrollmentID = id;
        studentID = student;
        courseID = course;
        enrollmentDate = date;
        this.status = status;
        this.overallGrade = overallGrade;
        this.overallGradePoint = overallGradePoint;
        this.assignmentGP = 0.0;
        this.midtermTestGP = 0.0;
        this.finalTestGP = 0.0;
        attemptNumber = attempt;
        failedComponents = failed;
        // derive overallGrade string if not provided
        if (this.overallGrade == null || this.overallGrade.isEmpty()) {
            setOverallGrade();
        }
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
        } else if(overallGradePoint>=3.7){
            overallGrade = "A";
        } else if(overallGradePoint>=3.3){
            overallGrade = "A-";
        } else if(overallGradePoint>=3){
            overallGrade = "B+";
        } else if(overallGradePoint>=2.7){
            overallGrade = "B";
        } else if(overallGradePoint>=2.3){
            overallGrade = "B-";
        } else if(overallGradePoint>=2){
            overallGrade = "C+";
        } else if(overallGradePoint>=1.7){
            overallGrade = "C";
        } else if(overallGradePoint>=1.3){
            overallGrade = "C-";
        } else if(overallGradePoint>=1){
            overallGrade = "D+";
        } else if(overallGradePoint>0){
            overallGrade = "D";
        } else {
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

    public double getGradePoint(){
        return overallGradePoint;
    }
}
