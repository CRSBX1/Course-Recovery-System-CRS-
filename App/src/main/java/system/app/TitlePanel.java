/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.app;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author Lenovo
 */
public class TitlePanel extends JPanel{
    private JLabel title_label;
    
    public TitlePanel(){
        setLayout(null);
        setBackground(new Color(0x7DC3E3));
        
        title_label = new JLabel("Course Recovery System");
        title_label.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        title_label.setBounds(30,22,700,35);
        
        add(title_label);
    }
}
