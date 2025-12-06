/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package system.app;
import java.io.*;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
/**
 *
 * @author Lenovo
 */
public class App {

    public static void main(String[] args) throws IOException{
        //Read from file test
        //List<Student> student = FileUtils.readFromFile("Data/Students.txt", FileUtils::parseStudent);
        //for(Student i: student){
        //    String a = i.getName();
        //    System.out.println("Name: " + a);
        //}
        DataRepository.loadCourseData();
        DataRepository.loadStudentData();
        DataRepository.loadEnrollData();
        DataRepository.linkAll();

        //EnrollStudentFrame frame = new EnrollStudentFrame();
        /**SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Generate Academic Report - Preview");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1440, 1024);

            // Add your panel
            frame.add(new GenerateAcademicReportPanel());

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });**/
        
        SwingUtilities.invokeLater(() -> new LoginFrame(new UserManager()));
    }
}
