package system.app;

import java.util.List;
import java.util.Map;
import javax.swing.*;
import java.awt.*;

public class RecoveryPlanEmailer {

    /**
     * Sends the recovery plan via email in a background SwingWorker thread.
     * It looks up the student's email using the studentId.
     * 
     * @param studentId The ID of the student to send the plan to.
     * @param courseID The ID of the failed course.
     * @param courseName The name of the failed course.
     * @param description The textual description/remarks for the plan.
     * @param milestones A list of String arrays containing the milestone details (TaskID, Week, Task).
     * @param parent The parent frame for dialogs.
     */
    public static void sendPlanToStudent(String studentId, String courseID, String courseName, 
                                         String description, List<String[]> milestones, Frame parent) {

        System.out.println("=== RecoveryPlanEmailer DEBUG ===");
        System.out.println("Student ID: " + studentId);
        System.out.println("Course ID: " + courseID);
        System.out.println("Course Name: " + courseName);
        System.out.println("Description: " + description);
        System.out.println("Milestones count: " + (milestones != null ? milestones.size() : "NULL"));
        
        // Get student from DataRepository
        Student student = DataRepository.findStudentByID(studentId);
        
        if (student == null) {
            System.err.println("ERROR: Student not found in DataRepository!");
            JOptionPane.showMessageDialog(parent, 
                "Student not found: " + studentId,
                "Student Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String studentEmail = student.getEmail();
        String studentName = student.getStudentName();
        
        System.out.println("Student Name: " + studentName);
        System.out.println("Student Email: " + studentEmail);

        if (studentEmail == null || studentEmail.isEmpty()) {
            System.err.println("ERROR: No email found for student!");
            JOptionPane.showMessageDialog(parent, 
                "No email found for student " + studentName + " (" + studentId + ")",
                "Email Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        System.out.println("Starting email send process...");

        // Use SwingWorker to prevent freezing the UI while sending email
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            Exception err;
            
            @Override
            protected Void doInBackground() {
                try {
                    System.out.println("=== Inside SwingWorker - Calling EmailSender.sendPlan() ===");
                    
                    // IMPORTANT: All 7 parameters must be passed in correct order!
                    EmailSender.sendPlan(
                        studentEmail,    // 1. to email
                        studentId,       // 2. student ID (for PDF filename)
                        studentName,     // 3. student name (for PDF content)
                        courseID,        // 4. course ID
                        courseName,      // 5. course name
                        milestones,      // 6. milestone data
                        description      // 7. plan description/remarks
                    );
                    
                    System.out.println("EmailSender.sendPlan() completed successfully!");
                    
                } catch (Exception e) {
                    System.err.println("ERROR in doInBackground: " + e.getMessage());
                    e.printStackTrace();
                    err = e;
                }
                return null;
            }
            
            @Override
            protected void done() {
                // Handle results on the Event Dispatch Thread
                if (err != null) {
                    System.err.println("=== EMAIL SENDING FAILED ===");
                    err.printStackTrace();
                    JOptionPane.showMessageDialog(parent, 
                        "Failed to send email: " + err.getMessage() + 
                        "\n\nPlease check:\n" +
                        "1. Email configuration in EmailConfiguration/config.properties\n" +
                        "2. Internet connection\n" +
                        "3. Student email address is valid\n" +
                        "4. iText PDF library is in classpath",
                        "Email Error",
                        JOptionPane.ERROR_MESSAGE);
                } else {
                    System.out.println("=== EMAIL SENT SUCCESSFULLY ===");
                    // Show success dialog
                    SuccessDialog sd = new SuccessDialog(parent,
                        "Successfully Submitted",
                        "The recovery plan has been created and sent to " + studentName + 
                        "'s email (" + studentEmail + ") with a PDF attachment.");
                    sd.setVisible(true);
                }
            }
        };
        
        worker.execute(); // Start the background thread
    }
}