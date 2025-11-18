package system.app.email;

import system.app.*;
import java.util.*;

/* send ops */
public class EmailSender {

    /* send summary */
    public static void sendSummary(Student s, String sid, String email) throws Exception {
        EmailService svc = EmailConfig.fromConfigFile("config.properties");
        String from = "aputesting780@gmail.com";
        String body = EmailHelper.buildAcademicSummary(s, sid);
        svc.sendPlainText(from, "Asia Pacific University", email, "Your Academic Performance Summary", body);
    }

    /* send plan */
    public static void sendPlan(String email, String courseID, String courseName, List<String[]> rows) throws Exception {
        EmailService svc = EmailConfig.fromConfigFile("config.properties");
        String from = "aputesting780@gmail.com";
        String body = EmailHelper.buildRecoveryPlan(courseID, courseName, rows);
        svc.sendPlainText(from, "Asia Pacific University", email, "Your Course Recovery Plan", body);
    }
}
