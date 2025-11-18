package system.app.email;

import system.app.*;
import java.util.*;
import java.io.*;

/* console plan */
public class ConsoleSendPlan {

    /* main */
    public static void main(String[] args) {
        try {
            // load data
            DataRepository.loadStudentData();
            DataRepository.loadCourseData();
            DataRepository.loadEnrollData();

            // load emails
            Map<String, String> emails = StudentEmailMap.load("Data/Students.txt");

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            // ask course id
            System.out.print("Enter Course ID (e.g. C001): ");
            String cid = br.readLine().trim();

            // find course name
            String cname = "(Unknown)";
            for (Course c : DataRepository.courseList) {
                if (c.getID().equals(cid)) {
                    cname = c.getCourseInfo()[1];
                    break;
                }
            }

            // build milestones
            List<String[]> rows = new ArrayList<>();
            System.out.println("Enter milestones (type 'done' for TaskID to finish):");

            while (true) {
                System.out.print("TaskID: ");
                String t = br.readLine().trim();
                if (t.equalsIgnoreCase("done")) break;

                System.out.print("Week: ");
                String w = br.readLine().trim();

                System.out.print("Task: ");
                String task = br.readLine().trim();

                rows.add(new String[]{t, w, task});
            }

            // send to all students enrolled
            int sent = 0;
            for (CourseEnrollment e : DataRepository.enrollList) {
                if (e.getCourseID().equals(cid)) {
                    String sid = e.getStudentID();
                    String to = emails.get(sid);
                    if (to == null || to.isEmpty()) continue;

                    try {
                        EmailSender.sendPlan(to, cid, cname, rows);
                        sent++;
                    } catch (Exception ex) {
                        // ignore
                    }
                }
            }

            System.out.println("Plan sent to " + sent + " students.");

        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }
}
