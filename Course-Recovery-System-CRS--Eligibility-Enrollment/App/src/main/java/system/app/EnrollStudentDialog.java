package system.app;
import javax.swing.*;
import java.awt.*;

public class EnrollStudentDialog extends JDialog {
    private int result = JOptionPane.CLOSED_OPTION;
    private JLabel dialogMessage;
    private JButton primaryBtn;
    private Icon infoIcon;
    private JLabel infoLabel;
    private Font dialogFont;

    public EnrollStudentDialog(JFrame parent, String message, String btnText){
        super(parent,"Eligibility Check",true);
        setSize(400,180);
        setLayout(null);

        infoIcon = UIManager.getIcon("OptionPane.informationIcon");
        if (infoIcon != null) {
            Image img = ((ImageIcon) infoIcon).getImage();
            Image scaledImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            infoIcon = new ImageIcon(scaledImg);
        }
        infoLabel = new JLabel(infoIcon);
        infoLabel.setBounds(20, 20, 50, 50);

        dialogFont = new Font("Arial",Font.BOLD,18);

        dialogMessage = new JLabel("<html><center>" + message + "</center></html>", SwingConstants.CENTER);
        dialogMessage.setFont(dialogFont);
        dialogMessage.setBounds(80, 20, 300, 50);

        primaryBtn = new JButton(btnText);
        primaryBtn.setBounds(140, 90, 100, 30);
        primaryBtn.addActionListener(e -> {
            result = JOptionPane.OK_OPTION;
            dispose();
        });

        add(dialogMessage);
        add(primaryBtn);
        add(infoLabel);

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public int getResult() {
        return result;
    }
}
