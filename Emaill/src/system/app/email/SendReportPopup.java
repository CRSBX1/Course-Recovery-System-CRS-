package system.app.email;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.*;
import system.app.*;
import java.util.*;

public class SendReportPopup {

    public static void open(String sid) {

        // load data
        SafeFileLoader.loadAllData();
        Map<String,String> map = StudentEmailMap.load("Data/Students.txt");

        // get default email if exists
        String defaultEmail = map.getOrDefault(sid, "");

        // create dialog
        JDialog dialog = new JDialog((Frame)null, "Send Report", true);
        dialog.setSize(450, 200);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(null);

        // main panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // title
        JLabel title = new JLabel("Send Academic Report to E-mail");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));

        // email
        JLabel lbl = new JLabel("Destination E-mail Address:");
        JTextField emailField = new JTextField(defaultEmail);
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        panel.add(title);
        panel.add(Box.createVerticalStrut(12));
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(6));
        panel.add(emailField);

        // buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(new Color(230, 50, 50)); // red
        cancelBtn.setForeground(Color.WHITE);

        JButton sendBtn = new JButton("Send");
        sendBtn.setBackground(new Color(40, 170, 90)); // green
        sendBtn.setForeground(Color.WHITE);

        // cancel
        cancelBtn.addActionListener(e -> dialog.dispose());

        // send
        sendBtn.addActionListener(e -> {

            String to = emailField.getText().trim();
            if (to.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Empty email");
                return;
            }

            // find student by email
            Student pick = null;
            for (Student st : DataRepository.studentList) {
                try {
                    Field f = Student.class.getDeclaredField("email");
                    f.setAccessible(true);
                    String studentEmail = (String) f.get(st);
                    if (to.equalsIgnoreCase(studentEmail)) {
                        pick = st;
                        break;
                    }
                } catch (Exception ex) {}
            }

            if (pick == null) {
                JOptionPane.showMessageDialog(dialog, "No student with email: " + to);
                return;
            }

            String realSID = sid;
            try {
                Field idField = Student.class.getDeclaredField("studentID");
                idField.setAccessible(true);
                realSID = (String) idField.get(pick);
            } catch (Exception ex) {}

            try {
                EmailSender.sendSummary(pick, realSID, to);
                JOptionPane.showMessageDialog(dialog, "Report sent to " + to);
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Send failed: " + ex.getMessage());
            }
        });

        btnPanel.add(cancelBtn);
        btnPanel.add(sendBtn);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }


    public static void main(String[] args) {
        open("S000012");
    }
}
