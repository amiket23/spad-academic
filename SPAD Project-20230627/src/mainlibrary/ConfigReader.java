package mainlibrary;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class ConfigReader {
    private static Properties properties;

    static {
        properties = new Properties();

        try (FileInputStream fis = new FileInputStream("config.ini")) {
            properties.load(fis);
        } catch (IOException e) {
            Logger.getLogger(ConfigReader.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
    }

    public static String getUsername() {
        return properties.getProperty("username");
    }

    public static String getPassword() {
        return properties.getProperty("password");
    }
    
    public static String getHost() {
        return properties.getProperty("host");
    }
    
    public static String getPort() {
        return properties.getProperty("port");
    }
    
}
