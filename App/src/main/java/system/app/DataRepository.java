/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.app;
import java.util.*;
import java.io.*;
/**
 *
 * @author Lenovo
 */
public class DataRepository {
    public static List<Student> studentList = new ArrayList<>();
    public static List<Course> courseList = new ArrayList<>();
    public static List<CourseEnrollment> enrollList = new ArrayList<>();
    
    public static void loadEnrollData() throws IOException{
        enrollList = FileUtils.readFromFile("Data/Course Enrollment.txt", FileUtils::parseEnroll);
    }
    
    public static void loadStudentData() throws IOException{
        studentList = FileUtils.readFromFile("Data/Students.txt", FileUtils::parseStudent);
    }
    
    public static void loadCourseData() throws IOException{
        courseList = FileUtils.readFromFile("Data/Course.txt", FileUtils::parseCourse);
    }
    
    public static Student findStudentByID(String ID){
        for(Student i: studentList) {
            if(i.getStudentID().equals(ID)){
                return i;
            }
        }
        return null;
    }
    
    public static Course findCourseByID(String id){
       for(Course c: DataRepository.courseList){
           if(c.getID().equals(id)){
               return c;
           }
       }
       return null;
   }
    
    public static void linkAll() {
    for (Student s : studentList) {
        List<CourseEnrollment> eList = enrollList.stream()
                .filter(e -> e.getStudentID().equals(s.getStudentID()))
                .toList();

        s.setEnrollment(eList);

        List<Course> failed = new ArrayList<>();
        List<Course> all = new ArrayList<>();
        
        for (CourseEnrollment ce : eList) {
            Course course = findCourseByID(ce.getCourseID());
            if (course != null) {
                all.add(course);
                ce.setOverallGradePoint(course.getCreditHours());
                //add
                if (!ce.getStatus().equals("Passed")) {
                    failed.add(course);
                }
            }
        }

        s.setFailedCourses(failed);
        s.setAllCourses(all);
        s.setTotalGradePoints();
        s.setTotalCreditHours();
        s.setCGPA();
        
    }
}

}
