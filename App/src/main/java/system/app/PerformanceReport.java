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
        
        // Header section - ✅ USING Student object methods
        report.append("=".repeat(80)).append("\n");
        report.append("           ACADEMIC PERFORMANCE REPORT\n");
        report.append("=".repeat(80)).append("\n\n");
        
        // Student Information - ✅ CONNECTED to Student class
        report.append("Student Name: ").append(student.getName()).append("\n");
        report.append("Student ID: ").append(student.getStudentID()).append("\n");
        report.append("Program: ").append(student.getProgram()).append("\n");
        report.append("Semester: ").append(semester).append("\n");
        report.append("Year: ").append(year).append("\n\n");
        
        // Course Enrollments Table Header
        report.append("-".repeat(120)).append("\n");
        report.append(String.format("%-8s %-25s %-6s %-8s %-8s %-10s %-8s %-10s\n",
            "Course", "Course Title", "Credit", "Midterm", "Final", "Assignment", "Grade", "Grade Pt"));
        report.append("-".repeat(120)).append("\n");
        
        // Course Details - ✅ CONNECTED: Using CourseEnrollment AND Course objects
        if (enrollments.isEmpty()) {
            report.append("No course enrollments found for this semester.\n");
        } else {
            for (CourseEnrollment enrollment : enrollments) {
                // ✅ CALLING CourseEnrollment methods - Get individual scores
                String courseID = enrollment.getCourseID();
                double midtermScore = enrollment.getMidtermTestGradePoint();
                double finalScore = enrollment.getFinalTestGradePoint();
                double assignmentScore = enrollment.getAssignmentGradePoint();
                String overallGrade = enrollment.getOverallGrade();
                double gradePoint = enrollment.getOverallGradePoint();
                
                // ✅ CONNECTED: Get Course object from DataRepository
                Course course = getCourseFromRepository(courseID);
                
                if (course != null) {
                    // ✅ CALLING Course methods
                    String courseName = course.getCourseName();
                    if (courseName.length() > 25) {
                        courseName = courseName.substring(0, 22) + "...";
                    }
                    int creditHours = course.getCreditHours();
                    
                    report.append(String.format("%-8s %-25s %-6d %-8.2f %-8.2f %-10.2f %-8s %.2f\n",
                        courseID, courseName, creditHours, midtermScore, finalScore, 
                        assignmentScore, overallGrade, gradePoint));
                }
            }
        }
        
        report.append("-".repeat(120)).append("\n\n");
        
        // CGPA Section - ✅ CONNECTED: Using Student's calculation
        report.append(String.format("Cumulative GPA (CGPA): %.2f\n", student.getCGPA()));
        report.append("\n");
        
        // Additional Statistics - ✅ CONNECTED: Using Student methods
        report.append("Academic Statistics:\n");
        report.append(String.format("Total Credit Hours: %d\n", student.getTotalCreditHours()));
        report.append(String.format("Total Grade Points: %.2f\n", student.getTotalGradePoints()));
        report.append(String.format("Failed Courses: %d\n", student.getFailedCourses().size()));
        
        report.append("\n");
        report.append("=".repeat(80)).append("\n");
        report.append("Report ID: ").append(reportID).append("\n");
        report.append("Generated on: ").append(java.time.LocalDate.now()).append("\n");
        report.append("=".repeat(80)).append("\n");
        
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
            
            // Add student information - ✅ CONNECTED: Using Student object
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
            
            document.add(new Paragraph("Student Information", headerFont));
            document.add(new Paragraph("Student Name: " + student.getName(), normalFont));
            document.add(new Paragraph("Student ID: " + student.getStudentID(), normalFont));
            document.add(new Paragraph("Program: " + student.getProgram(), normalFont));
            document.add(new Paragraph("Semester: " + semester, normalFont));
            document.add(new Paragraph("Year: " + year, normalFont));
            document.add(new Paragraph(" "));
            
            // Create course table - ✅ CONNECTED: Using CourseEnrollment and Course
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
            
            // Table data - ✅ CONNECTED: Loop through enrollments
            Font tableCellFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
            
            for (CourseEnrollment enrollment : enrollments) {
                // ✅ CALLING enrollment methods - Get all scores
                String courseID = enrollment.getCourseID();
                double midtermScore = enrollment.getMidtermTestGradePoint();
                double finalScore = enrollment.getFinalTestGradePoint();
                double assignmentScore = enrollment.getAssignmentGradePoint();
                String overallGrade = enrollment.getOverallGrade();
                double gradePoint = enrollment.getOverallGradePoint();
                
                // ✅ CONNECTED: Get course details
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
            
            // Add CGPA - ✅ CONNECTED: Using Student's CGPA
            document.add(new Paragraph(" "));
            Paragraph cgpaText = new Paragraph(
                String.format("Cumulative GPA (CGPA): %.2f", student.getCGPA()), 
                headerFont
            );
            document.add(cgpaText);
            
            // Add statistics - ✅ CONNECTED: Using Student methods
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Academic Statistics:", headerFont));
            document.add(new Paragraph("Total Credit Hours: " + student.getTotalCreditHours(), normalFont));
            document.add(new Paragraph("Total Grade Points: " + String.format("%.2f", student.getTotalGradePoints()), normalFont));
            document.add(new Paragraph("Failed Courses: " + student.getFailedCourses().size(), normalFont));
            
            // Footer
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Report ID: " + reportID, normalFont));
            document.add(new Paragraph("Generated on: " + java.time.LocalDate.now(), normalFont));
            
            System.out.println("✅ PDF report successfully exported to: " + filePath);
            
        } catch (DocumentException | IOException e) {
            System.err.println("❌ Error exporting PDF: " + e.getMessage());
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
            reportID, student.getName(), semester, year, cgpa, enrollments.size());
    }
}
