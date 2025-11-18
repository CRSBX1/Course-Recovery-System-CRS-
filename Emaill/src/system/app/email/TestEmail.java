package system.app.email;

import system.app.*;
import java.util.*;
import java.lang.reflect.*;

/* test send */
public class TestEmail {

    public static void main(String[] args) throws Exception {

        // load data
       SafeFileLoader.loadAllData();


        // map
        Map<String,String> map = StudentEmailMap.load("Data/Students.txt");

        //  test student
        String sid = "S000011";

        //  email from Students.txt
        String email = map.get(sid);
        if (email == null) {
            System.out.println("no email for " + sid);
            return;
        }

        // find student object using reflection 
        Student pick = null;
        for (Student st : DataRepository.studentList) {
            try {
                Field f = Student.class.getDeclaredField("email");
                f.setAccessible(true);
                String em = (String) f.get(st);
                if (email.equals(em)) {
                    pick = st;
                    break;
                }
            } catch (Exception ex) {
                
            }
        }

        if (pick == null) {
            System.out.println("Student object not found for " + sid);
            return;
        }

        // build summary email text
        String body = EmailHelper.buildAcademicSummary(pick, sid);

        // send
        EmailService svc = EmailConfig.fromConfigFile("config.properties");
        String from = svc != null ? System.getenv().get("EMAIL_USERNAME") : null;

        // fallback for FROM address
        if (from == null) from = "aputesting780@gmail.com";

        svc.sendPlainText(from, "APU", email, "Test Email", body);

        System.out.println("sent to " + email);
    }
}
