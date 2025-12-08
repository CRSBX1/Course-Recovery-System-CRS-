package system.app;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class EmailHelper {

    private static final String TEMP_BASE = "Data" + File.separator + "Temp";


    public static String buildRecoveryPlan(String courseID, String courseName, java.util.List<String[]> rows, String remarks) {
        StringBuilder sb = new StringBuilder();
        sb.append("Dear Student,\n\n");
        sb.append("Your course recovery plan has been prepared.\n\n");
        sb.append("Course: ").append(courseID).append(" - ").append(courseName).append("\n\n");

        sb.append("The detailed recovery plan with milestones is attached as a PDF file.\n\n");
        
        sb.append("Plan Overview: ").append(remarks).append("\n\n");
        
        sb.append("Please review the attached PDF for the complete milestone schedule.\n\n");
        
        sb.append("Please contact your course administrator for further details.\n\n");
        sb.append("Best regards,\nAsia Pacific University\n");

        return sb.toString();
    }
    
    /**
     * Creates a PDF file containing the recovery plan with a professional table layout
     * @param studentID The student's ID
     * @param studentName The student's name
     * @param courseID The ID of the course
     * @param courseName The name of the course
     * @param rows The milestone data (TaskID, Week, Task)
     * @param remarks The plan description
     * @return The absolute path to the created PDF file
     * @throws IOException if file writing fails
     * @throws DocumentException if PDF creation fails
     */
    public static String createPlanAttachmentPDF(String studentID, String studentName, 
                                                 String courseID, String courseName, 
                                                 java.util.List<String[]> rows, String remarks) 
            throws IOException, DocumentException {
        
        // Ensure the temp directory exists
        Files.createDirectories(Paths.get(TEMP_BASE));
        
        // Generate a unique filename
        String fileName = studentID + "_" + courseID + "_RecoveryPlan_" + System.currentTimeMillis() + ".pdf";
        String filePath = TEMP_BASE + File.separator + fileName;
        
        // Create PDF document
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();
        
        try {
            // Define fonts
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
            Font subHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.BLACK);
            Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.WHITE);
            Font tableCellFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
            
            // Add title
            Paragraph title = new Paragraph("COURSE RECOVERY PLAN", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);
            
            // Add horizontal line
            LineSeparator line = new LineSeparator();
            line.setLineColor(BaseColor.DARK_GRAY);
            document.add(new Chunk(line));
            document.add(new Paragraph(" ")); // Space
            
            // Student Information Section
            document.add(new Paragraph("Student Information", headerFont));
            document.add(new Paragraph(" ", normalFont)); // Small space
            
            document.add(new Paragraph("Student ID: " + studentID, normalFont));
            document.add(new Paragraph("Student Name: " + studentName, normalFont));
            document.add(new Paragraph(" ")); // Space
            
            // Course Information Section
            document.add(new Paragraph("Course Details", headerFont));
            document.add(new Paragraph(" ", normalFont)); // Small space
            
            document.add(new Paragraph("Course Code: " + courseID, normalFont));
            document.add(new Paragraph("Course Name: " + courseName, normalFont));
            document.add(new Paragraph(" ")); // Space
            
            // Plan Description Section
            document.add(new Paragraph("Plan Description", headerFont));
            document.add(new Paragraph(" ", normalFont)); // Small space
            
            Paragraph descPara = new Paragraph(remarks, normalFont);
            descPara.setAlignment(Element.ALIGN_JUSTIFIED);
            descPara.setSpacingAfter(15);
            document.add(descPara);
            
            // Milestones Section
            document.add(new Paragraph("Recovery Milestones", headerFont));
            document.add(new Paragraph(" ", normalFont)); // Small space
            
            // Create milestone table with 3 columns
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            table.setSpacingAfter(20);
            
            // Set column widths (Task ID: 20%, Week: 25%, Task Description: 55%)
            float[] columnWidths = {20f, 25f, 55f};
            table.setWidths(columnWidths);
            
            // Add table headers with dark background
            String[] headers = {"Task ID", "Study Week", "Task Description"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, tableHeaderFont));
                cell.setBackgroundColor(new BaseColor(41, 128, 185)); // Nice blue color
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(10);
                cell.setBorderWidth(1);
                table.addCell(cell);
            }
            
            // Add milestone data rows with alternating colors
            boolean alternate = false;
            for (String[] row : rows) {
                // Task ID cell
                PdfPCell idCell = new PdfPCell(new Phrase(row[0], tableCellFont));
                idCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                idCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                idCell.setPadding(8);
                if (alternate) {
                    idCell.setBackgroundColor(new BaseColor(236, 240, 241)); // Light gray
                }
                table.addCell(idCell);
                
                // Week cell
                PdfPCell weekCell = new PdfPCell(new Phrase(row[1], tableCellFont));
                weekCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                weekCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                weekCell.setPadding(8);
                if (alternate) {
                    weekCell.setBackgroundColor(new BaseColor(236, 240, 241));
                }
                table.addCell(weekCell);
                
                // Task description cell
                PdfPCell taskCell = new PdfPCell(new Phrase(row[2], tableCellFont));
                taskCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                taskCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                taskCell.setPadding(8);
                if (alternate) {
                    taskCell.setBackgroundColor(new BaseColor(236, 240, 241));
                }
                table.addCell(taskCell);
                
                alternate = !alternate; // Toggle for next row
            }
            
            document.add(table);
            
            // Add footer section
            document.add(new Paragraph(" ")); // Space
            document.add(new Paragraph(" ")); // Space
            
            Paragraph footer = new Paragraph("Important Notes:", subHeaderFont);
            footer.setSpacingAfter(10);
            document.add(footer);
            
            com.itextpdf.text.List notesList = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);
            notesList.add(new ListItem("Follow the milestones in the order specified", normalFont));
            notesList.add(new ListItem("Contact your lecturer if you need clarification on any task", normalFont));
            notesList.add(new ListItem("Complete all milestones before the course resit examination", normalFont));
            notesList.add(new ListItem("Regular attendance and participation are essential for success", normalFont));
            document.add(notesList);
            
            document.add(new Paragraph(" ")); // Space
            document.add(new Paragraph(" ")); // Space
            
            // Contact information
            Paragraph contact = new Paragraph("For assistance, please contact:", subHeaderFont);
            contact.setSpacingAfter(10);
            document.add(contact);
            
            document.add(new Paragraph("Academic Officer Office", normalFont));
            document.add(new Paragraph("Email: aputesting780@gmail.com", normalFont));
            document.add(new Paragraph("Phone: +60 6767 6767", normalFont));
            
            document.add(new Paragraph(" ")); // Space
            document.add(new Paragraph(" ")); // Space
            
            // Document footer
            document.add(new Chunk(line));
            Paragraph docFooter = new Paragraph(
                "Generated on: " + java.time.LocalDate.now() + " | Confidential - For student use only", 
                FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.GRAY)
            );
            docFooter.setAlignment(Element.ALIGN_CENTER);
            document.add(docFooter);
            
        } finally {
            document.close();
        }
        
        System.out.println("PDF recovery plan created at: " + filePath);
        
        // Return the absolute path
        return new File(filePath).getAbsolutePath();
    }
}