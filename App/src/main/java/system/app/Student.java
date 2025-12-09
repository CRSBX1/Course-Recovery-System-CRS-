/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.app;
import java.util.*;

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
    private List<Course> failedCourses = new ArrayList<>();
    private List<Course> allCourses = new ArrayList<>();
    private List<CourseEnrollment> enrollment = new ArrayList<>();
    private String enrollStatus;
    
    public Student(String id, String name, String email, String program, int year, int semester, String enrollStatus){
        studentID = id;
        fullName = name;
        this.email = email;
        this.program = program;
        this.year = year;
        this.semester = semester;
        this.enrollStatus = enrollStatus;
    }
    
    public void addFailedCourse(Course course) {
        failedCourses.add(course);
    }
    
    public String getStudentID(){
        return studentID;
    }
    
    public String getStudentName(){
        return fullName;
    }
    
    public String getEmail(){
        return email;
    }
    
    public String getStudentProgram(){
        return program;
    }
    
    public int getYear(){
        return year;
    }
    
    public int getSemester(){
        return semester;
    }
    
    public String getEnrollStatus(){
        return enrollStatus;
    }
    
    public List<CourseEnrollment> getAllEnrollments(){
        return enrollment;
    }
    
    public final double getTotalGradePoints() {
        return totalGradePoints;
    }
    
    public final int getTotalCreditHours() {
        return totalCreditHours;
    }
    
    public final double getCGPA(){
        return cgpa;
    }
    
    public List<Course> getFailedCourses() {
        return failedCourses;
    }
    
    public int getFailedCoursesCount() {
        return failedCourses.size();
    }
    public List<CourseEnrollment> getEnrollment() {
        return enrollment;
    }

    public String getProgram() {
        return program;
    }
    
    public void setEnrollStatus(String status){
        enrollStatus = status;
    }
    
    public void setEnrollment(List<CourseEnrollment> list){ //set the enrollment list; not to be confused with enrollment status
        enrollment = list;
    }
    
    public void setFailedCourses(List<Course> course){
        failedCourses = course;
    }
    
    public void setAllCourses(List<Course> course){
        allCourses = course;
    }
    
    public final void setTotalGradePoints(double gp) {
        totalGradePoints = gp;
    }
    
    public final void setTotalCreditHours(int ch) {
        totalCreditHours = ch;
    }
    
    public final void setCGPA(){
        cgpa = totalGradePoints/totalCreditHours;
    }
    
    public void updateAcademicInfo() {
        //I don't know what this method is for ngl. Kind of a scope creep maybe?
    }
    
    
}
