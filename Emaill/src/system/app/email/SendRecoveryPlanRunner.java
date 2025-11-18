package system.app.email;

import system.app.*;
import java.util.*;
import java.io.*;

/* send plan */
public class SendRecoveryPlanRunner {

    /* send for course */
    public static void sendForCourse(String courseID, List<String[]> milestones) {
        // load data
        SafeFileLoader.loadAllData();
        Map<String,String> emails = StudentEmailMap.load("Data/Students.txt");

        // find course name
        String courseName = "(Unknown)";
        for (Course c : DataRepository.courseList) {
            if (courseID.equals(c.getID())) { courseName = c.getCourseInfo()[1]; break; }
        }

        int sent = 0;
        // iterate enrollments
        for (CourseEnrollment e : DataRepository.enrollList) {
            if (!courseID.equals(e.getCourseID())) continue;
            String sid = e.getStudentID();
            String to = emails.get(sid);
            if (to == null || to.isEmpty()) continue;

            try {
                EmailSender.sendPlan(to, courseID, courseName, milestones);
                sent++;
            } catch (Exception ex) {
                System.err.println("Failed: " + sid + " -> " + ex.getMessage());
            }
        }

        System.out.println("Recovery plan sent to " + sent + " students for " + courseID);
    }

    /* example main */
    public static void main(String[] args) throws Exception {
        // example milestones: {TaskID, Week, Task}
        List<String[]> ms = new ArrayList<>();
        ms.add(new String[]{"M001","Week 1-2","Review lectures"});
        ms.add(new String[]{"M002","Week 3-4","Complete resit assignment"});
       
        sendForCourse("C001", ms);
    }
}
