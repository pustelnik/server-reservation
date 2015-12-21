package pl.san.jakub.tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

/**
 * Created by Jakub on 12.12.2015.
 */
public class Environment {

    private static final Logger LOGGER = LoggerFactory.getLogger(Environment.class);
    private static final String PATH;
    public static final String CONF_SERVER_PROPERTIES = "conf/server.properties";

    static {
        PATH = getPath(ClassLoader.getSystemResource("").getPath());
        LOGGER.info("Searching in path: "+PATH);
    }
    private Environment() {};

    public static void main(String[] args) {
        String path = ClassLoader.getSystemResource("").getPath();
        String[] temp = path.split("/");
        System.out.println(Arrays.toString(temp));
    }
    private static String getPath(String path) {
        String[] temp = path.split("/");
        StringBuilder result = new StringBuilder();
        if(path.contains("jar")) {
            for(int i = 0; i < temp.length - 1; i++) {
                result.append(temp[i]).append("/");
            }
            result.append(CONF_SERVER_PROPERTIES);
            return result.toString().replace("file:/", "");
        } else {
            for (int i = 0; i < temp.length - 2; i++) {
                result.append(temp[i]).append("/");
            }
            result.append(CONF_SERVER_PROPERTIES);
            return result.toString();
        }
    }

    public static String readProperty(String propertyName) {
        Properties properties = new Properties();
        try {
            readFile(properties, PATH);
        }
        catch (NullPointerException e) {
            LOGGER.info(e.getMessage());
            LOGGER.info("File not found at path {}.",ClassLoader.getSystemResource("").getPath());

        }

        return properties.getProperty(propertyName);
    }

    private static void readFile(Properties properties, String path) {
        try(FileInputStream file = new FileInputStream(path)) {
            properties.load(file);
        } catch (FileNotFoundException e) {
            LOGGER.info(e.getMessage());
            LOGGER.info("File not found at path {}.",path);
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
        }
    }


}
