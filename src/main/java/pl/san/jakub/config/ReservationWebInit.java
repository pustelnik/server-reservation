package pl.san.jakub.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;

/**
 * Created by Jakub on 07.11.2015.
 */

public class ReservationWebInit extends
        AbstractAnnotationConfigDispatcherServletInitializer implements
        ServletContextListener{
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] {RootConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] {WebMvcConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            onStartup(sce.getServletContext());
        } catch (ServletException e) {
            logger.error("Failed to initialize web application", e);
            System.exit(0);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // empty method to show context is destroyed notification
    }

    @Override
    protected void registerContextLoaderListener(ServletContext servletContext) {
        // registers web application context
    }
}
