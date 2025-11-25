package system.app;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.*;
import java.util.*;

public class EmailPart {
    public static void sendEnrollmentEmail(Student student){
        Properties cfg = new Properties();
        try (FileInputStream in = new FileInputStream(FileUtils.getPath("config.properties"))){
            cfg.load(in);
        } catch (Exception ex){
            System.out.println("Could not load config.properties: " + ex.getMessage());
            ex.printStackTrace();
            return;
        }

        String host = cfg.getProperty("EMAIL_SMTP_HOST");
        String port = cfg.getProperty("EMAIL_SMTP_PORT");
        String username = cfg.getProperty("EMAIL_USERNAME");
        String password = cfg.getProperty("EMAIL_PASSWORD");

        if(host == null || port == null || username == null || password == null){
            System.out.println("Missing SMTP configuration in config.properties");
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        // Decide TLS vs SSL depending on port
        if(port.equals("465")){
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
        } catch (MessagingException e){
            System.out.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e){
            System.out.println("Unexpected error sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
