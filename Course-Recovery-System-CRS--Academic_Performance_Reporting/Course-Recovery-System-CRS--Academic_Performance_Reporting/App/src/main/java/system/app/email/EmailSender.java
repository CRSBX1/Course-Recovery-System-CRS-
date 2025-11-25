package system.app.email;

import system.app.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.*;
import java.awt.Frame;
import java.awt.Dimension;
import system.app.email.EmailHelper;
import java.io.File;
import java.io.IOException;
import jakarta.mail.MessagingException;

public class EmailSender {

    public static void sendSummary(Student s, String sid, String email) throws Exception {
        EmailService svc = EmailConfig.fromConfigFile("config.properties");
        String from = "aputesting780@gmail.com";
        String body = EmailHelper.buildAcademicSummary(s, sid);
        PerformanceReport report = new PerformanceReport(
                "R" + System.currentTimeMillis(),
                s,
                s.getSemester(),
                String.valueOf(s.getYear())
        );
        String tmp = System.getProperty("java.io.tmpdir") + File.separator + s.getStudentID() + "_report_" + System.currentTimeMillis() + ".pdf";
        try {
            report.exportToPDF(tmp);
            svc.sendWithAttachment(from, "Asia Pacific University", email, "Your Academic Performance Summary", body, new File(tmp));
        } catch (MessagingException | IOException ex) {
            throw ex;
        } finally {
            try { new File(tmp).delete(); } catch (Exception ignore) {}
        }
    }

    public static void sendPlan(String email, String courseID, String courseName, List<String[]> rows, String remarks) throws Exception {
        EmailService svc = EmailConfig.fromConfigFile("config.properties");
        String from = "aputesting780@gmail.com";
        String body = EmailHelper.buildRecoveryPlan(courseID, courseName, rows, remarks);
        svc.sendPlainText(from, "Asia Pacific University", email, "Your Course Recovery Plan", body);
    }

}
