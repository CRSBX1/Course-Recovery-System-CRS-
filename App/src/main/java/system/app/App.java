/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package system.app;
import java.io.*;
import java.util.*;
/**
 *
 * @author Lenovo
 */
public class App {

    public static void main(String[] args) throws IOException{
        //Read from file test
        List<Student> student = FileUtils.readFromFile("Data/Students.txt", FileUtils::parseStudent);
        for(Student i: student){
            String a = i.getName();
            System.out.println("Name: " + a);
        }
    }
}
