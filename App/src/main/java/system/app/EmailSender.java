package system.app;

import system.app.*;
import java.util.List;

public class EmailSender {

    /**
     * Sends the recovery plan email WITH PDF attachment
     * @param email Recipient email address
     * @param studentID The student's IDE
     * @param studentName The student's name
     * @param courseID Course ID
     * @param courseName Course name
     * @param rows Milestone data
     * @param remarks Plan description
     */
    public static void sendPlan(String email, String studentID, String studentName,
                                String courseID, String courseName, 
                                List<String[]> rows, String remarks) throws Exception {
        
        System.out.println("=== EmailSender.sendPlan() called ===");
        System.out.println("Email: " + email);
        System.out.println("Student ID: " + studentID);
        System.out.println("Student Name: " + studentName);
        System.out.println("Course ID: " + courseID);
        System.out.println("Course Name: " + courseName);
        System.out.println("Milestones: " + (rows != null ? rows.size() + " rows" : "NULL"));
        System.out.println("Remarks: " + (remarks != null ? remarks.substring(0, Math.min(50, remarks.length())) + "..." : "NULL"));
        
        // Load email service configuration
        System.out.println("Loading email configuration...");
        EmailService svc = EmailConfig.fromConfigFile("EmailConfiguration/config.properties");
        if (svc == null) {
            throw new Exception("Failed to load email configuration from config.properties");
        }
        System.out.println("Email configuration loaded successfully");
        
        String from = "aputesting780@gmail.com";
        String subject = "Your Course Recovery Plan - " + courseID + " " + courseName;
        
        // Generate email body text
        System.out.println("Generating email body...");
        String body = EmailHelper.buildRecoveryPlan(courseID, courseName, rows, remarks);
        System.out.println("Email body generated (" + body.length() + " characters)");
        
        // Create the PDF attachment (returns absolute path)
        System.out.println("=== Creating PDF attachment ===");
        System.out.println("Calling EmailHelper.createPlanAttachmentPDF()...");
        
        String attachmentPath = null;
        try {
            attachmentPath = EmailHelper.createPlanAttachmentPDF(
                studentID,
                studentName,
                courseID, 
                courseName, 
                rows, 
                remarks
            );
            System.out.println("✓ PDF created successfully at: " + attachmentPath);
        } catch (Exception e) {
            System.err.println("✗ ERROR creating PDF: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Failed to create PDF attachment: " + e.getMessage(), e);
        }
        
        // Check if PDF file actually exists
        java.io.File pdfFile = new java.io.File(attachmentPath);
        if (!pdfFile.exists()) {
            throw new Exception("PDF file was not created at: " + attachmentPath);
        }
        System.out.println("✓ PDF file exists, size: " + pdfFile.length() + " bytes");
        
        // Send email WITH PDF attachment
        System.out.println("=== Sending email with attachment ===");
        System.out.println("From: " + from);
        System.out.println("To: " + email);
        System.out.println("Subject: " + subject);
        System.out.println("Attachment: " + attachmentPath);
        
        try {
            svc.sendWithAttachment(from, "Asia Pacific University", email, subject, body, attachmentPath);
            System.out.println("✓ Email sent successfully!");
        } catch (Exception e) {
            System.err.println("✗ ERROR sending email: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Failed to send email: " + e.getMessage(), e);
        }
        
        System.out.println("=== Recovery plan email process completed ===");
        
        // Optional: Clean up temp file after sending
        // Uncomment the line below if you want to delete the PDF after sending
        // new java.io.File(attachmentPath).delete();
    }
}