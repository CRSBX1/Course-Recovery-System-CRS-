/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.app;
import java.util.*;
import java.io.*;
import java.time.*;
/**
 *
 * @author Lenovo
 */
public class FileUtils {
    
    private FileUtils(){
        throw new AssertionError("This class can't be instantiated since its made for utility purposes");
    }
    
    @FunctionalInterface //Can only contain one abstract method
    public interface lineParser<T>{
        T parser(String line); //Interface for data parse
    }
    
    @FunctionalInterface
    public interface destination{
        HashMap<Integer,String> dataMap(); //Interface for different maps
    }
    
    public static <T> List<T> readFromFile(String filename, lineParser<T> parse) throws IOException{
        ArrayList<T> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))){
            String line;
            while ((line = br.readLine()) != null){
                list.add(parse.parser(line));
            }
        }
        catch(Exception e){
            System.out.println("Error when reading file: " + e.getMessage());
        }
        return list;
    }
    
    public static void writeToFile(String fileName, destination map) throws IOException{
        try{
           FileWriter clearFile = new FileWriter(fileName); //Use filewriter to clear file
           clearFile.close();
        }
        catch(IOException e){
            System.out.println("Error when writing to file: " + e.getMessage());
        }
        
        try{
           FileWriter write = new FileWriter(fileName,true);
           for(String s: map.dataMap().values()){
               write.append(s);
           }
           write.close();
        }
        catch(IOException e){
            System.out.println("Error when writing to file: " + e.getMessage());
        }
    }
    
    public static Student parseStudent(String line){
        String[] parts = line.split(",");
        return new Student(parts[0],parts[1],parts[2],parts[3],Integer.parseInt(parts[4]),Integer.parseInt(parts[5]),parts[6]);
    }
    
    public static Course parseCourse(String line){
        String[] parts = line.split(",");
        return new Course(parts[0],parts[1],Integer.parseInt(parts[2]),Integer.parseInt(parts[3]),parts[4],Integer.parseInt(parts[5]));
    }
    
    public static CourseEnrollment parseEnroll(String line){
        ArrayList<String> components = new ArrayList<>();
        String[] parts = line.split(",");
        for(int i=9;i<parts.length;i++){
            components.add(parts[i]);  
        }
        return new CourseEnrollment(parts[0],parts[1],parts[2],LocalDate.parse(parts[3]),parts[4],Double.parseDouble(parts[5]),Double.parseDouble(parts[6]),Double.parseDouble(parts[7]),Integer.parseInt(parts[8]),components);
        //String id, String student, String course, LocalDate date, String status, double asg_score, double mid_score, double final_score, int attempt, ArrayList<String> failed
    }
    
    public static HashMap writeStudent(){
        int counter = 0;
        HashMap<Integer,String> studentMap = new HashMap<>();
        for(Student i: DataRepository.studentList){
            String credentials = i.getStudentID()+","+i.getStudentName()+","+i.getEmail()+","+i.getStudentProgram()+","+Integer.toString(i.getYear())+","+Integer.toString(i.getSemester())+","+i.getEnrollStatus()+"\n";
            studentMap.put(counter, credentials);
            counter++;
        }
        return studentMap;
    }
    
    public static HashMap writeEnroll(){
        int counter = 0;
        String enroll;
        HashMap<Integer,String> enrollMap = new HashMap<>();
        for(CourseEnrollment i: DataRepository.enrollList){
            switch (i.getFailedComponent().size()) {
                case 3:
                    {
                        enroll = i.getEnrollID()+","+i.getStudentID()+","+i.getCourseID()+","+i.getEnrollDate()+","+i.getStatus()+","+i.getAssignmentGradePoint()+","+i.getMidtermTestGradePoint()+","+i.getFinalTestGradePoint()+","+i.getAttempts()+","+i.getFailedComponent().get(0)+","+i.getFailedComponent().get(1)+","+i.getFailedComponent().get(2)+"\n";
                        break;
                    }
                case 2:
                    {
                        enroll = i.getEnrollID()+","+i.getStudentID()+","+i.getCourseID()+","+i.getEnrollDate()+","+i.getStatus()+","+i.getAssignmentGradePoint()+","+i.getMidtermTestGradePoint()+","+i.getFinalTestGradePoint()+","+i.getAttempts()+","+i.getFailedComponent().get(0)+","+i.getFailedComponent().get(1)+"\n";
                        break;
                    }
                case 1:
                    {
                        enroll = i.getEnrollID()+","+i.getStudentID()+","+i.getCourseID()+","+i.getEnrollDate()+","+i.getStatus()+","+i.getAssignmentGradePoint()+","+i.getMidtermTestGradePoint()+","+i.getFinalTestGradePoint()+","+i.getAttempts()+","+i.getFailedComponent().get(0)+"\n";
                        break;
                    }
                default:
                    {
                        enroll = i.getEnrollID()+","+i.getStudentID()+","+i.getCourseID()+","+i.getEnrollDate()+","+i.getStatus()+","+i.getAssignmentGradePoint()+","+i.getMidtermTestGradePoint()+","+i.getFinalTestGradePoint()+","+i.getAttempts()+"\n";
                        break;
                    }
            }
            String credentials = enroll;
            enrollMap.put(counter, credentials);
            counter++;
        }
        return enrollMap;
    }
}
