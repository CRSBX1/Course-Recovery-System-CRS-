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
        T parser(String line);
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
    
    public static Student parseStudent(String line){
        String[] parts = line.split(",");
        return new Student(parts[0],parts[1],parts[2],parts[3],Integer.parseInt(parts[4]),Integer.parseInt(parts[5]));
    }
    
    public static Course parseCourse(String line){
        String[] parts = line.split(",");
        return new Course(parts[0],parts[1],Integer.parseInt(parts[2]),Integer.parseInt(parts[3]),parts[4],Integer.parseInt(parts[5]));
    }
    
    public static CourseEnrollment parseEnroll(String line){
        ArrayList<String> components = new ArrayList<>();
        String[] parts = line.split(",");
        for(int i=9;i<=parts.length;i++){
            components.add(parts[i]);  
        }
        return new CourseEnrollment(parts[0],parts[1],parts[2],LocalDate.parse(parts[3]),parts[4],Integer.parseInt(parts[5]),Integer.parseInt(parts[6]),Integer.parseInt(parts[7]),Integer.parseInt(parts[8]),components);
    }    
}
