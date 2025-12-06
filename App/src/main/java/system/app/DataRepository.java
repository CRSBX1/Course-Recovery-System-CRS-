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
    
}
