package pl.san.jakub.starter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.context.support.GenericWebApplicationContext;
import pl.san.jakub.config.RootConfig;

/**
 * Created by Jakub on 09.11.2015.
 */
public class Main {


    static boolean webApplicationContextInitialized = false;

    public static void main(String[] args) throws Exception {

        final Logger logger = LoggerFactory.getLogger("main");
        logger.info("---------------------------------------------------------");
        logger.info("-                                                       -");
        logger.info("-                  STARTING JETTY SERVER                -");
        logger.info("-                                                       -");
        logger.info("-                                                       -");
        logger.info("-                                                       -");
        logger.info("-                                                       -");
        logger.info("                 SERVER RESERVATION SYSTEM              -");
        logger.info("-                                                       -");
        logger.info("-                         AUTHOR:                       -");
        logger.info("-                    JAKUB PUSTELNIK                    -");
        logger.info("-                                                       -");
        logger.info("-                                                       -");
        logger.info("-                                                       -");
        logger.info("---------------------------------------------------------");

        try {
            AnnotationConfigApplicationContext applicationContext =
                    new AnnotationConfigApplicationContext();

            applicationContext
                    .addApplicationListener(
                            new ApplicationListener<ContextRefreshedEvent>() {
                                @Override
                                public void onApplicationEvent(
                                        ContextRefreshedEvent event) {
                                    ApplicationContext ctx =
                                            event.getApplicationContext();
                                    if (ctx instanceof GenericWebApplicationContext) {
                                        webApplicationContextInitialized = true;
                                    }
                                }
                            });

            applicationContext.registerShutdownHook();
            applicationContext.register(RootConfig.class);
            applicationContext.refresh();

            if (!webApplicationContextInitialized) {
                logger.error("Failed to initialize web application.  Exiting.");
                System.exit(1);
            }

            logger.info("Running.");
        } catch (Exception e) {
            logger.error("Error starting application", e);
            System.exit(1);
        }
    }
}
