/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.app;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author alfredwilliamjulianto
 */
public class TestGUIFrame {
    public static void main(String[] args) {
        try {
            // Load data first
            System.out.println("Loading data...");
            DataRepository.loadCourseData();
            DataRepository.loadEnrollData();
            DataRepository.loadStudentData();
            System.out.println("Data loaded successfully!");
            
        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Create and show GUI
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Generate Academic Report - Preview");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1440, 1024);
            
            // Add your panel
            frame.add(new GenerateAcademicReportPanel());
            
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
