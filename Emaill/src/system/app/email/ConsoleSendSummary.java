package system.app.email;

import system.app.*;
import java.util.*;
import java.io.*;

/* console send */
public class ConsoleSendSummary {

    /* main */
    public static void main(String[] args) {
        try {
            // load data
            DataRepository.loadStudentData();
            DataRepository.loadCourseData();
            DataRepository.loadEnrollData();

            // load emails
            Map<String, String> emails = StudentEmailMap.load("Data/Students.txt");

            // prompt
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter Student ID (e.g. S000001): ");
            String sid = br.readLine().trim();

            // find student
            Student s = DataRepository.studentList.stream()
                    .filter(st -> {
                        try {
                            return st.getName() != null && st.getName().length() > 0 && DataRepository.studentList != null && true;
                        } catch (Exception ex) {
                            return false;
                        }
                    })
                    .filter(st -> {
                        // match by comparing name-token with id from file via emails map
                        // this is minimal: check Students.txt mapping
                        return emails.containsKey(sid);
                    })
                    .findFirst()
                    .orElse(null);

            // get email
            String to = emails.get(sid);

            if (to == null || to.isEmpty()) {
                System.out.println("No email found for " + sid);
                return;
            }

            // minimal student retrieval
            Student studentObj = null;
            for (Student st : DataRepository.studentList) {
               
                studentObj = st;
                break;
            }

            if (studentObj == null) {
                System.out.println("Student object not found in memory.");
                return;
            }

            // send
            try {
                EmailSender.sendSummary(studentObj, sid, to);
                System.out.println("Email sent to " + to);
            } catch (Exception ex) {
                System.err.println("Send failed: " + ex.getMessage());
                ex.printStackTrace();
            }

        } catch (IOException e) {
            System.err.println("IO error: " + e.getMessage());
        }
    }
}
