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
public class EligibilityChecker {
    private double minimumCGPA = 2.0;
    private int maximumFailedCourses = 3;
    
    public boolean checkProgressionEligibility(Student student){
        return student.getFailedCoursesCount()>3 | student.getCGPA()<2.0;
    }
    
    public boolean checkFailedCoursesCriteria(Student student){
        return true;
    }
    
    public List<Student> getIneligibleStudents(){
        List<Student> student_not_pass = new ArrayList<>();
        for(Student i: DataRepository.studentList){
            if(i.getFailedCoursesCount()>3 | i.getCGPA()<2.0){
                student_not_pass.add(i);
            }
        }
        return student_not_pass;
    }
    
    
}
