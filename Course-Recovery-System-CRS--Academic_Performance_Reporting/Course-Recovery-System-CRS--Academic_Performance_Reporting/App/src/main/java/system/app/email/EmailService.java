package system.app.email;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.activation.*;
import java.util.Properties;
import java.io.File;
import java.io.IOException;

public class EmailService {

    private final String host;
    private final String port;
    private final String user;
    private final String pass;

    public EmailService(String host, String port, String user, String pass) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.pass = pass;
    }

    public static EmailService fromEnv() {
        String h = System.getenv().getOrDefault("EMAIL_SMTP_HOST", "smtp.gmail.com");
        String p = System.getenv().getOrDefault("EMAIL_SMTP_PORT", "587");
        String u = System.getenv().get("EMAIL_USERNAME");
        String pw = System.getenv().get("EMAIL_PASSWORD");
        return new EmailService(h, p, u, pw);
    }

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
        } catch (Exception ex) {
            msg.setFrom(new InternetAddress(fromAddr));
        }
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddr, false));
        msg.setSubject(subject);
        msg.setText(body);
        Transport.send(msg);
    }

    public void sendWithAttachment(String fromAddr, String fromName, String toAddr, String subject, String body, File file)
            throws MessagingException, IOException {

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
        } catch (Exception ex) {
            msg.setFrom(new InternetAddress(fromAddr));
        }
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddr, false));
        msg.setSubject(subject);

        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(body);

        MimeBodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.attachFile(file);

        Multipart mp = new MimeMultipart();
        mp.addBodyPart(textPart);
        mp.addBodyPart(attachmentPart);

        msg.setContent(mp);

        Transport.send(msg);
    }
}
