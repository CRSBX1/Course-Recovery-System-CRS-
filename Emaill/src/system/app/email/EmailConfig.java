package system.app.email;

import java.io.*;
import java.util.Properties;

/* config loader */
public class EmailConfig {

    /* from file */
    public static EmailService fromConfigFile(String path) {
        Properties p = new Properties();
        try (InputStream in = new FileInputStream(path)) {
            p.load(in);
        } catch (Exception e) {
            return null;
        }
        String host = p.getProperty("EMAIL_SMTP_HOST", "smtp.gmail.com");
        String port = p.getProperty("EMAIL_SMTP_PORT", "587");
        String user = p.getProperty("EMAIL_USERNAME");
        String pass = p.getProperty("EMAIL_PASSWORD");
        return new EmailService(host, port, user, pass);
    }
}
