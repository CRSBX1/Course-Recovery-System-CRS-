/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.app;

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
    private String year; // Should be integer? the class diagram shows year..
    private List<CourseEnrollment> enrollments;
    private double cgpa;

    // Constructor
    public PerformanceReport(String reportID, Student student, int semester, String year) {
        this.reportID = reportID;
        this.student = student;
        this.semester = semester;
        this.year = year;
        this.enrollments = new ArrayList<>();
        this.cgpa = 0.0;
    }

    // Getters and Setters
    public String getReportID() {
        return reportID;

    }

    public void setReportID(String reportID) {
        this.reportID = reportID;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<CourseEnrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<CourseEnrollment> enrollments) {
        this.enrollments = enrollments;
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    // Method
    public double calculateCGPA() {
        // Calculate cgpa code
        return this.cgpa;
    }

    public String generateReport() {
        return "";
    }

    public void exportToPDF(String filePath) {
        // PDF export implementation would require a library like iText or Apache PDFBox
        System.out.println("Exporting report to PDF: ");
        // To do: Implement actual PDF export functionality
    }

    public void addCourseEnrollment(CourseEnrollment enrollment) {
        this.enrollments.add(enrollment);
    }

    public String formatReportData() {
        // To do: Implement report data formatting later
        return "";
    }
}
