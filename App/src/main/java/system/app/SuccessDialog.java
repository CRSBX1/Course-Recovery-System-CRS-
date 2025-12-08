package system.app;

import javax.swing.*;
import java.awt.*;

public class SuccessDialog extends JDialog {
    public SuccessDialog(Frame parent, String title, String message) {
        super(parent, title, true);
        setSize(420, 180);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10,10));
        JLabel lbl = new JLabel("<html><h2>" + title + "</h2></html>");
        JTextArea txt = new JTextArea(message);
        txt.setEditable(false);
        txt.setLineWrap(true);
        txt.setWrapStyleWord(true);
        JButton ok = new JButton("Okay");
        ok.addActionListener(e -> setVisible(false));
        add(lbl, BorderLayout.NORTH);
        add(new JScrollPane(txt), BorderLayout.CENTER);
        JPanel p = new JPanel();
        p.add(ok);
        add(p, BorderLayout.SOUTH);
    }
}
