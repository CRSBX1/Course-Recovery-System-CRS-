/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system.app;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.*;
import java.util.*;

public class EmailPart {

    public static void sendEnrollmentEmail(Student student) {
        Properties cfg = new Properties();
        try (FileInputStream in = new FileInputStream("EmailConfiguration/config.properties")) {
            cfg.load(in);
        } catch (Exception ex) {
            System.out.println("Could not load config.properties: " + ex.getMessage());
            ex.printStackTrace();
            return;
        }

        String host = cfg.getProperty("EMAIL_SMTP_HOST");
        String port = cfg.getProperty("EMAIL_SMTP_PORT");
        String username = cfg.getProperty("EMAIL_USERNAME");
        String password = cfg.getProperty("EMAIL_PASSWORD");

        if (host == null || port == null || username == null || password == null) {
            System.out.println("Missing SMTP configuration in config.properties");
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        // Decide TLS vs SSL depending on port
        if (port.equals("465")) {
            props.put("mail.smtp.socketFactory.port", port);
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.ssl.enable", "true");
        } else {
            props.put("mail.smtp.starttls.enable", "true");
        }
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        session.setDebug(true);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(student.getEmail()));
            message.setSubject("Enrollment Confirmation");
            String body = "Dear Student,\n\nYou have been enrolled to the next semester\n\nBest regards,\nAsia Pacific University";
            message.setText(body);
            Transport.send(message);
            System.out.println("Email sent to " + student.getEmail());
        } catch (MessagingException e) {
            System.out.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Unexpected error sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void sendReportWithAttachment(String toEmail, String subject,
            String textBody, String pdfPath)
            throws MessagingException, IOException {

        // Load email configuration
        Properties cfg = new Properties();
        try (FileInputStream in = new FileInputStream("EmailConfiguration/config.properties")) {
            cfg.load(in);
        } catch (Exception ex) {
            System.out.println("Could not load config.properties: " + ex.getMessage());
            ex.printStackTrace();
            throw new IOException("Email configuration not found: " + ex.getMessage());
        }

        String host = cfg.getProperty("EMAIL_SMTP_HOST");
        String port = cfg.getProperty("EMAIL_SMTP_PORT");
        String username = cfg.getProperty("EMAIL_USERNAME");
        String password = cfg.getProperty("EMAIL_PASSWORD");

        if (host == null || port == null || username == null || password == null) {
            throw new IOException("Missing SMTP configuration in config.properties");
        }

        // Setup SMTP properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");

        // Decide TLS vs SSL depending on port
        if (port.equals("465")) {
            props.put("mail.smtp.socketFactory.port", port);
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.ssl.enable", "true");
        } else {
            props.put("mail.smtp.starttls.enable", "true");
        }

        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");

        // Create session
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        session.setDebug(true);

        try {
            // Create message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);

            // Create multipart message (for text + attachment)
            Multipart multipart = new MimeMultipart();

            // Part 1: Text body
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(textBody, "UTF-8");
            multipart.addBodyPart(textPart);

            // Part 2: PDF attachment
            MimeBodyPart pdfPart = new MimeBodyPart();
            pdfPart.attachFile(pdfPath);
            multipart.addBodyPart(pdfPart);

            // Set content
            message.setContent(multipart);

            // Send email
            Transport.send(message);
            System.out.println("Email with attachment sent successfully to " + toEmail);

        } catch (MessagingException e) {
            System.out.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.out.println("Unexpected error sending email: " + e.getMessage());
            e.printStackTrace();
            throw new MessagingException("Failed to send email: " + e.getMessage());
        }
    }
}
