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
    
    public Student(String id, String name, String email, String program, int year, int semester){
        studentID = id;
        fullName = name;
        this.email = email;
        this.program = program;
        this.year = year;
        this.semester = semester;
        totalCreditHours = 0;
        totalGradePoints = 0;
        
        enrollment = DataRepository.enrollList.stream()
                .filter(i -> i.getStudentID().equals(studentID)).toList();
        
        for(CourseEnrollment i: enrollment){
            for(Course e: DataRepository.courseList){
                if(i.getCourseID().equals(e.getID()) & !i.getStatus().equals("Failed")){
                    allCourses.add(e);
                }
                else if(i.getCourseID().equals(e.getID()) & i.getStatus().equals("Failed")){
                    failedCourses.add(e);
                }
            }
        }
    }
    
    public void addFailedCourse(Course course) {
        failedCourses.add(course);
    }
    
    public String getName(){
        return fullName;
    }
    
    public double getTotalGradePoints() {
        for(CourseEnrollment i: enrollment){
            totalGradePoints += i.getGradePoint();
        }
        return totalGradePoints;
    }
    
    public int getTotalCreditHours() {
        for(Course e: allCourses){
            totalCreditHours += e.getCreditHours();  
        }
        return totalCreditHours;
    }
    
    public double getCGPA(){
        cgpa = getTotalGradePoints()/getTotalCreditHours();
        return cgpa;
    }
    
    public List<Course> getFailedCourses() {
        return failedCourses;
    }
    
    public double getFailedCoursesCount() {
        return failedCourses.size();
    }
    
    public void updateAcademicInfo() {
        //I don't know what this method is for ngl. Kind of a scope creep maybe?
    }
    
    
}
