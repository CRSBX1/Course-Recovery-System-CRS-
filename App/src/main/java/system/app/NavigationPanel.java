/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.app;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
/**
 *
 * @author Lenovo
 */
public class NavigationPanel extends JPanel{
    private JButton btnDashboard;
    private JButton btnCourseRecovery;
    private JButton btnAcademicReport;
    private JButton btnEnroll;
    private Font btnFont;
    
    public NavigationPanel(){
        setLayout(null);
        setBackground(new Color(0xF5F5F5)); //set color to whitesmoke
        
        //Configure button text font
        btnFont = new Font("Arial", Font.BOLD,20);
        
        //Configure dashboard button
        btnDashboard = new JButton("Dashboard");
        btnDashboard.setFont(btnFont);
        btnDashboard.setBounds(0,0,275,100);
        
        //Configure Course Recovery Plan Button
        btnCourseRecovery = new JButton("Course Recovery Plan");
        btnCourseRecovery.setFont(btnFont);
        btnCourseRecovery.setBounds(0, 100, 275, 100);
        
        //Configure Academic Report Button
        btnAcademicReport = new JButton("<html>Generate Academic Report</html>");
        btnAcademicReport.setFont(btnFont);
        btnAcademicReport.setBounds(0,200,275,100);
        
        //Configure Enrollment Button
        btnEnroll = new JButton("<html>Eligibility Check and Enrollment</html>");
        btnEnroll.setFont(btnFont);
        btnEnroll.setBounds(0, 300, 275, 100);
        
        add(btnDashboard);
        add(btnCourseRecovery);
        add(btnAcademicReport);
        add(btnEnroll);
    }
}
