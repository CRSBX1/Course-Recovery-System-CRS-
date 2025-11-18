package system.app;

import java.time.LocalDate;
import java.util.*;
import java.io.*;

/* safe loader */
public class SafeFileLoader {

    public static void loadAllData() {

        try {
            DataRepository.studentList = FileUtils.readFromFile("Data/Students.txt", FileUtils::parseStudent);
        } catch (Exception e) {
            DataRepository.studentList = new ArrayList<>();
        }

        try {
            DataRepository.courseList = FileUtils.readFromFile("Data/Course.txt", FileUtils::parseCourse);
        } catch (Exception e) {
            DataRepository.courseList = new ArrayList<>();
        }

        try {
            DataRepository.enrollList = FileUtils.readFromFile("Data/Course Enrollment.txt", line -> {
                try {
                    ArrayList<String> comp = new ArrayList<>();
                    String[] p = line.split(",");

                    String id = p.length>0 ? p[0] : "X";
                    String sid = p.length>1 ? p[1] : "X";
                    String cid = p.length>2 ? p[2] : "X";

                    LocalDate d = LocalDate.now();
                    if (p.length>3 && p[3].length()>0) d = LocalDate.parse(p[3]);

                    String status = p.length>4 ? p[4] : "Unknown";
                    String grade = p.length>5 ? p[5] : "N";

                    double gp = 0.0;
                    if (p.length>6) try { gp = Double.parseDouble(p[6]); } catch(Exception ex){}

                    int att = 0;
                    if (p.length>7) try { att = Integer.parseInt(p[7]); } catch(Exception ex){}

                    for (int i = 8; i < p.length; i++) {
                        comp.add(p[i]);
                    }

                    return new CourseEnrollment(id, sid, cid, d, status, grade, gp, att, comp);

                } catch (Exception ex) {
                    return new CourseEnrollment("ERR","ERR","ERR", LocalDate.now(),"ERR","N",0.0,0,new ArrayList<>());
                }
            });
        } catch (Exception e) {
            DataRepository.enrollList = new ArrayList<>();
        }
    }
}
