package system.app.email;

import system.app.*;
import java.util.*;
import java.util.stream.*;

/* build msgs */
public class EmailHelper {

   
public static String buildAcademicSummary(Student s, String sid) {
    StringBuilder sb = new StringBuilder();
    sb.append("Dear Student,\n\n");
    sb.append("Here is your academic performance summary:\n\n");
    sb.append("Student ID: ").append(sid).append("\n");
    sb.append("Student Name: ").append(s.getName()).append("\n\n");

    List<CourseEnrollment> list = DataRepository.enrollList.stream()
            .filter(e -> e.getStudentID().equals(sid))
            .toList();

    sb.append("Courses and Grade Points:\n");
    sb.append("---------------------------------\n");

    double totalGPxCredits = 0.0;
    int totalCredits = 0;

    if (list.isEmpty()) {
        sb.append("(No records found)\n");
    } else {
        Map<String, Course> cmap = DataRepository.courseList.stream()
                .collect(Collectors.toMap(Course::getID, c -> c));

        for (CourseEnrollment e : list) {
            Course c = cmap.get(e.getCourseID());
            String cname = (c == null ? "(Unknown)" : c.getCourseInfo()[1]);
            int credits = (c == null ? 0 : c.getCreditHours());
            double gp = e.getGradePoint();
            sb.append(e.getCourseID())
              .append(" - ").append(cname)
              .append("     ").append(gp)
              .append("\n");

            totalGPxCredits += gp * credits;
            totalCredits += credits;
        }
    }

    sb.append("---------------------------------\n\n");

    String cgpaText = "N/A";
    double cgpaVal = Double.NaN;
    if (totalCredits > 0) {
        // digit-by-digit calc (already done by Java but showing reasoning):
        // e.g. 4.0*4 + 3.5*4 + 3.0*3 = 16 + 14 + 9 = 39 ; credits = 11 ; cgpa = 39 / 11 = 3.5454...
        cgpaVal = totalGPxCredits / totalCredits;
        cgpaText = String.format("%.2f", cgpaVal);
    }
    sb.append("Cumulative GPA (CGPA): ").append(cgpaText).append("\n\n");

    // eligibility: cgpa >= 2.0 and failed courses <= 3
    long failedCount = DataRepository.enrollList.stream()
            .filter(e -> e.getStudentID().equals(sid))
            .filter(e -> "Failed".equalsIgnoreCase(e.getStatus()))
            .count();

    String remarks;
    if (!Double.isNaN(cgpaVal) && cgpaVal >= 2.0 && failedCount <= 3) {
        remarks = "Eligible for progression to next level.";
    } else {
        remarks = "Not eligible to progress. Please contact your course administrator.";
    }
    sb.append("Remarks: ").append(remarks).append("\n\n");

    sb.append("Please contact your course administrator for further details.\n\n");
    sb.append("Best regards,\nAsia Pacific University\n");

    return sb.toString();


    }

    /* recovery msg */
    public static String buildRecoveryPlan(String courseID, String courseName, List<String[]> rows) {
        StringBuilder sb = new StringBuilder();
        sb.append("Dear Student,\n\n");
        sb.append("Your course recovery plan has been prepared.\n\n");
        sb.append("Course: ").append(courseID).append(" - ").append(courseName).append("\n\n");

        sb.append("Milestones:\n");
        sb.append("----------------------------------------------------\n");
        sb.append(String.format("%-10s %-12s %s\n", "TaskID", "Week", "Task"));
        sb.append("----------------------------------------------------\n");

        for (String[] r : rows) {
            sb.append(String.format("%-10s %-12s %s\n", r[0], r[1], r[2]));
        }

        sb.append("----------------------------------------------------\n\n");
        sb.append("Best regards,\nAsia Pacific University\n");
        return sb.toString();
    }
}
