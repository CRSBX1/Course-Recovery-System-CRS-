/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.app;

/**
 *
 * @author Lenovo
 */
public class Course {
   private String courseID;
   private String courseName;
   private int creditHours;
   private int semester;
   private String instructor;
   private int capacity;
   
   public Course(String id, String name, int hours, int semester, String instructor, int capacity){
       courseID = id;
       courseName = name;
       creditHours = hours;
       this.semester = semester;
       this.instructor = instructor;
       this.capacity = capacity;
   }
   
   public void assignInstructor(String ins){
       instructor = ins;
   }
   
   public void updateCourseDetails(int hours, int capacity, int semester){
       creditHours = hours;
       this.capacity = capacity;
       this.semester = semester;
   }
   
   public String getID(){
       return courseID;
   }
   
   public int getCreditHours(){
       return creditHours;
   }
   
   public String[] getCourseInfo(){
       return  new String []{courseID, courseName};
   }
   
}
