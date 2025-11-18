package system.app.email;

import system.app.*;
import java.util.*;

/* test plan */
public class TestRecoveryPlan {

    public static void main(String[] args) throws Exception {

        // load data
        SafeFileLoader.loadAllData();

        // test student ID from Students.txt
        String sid = "S000011";

        // load email
        Map<String,String> emails = StudentEmailMap.load("Data/Students.txt");
        String email = emails.get(sid);

        if (email == null) {
            System.out.println("No email for " + sid);
            return;
        }

        // example course
        String courseID = "C101";
        String courseName = "Sample Course"; // put real name if you want

        // milestones
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"M001", "Week 1 - 2", "Review all lecture topic"});
        rows.add(new String[]{"M002", "Week 3 - 4", "Work on resit assignment"});
        rows.add(new String[]{"M003", "Week 4", "Submit resit assignment"});
        rows.add(new String[]{"M004", "Week 4", "Take resit exam"});

        // send email
        EmailService svc = EmailConfig.fromConfigFile("config.properties");
        String from = "aputesting780@gmail.com";

        String body = EmailHelper.buildRecoveryPlan(courseID, courseName, rows);

        svc.sendPlainText(from, "Asia Pacific University", email, "Test Recovery Plan", body);

        System.out.println("Sent recovery plan to " + email);
    }
}
