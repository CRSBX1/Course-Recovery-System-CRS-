/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.app;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alfredwilliamjulianto
 */
public class PerformanceReport {

    private String reportID;
    private Student student;
    private int semester;
    private String year;
    private List<CourseEnrollment> enrollments;
    private double cgpa;

    // Constructor
    public PerformanceReport(String reportID, Student student, int semester, String year) {
        this.reportID = reportID;
        this.student = student;
        this.semester = semester;
        this.year = year;

        this.enrollments = new ArrayList<>();
        if (student.getEnrollment() != null) {
            this.enrollments.addAll(student.getEnrollment());
        }

        this.cgpa = student.getCGPA();
    }

    // Helper Method
    private Course getCourseFromRepository(String courseID) {
        for (Course course : DataRepository.courseList) {
            if (course.getID().equals(courseID)) {
                return course;
            }
        }
        return null;
    }

    // Getters and Setters
    public String getReportID() {
        return reportID;

    }

    public Student getStudent() {
        return student;
    }

    public int getSemester() {
        return semester;
    }

    public String getYear() {
        return year;
    }

    public List<CourseEnrollment> getEnrollments() {
        return enrollments;
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    public void setEnrollments(List<CourseEnrollment> enrollments) {
        this.enrollments = enrollments;
    }

    // Method
    public String generateReport() {
        StringBuilder report = new StringBuilder();

        // Header
        report.append("\n");
        report.append("ACADEMIC PERFORMANCE REPORT\n");
        report.append("\n\n");

        // Greeting
        report.append("Dear ").append(student.getStudentName()).append(",\n\n");
        report.append("This is your official academic performance report for the current semester.\n\n\n");

        // Student Information
        report.append("STUDENT INFORMATION\n\n");
        report.append("Student ID        : ").append(student.getStudentID()).append("\n");
        report.append("Student Name      : ").append(student.getStudentName()).append("\n");
        report.append("Program           : ").append(student.getProgram()).append("\n");
        report.append("Academic Year     : ").append(year).append("\n");
        report.append("Semester          : ").append(semester).append("\n");
        report.append("\n\n");

        // Course Performance
        report.append("COURSE PERFORMANCE\n\n");

        if (enrollments.isEmpty()) {
            report.append("No course enrollments found for this semester.\n\n");
        } else {
            int courseNum = 1;
            for (CourseEnrollment enrollment : enrollments) {
                String courseID = enrollment.getCourseID();
                String overallGrade = enrollment.getOverallGrade();
                double gradePoint = enrollment.getOverallGradePoint();

                Course course = getCourseFromRepository(courseID);

                if (course != null) {
                    String courseName = course.getCourseName();
                    int creditHours = course.getCreditHours();

                    report.append("Course ").append(courseNum++).append(":\n");
                    report.append("  ").append(courseID).append(" - ").append(courseName).append("\n");
                    report.append("  Credits: ").append(creditHours).append(" CH");
                    report.append("  |  Grade: ").append(overallGrade);
                    report.append("  |  Grade Point: ").append(String.format("%.2f", gradePoint)).append("\n\n");
                }
            }
        }

        report.append("\n");

        // Academic Summary
        report.append("ACADEMIC SUMMARY\n\n");

        double cgpa = student.getCGPA();
        int failedCount = student.getFailedCourses().size();

        report.append("Cumulative GPA (CGPA)     : ").append(String.format("%.2f", cgpa)).append("\n");
        report.append("Total Credit Hours        : ").append(student.getTotalCreditHours()).append("\n");
        report.append("Total Grade Points        : ").append(String.format("%.2f", student.getTotalGradePoints())).append("\n");
        report.append("Failed Courses            : ").append(failedCount).append("\n\n");

        // Academic Status
        String status;
        if (cgpa >= 3.7) {
            status = "Excellent Performance - Dean's List";
        } else if (cgpa >= 3.0) {
            status = "Good Academic Standing";
        } else if (cgpa >= 2.0 && failedCount <= 3) {
            status = "Satisfactory";
        } else {
            status = "Academic Concern - Please See Advisor";
        }

        report.append("Academic Status           : ").append(status).append("\n\n\n");

        // Failed Courses (if any)
        if (failedCount > 0) {
            report.append("COURSES REQUIRING ATTENTION\n\n");

            for (Course failedCourse : student.getFailedCourses()) {
                report.append("  - ").append(failedCourse.getID()).append(": ");
                report.append(failedCourse.getCourseName()).append("\n");
            }

            report.append("\nPlease contact your academic advisor to discuss recovery options.\n\n\n");
        }

        // Progression Eligibility
        report.append("PROGRESSION ELIGIBILITY\n\n");

        if (cgpa >= 2.0 && failedCount <= 3) {
            report.append("Status: ELIGIBLE for progression to next semester\n\n");
            report.append("Requirements:\n");
            report.append("  CGPA >= 2.0           : ").append(String.format("%.2f", cgpa)).append(" (PASS)\n");
            report.append("  Failed Courses <= 3   : ").append(failedCount).append(" (PASS)\n");
        } else {
            report.append("Status: NOT ELIGIBLE for progression\n\n");
            report.append("Requirements:\n");
            report.append("  CGPA >= 2.0           : ").append(String.format("%.2f", cgpa));
            report.append(" (").append(cgpa >= 2.0 ? "PASS" : "FAIL").append(")\n");
            report.append("  Failed Courses <= 3   : ").append(failedCount);
            report.append(" (").append(failedCount <= 3 ? "PASS" : "FAIL").append(")\n");
            report.append("\nPlease schedule a meeting with your academic advisor.\n");
        }

        report.append("\n\n");

        // Contact Information
        report.append("If you have any questions, please contact:\n\n");
        report.append("Academic Officer Office\n");
        report.append("Email: aputesting780@gmail.com\n");
        report.append("Phone: +60 6767 6767\n\n\n");

        // Closing
        report.append("Best regards,\n\n");
        report.append("Academic Officer Department\n\n\n");

        // Footer
        report.append("---\n");
        report.append("Report ID: ").append(reportID).append("\n");
        report.append("Generated: ").append(java.time.LocalDate.now()).append("\n");
        report.append("Confidential - For student use only\n");

        return report.toString();
    }

    public void exportToPDF(String filePath) {
        Document document = new Document(PageSize.A4);

        try {
            // Create PDF writer
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Add title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph title = new Paragraph("ACADEMIC PERFORMANCE REPORT", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Add student information - CONNECTED: Using Student object
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

            document.add(new Paragraph("Student Information", headerFont));
            document.add(new Paragraph("Student Name: " + student.getStudentName(), normalFont));
            document.add(new Paragraph("Student ID: " + student.getStudentID(), normalFont));
            document.add(new Paragraph("Program: " + student.getProgram(), normalFont));
            document.add(new Paragraph("Semester: " + semester, normalFont));
            document.add(new Paragraph("Year: " + year, normalFont));
            document.add(new Paragraph(" "));

            // Create course table - CONNECTED: Using CourseEnrollment and Course
            PdfPTable table = new PdfPTable(8);  // Changed to 8 columns
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            table.setSpacingAfter(10);

            // Set column widths
            float[] columnWidths = {10f, 25f, 8f, 10f, 10f, 12f, 10f, 10f};
            table.setWidths(columnWidths);

            // Table headers
            Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.WHITE);
            String[] headerTitles = {"Course", "Course Title", "Credit", "Midterm", "Final", "Assignment", "Grade", "Grade Pt"};

            for (String headerTitle : headerTitles) {
                PdfPCell header = new PdfPCell(new Phrase(headerTitle, tableHeaderFont));
                header.setBackgroundColor(BaseColor.DARK_GRAY);
                header.setHorizontalAlignment(Element.ALIGN_CENTER);
                header.setPadding(6);
                table.addCell(header);
            }

            // Table data - CONNECTED: Loop through enrollments
            Font tableCellFont = FontFactory.getFont(FontFactory.HELVETICA, 9);

            for (CourseEnrollment enrollment : enrollments) {
                // CALLING enrollment methods - Get all scores
                String courseID = enrollment.getCourseID();
                double midtermScore = enrollment.getMidtermTestGradePoint();
                double finalScore = enrollment.getFinalTestGradePoint();
                double assignmentScore = enrollment.getAssignmentGradePoint();
                String overallGrade = enrollment.getOverallGrade();
                double gradePoint = enrollment.getOverallGradePoint();

                // CONNECTED: Get course details
                Course course = getCourseFromRepository(courseID);

                if (course != null) {
                    table.addCell(new PdfPCell(new Phrase(courseID, tableCellFont)));
                    table.addCell(new PdfPCell(new Phrase(course.getCourseName(), tableCellFont)));
                    table.addCell(new PdfPCell(new Phrase(String.valueOf(course.getCreditHours()), tableCellFont)));
                    table.addCell(new PdfPCell(new Phrase(String.format("%.2f", midtermScore), tableCellFont)));
                    table.addCell(new PdfPCell(new Phrase(String.format("%.2f", finalScore), tableCellFont)));
                    table.addCell(new PdfPCell(new Phrase(String.format("%.2f", assignmentScore), tableCellFont)));
                    table.addCell(new PdfPCell(new Phrase(overallGrade, tableCellFont)));
                    table.addCell(new PdfPCell(new Phrase(String.format("%.2f", gradePoint), tableCellFont)));
                }
            }

            document.add(table);

            // Add CGPA - CONNECTED: Using Student's CGPA
            document.add(new Paragraph(" "));
            Paragraph cgpaText = new Paragraph(
                    String.format("Cumulative GPA (CGPA): %.2f", student.getCGPA()),
                    headerFont
            );
            document.add(cgpaText);

            // Add statistics - CONNECTED: Using Student methods
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Academic Statistics:", headerFont));
            document.add(new Paragraph("Total Credit Hours: " + student.getTotalCreditHours(), normalFont));
            document.add(new Paragraph("Total Grade Points: " + String.format("%.2f", student.getTotalGradePoints()), normalFont));
            document.add(new Paragraph("Failed Courses: " + student.getFailedCourses().size(), normalFont));

            // Footer
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Report ID: " + reportID, normalFont));
            document.add(new Paragraph("Generated on: " + java.time.LocalDate.now(), normalFont));

            System.out.println("PDF report successfully exported to: " + filePath);

        } catch (DocumentException | IOException e) {
            System.err.println("Error exporting PDF: " + e.getMessage());
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    public void addCourseEnrollment(CourseEnrollment enrollment) {
        this.enrollments.add(enrollment);
    }

    public String formatReportData() {
        return String.format("Report[ID=%s, Student=%s, Semester=%d, Year=%s, CGPA=%.2f, Courses=%d]",
                reportID, student.getStudentName(), semester, year, cgpa, enrollments.size());
    }
}
