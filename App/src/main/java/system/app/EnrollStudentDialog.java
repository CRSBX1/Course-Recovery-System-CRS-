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
public class EnrollStudentDialog extends JDialog{
    private JLabel dialogMessage;
    private JButton primaryBtn;
    private Icon infoIcon;
    private JLabel infoLabel;
    private Font dialogFont;
    
    public EnrollStudentDialog(JFrame parent, String message, String btnText){
        super(parent,"Eligibility Check",true);
        setSize(400,180);
        setLayout(null);
        
        //Initialize Icon and its label
        infoIcon = UIManager.getIcon("OptionPane.informationIcon");
        if (infoIcon != null) {
            Image img = ((ImageIcon) infoIcon).getImage();
            Image scaledImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            infoIcon = new ImageIcon(scaledImg);
        }
        infoLabel = new JLabel(infoIcon);
        infoLabel.setBounds(20, 20, 50, 50);
        
        //Initialize and configure the dialog's font
        dialogFont = new Font("Arial",Font.BOLD,18);
        
        //Initialize and configure the dialog's message
        dialogMessage = new JLabel("<html><center>" + message + "</center></html>", SwingConstants.CENTER);
        dialogMessage.setFont(dialogFont);
        dialogMessage.setBounds(80, 20, 300, 50);
        
        primaryBtn = new JButton(btnText);
        primaryBtn.setBounds(140, 90, 100, 30);
        primaryBtn.addActionListener(e -> dispose());
        
        add(dialogMessage);
        add(primaryBtn);
        add(infoLabel);

        setLocationRelativeTo(parent);
        setVisible(true);
    }
}