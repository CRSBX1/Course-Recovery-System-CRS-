package system.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmailConfiguration {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = EmailConfiguration.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException("config.properties not found in resources folder");
            }

            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
