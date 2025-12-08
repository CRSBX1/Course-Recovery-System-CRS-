package system.app;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.activation.*; // NEW IMPORT for file handling
import java.util.Properties;
import java.io.File; // NEW IMPORT for file handling

/* email service */
public class EmailService {

    /* fields */
    private final String host;
    private final String port;
    private final String user;
    private final String pass;

    /* ctor */
    public EmailService(String host, String port, String user, String pass) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.pass = pass;
    }

    /* from env */
    public static EmailService fromEnv() {
        String h = System.getenv().getOrDefault("EMAIL_SMTP_HOST", "smtp.gmail.com");
        String p = System.getenv().getOrDefault("EMAIL_SMTP_PORT", "587");
        String u = System.getenv().get("EMAIL_USERNAME");
        String pw = System.getenv().get("EMAIL_PASSWORD");
        return new EmailService(h, p, u, pw);
    }

    /* send text (existing) */
    public void sendPlainText(String fromAddr, String fromName, String toAddr, String subject, String body) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });

        Message msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(fromAddr, fromName));
        } catch (java.io.UnsupportedEncodingException ex) {
            msg.setFrom(new InternetAddress(fromAddr));
        }
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddr, false));
        msg.setSubject(subject);
        msg.setText(body); 

        Transport.send(msg);
    }

    /**
     * Sends an email with a plain text body and a single file attachment.
     */
    public void sendWithAttachment(String fromAddr, String fromName, String toAddr, String subject, String body, String filePath) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });

        Message msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(fromAddr, fromName));
        } catch (java.io.UnsupportedEncodingException ex) {
            msg.setFrom(new InternetAddress(fromAddr));
        }
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddr, false));
        msg.setSubject(subject);

        // Create the multipart message
        MimeMultipart multipart = new MimeMultipart();

        // 1. Text body part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(body);
        multipart.addBodyPart(messageBodyPart);

        // 2. Attachment part
        if (filePath != null && !filePath.isEmpty()) {
            File file = new File(filePath);
            if (file.exists()) {
                MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(file);
                attachmentBodyPart.setDataHandler(new DataHandler(source));
                attachmentBodyPart.setFileName(file.getName());
                multipart.addBodyPart(attachmentBodyPart);
            }
        }

        // Put parts in message
        msg.setContent(multipart);

        // Send the message
        Transport.send(msg);
    }
}