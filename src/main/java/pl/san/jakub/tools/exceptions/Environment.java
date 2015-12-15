package pl.san.jakub.tools.exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Jakub on 12.12.2015.
 */
public class Environment {

    private static final Logger LOGGER = LoggerFactory.getLogger(Environment.class);

    public static String readProperty(String propertyName) {
        Properties properties = new Properties();
        try {
            String path = ClassLoader.getSystemResource("").getPath()
                    .replace("target/classes/", "conf/server.properties");
            if(path.contains("jar")) {
                path = ClassLoader.getSystemResource("").getPath()
                        .replace("server-reservation-1.0-SNAPSHOT-compile-jar-with-dependencies.jar!/",
                                "conf/server.properties").replace("file:/", "");
            }
            LOGGER.info("Searching in path: "+path);
            readFile(properties, path);
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
