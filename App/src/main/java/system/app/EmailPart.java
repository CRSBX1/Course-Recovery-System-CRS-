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

    // --- Configuration and Session Helper ---

    private static class MailConfig {
        Properties props;
        String username;
        String password;
        String fromAddress;
    }

    private static MailConfig loadConfig() throws IOException {
        Properties cfg = new Properties();
        try (FileInputStream in = new FileInputStream("EmailConfiguration/config.properties")) {
            cfg.load(in);
        } catch (Exception ex) {
            throw new IOException("Could not load config.properties: " + ex.getMessage(), ex);
        }

        String host = cfg.getProperty("EMAIL_SMTP_HOST");
        String port = cfg.getProperty("EMAIL_SMTP_PORT");
        String username = cfg.getProperty("EMAIL_USERNAME");
        String password = cfg.getProperty("EMAIL_PASSWORD");

        if (host == null || port == null || username == null || password == null) {
            throw new IOException("Missing SMTP configuration in config.properties");
        }
        
        MailConfig mc = new MailConfig();
        mc.username = username;
        mc.password = password;
        mc.fromAddress = username; // Assuming the username is the default sender address

        // Setup SMTP properties
        mc.props = new Properties();
        mc.props.put("mail.smtp.auth", "true");
        // Decide TLS vs SSL depending on port
        if (port.equals("465")) {
            mc.props.put("mail.smtp.socketFactory.port", port);
            mc.props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            mc.props.put("mail.smtp.ssl.enable", "true");
        } else {
            mc.props.put("mail.smtp.starttls.enable", "true");
        }
        mc.props.put("mail.smtp.host", host);
        mc.props.put("mail.smtp.port", port);
        mc.props.put("mail.smtp.connectiontimeout", "10000");
        mc.props.put("mail.smtp.timeout", "10000");
        
        return mc;
    }

    private static Session getMailSession(MailConfig mc) {
        return Session.getInstance(mc.props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mc.username, mc.password);
            }
        });
    }

    public static void sendEnrollmentEmail(Student student) {
        try {
            MailConfig mc = loadConfig();
            Session session = getMailSession(mc);
            session.setDebug(true);

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mc.fromAddress));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(student.getEmail()));
            message.setSubject("Enrollment Confirmation");
            String body = "Dear Student,\n\nYou have been enrolled to the next semester\n\nBest regards,\nAsia Pacific University";
            message.setText(body);
            Transport.send(message);
            System.out.println("Email sent to " + student.getEmail());
        } catch (Exception e) {
            System.out.println("Error sending enrollment email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void sendReportWithAttachment(String toEmail, String subject,
            String textBody, String pdfPath) throws MessagingException, IOException {

        MailConfig mc = loadConfig();
        Session session = getMailSession(mc);
        session.setDebug(true);

        // Create message
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(mc.fromAddress));
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
    }
}