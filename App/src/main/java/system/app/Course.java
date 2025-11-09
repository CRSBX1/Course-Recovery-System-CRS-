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
   
   public void assignInstructor(String ins){
   //Extra feature? Not sure about this one
   }
   
   public void updateCourseDetails(){
   //Extra feature again
   }
   
   public int getCreditHours(){
       return creditHours;
   }
   
   public String[] getCourseInfo(){
       return  new String []{courseID, courseName};
   }
   
}
