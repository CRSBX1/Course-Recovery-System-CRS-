package system.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CourseEnrollmentLoader {

    private static final String ENROLL_FILE = "Data" + File.separator + "Course Enrollment.txt";

    public static class FailedRecord {
        public String entryId;
        public String studentId;
        public String courseId;
        public List<String> components = new ArrayList<>();

        public FailedRecord(String entryId, String studentId, String courseId) {
            this.entryId = entryId;
            this.studentId = studentId;
            this.courseId = courseId;
        }
    }

    public static List<FailedRecord> loadFailed() {
        List<FailedRecord> out = new ArrayList<>();
        File f = new File(ENROLL_FILE);

        if (!f.exists()) return out;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] p = line.split(",", -1);
                if (p.length < 5) continue;

                String entryId  = p[0].trim();
                String studentId = p[1].trim();
                String courseId = p[2].trim();
                String status   = p[4].trim();

                if (!status.equalsIgnoreCase("Failed")) continue;

                FailedRecord fr = new FailedRecord(entryId, studentId, courseId);

                for (int i = 9; i < p.length; i++) {
                    String comp = p[i].trim();
                    if (!comp.isEmpty()) fr.components.add(comp);
                }

                out.add(fr);
            }
        } catch (IOException ex) {
            System.err.println("CourseEnrollmentLoader: " + ex.getMessage());
        }

        return out;
    }
}
