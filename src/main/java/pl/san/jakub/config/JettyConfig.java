package pl.san.jakub.config;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;
import java.io.IOException;
import java.util.EnumSet;

/**
 * Created by Jakub on 09.11.2015.
 */
public class JettyConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${jetty.port:8080}")
    private int jettyPort;

    @Bean
    public WebAppContext jettyWebAppContext() throws IOException {


        WebAppContext ctx = new WebAppContext();
        ctx.setContextPath("/");
        // add spring security
        ctx.addFilter(new FilterHolder( new DelegatingFilterProxy( "springSecurityFilterChain" ) ),"/*", EnumSet.allOf( DispatcherType.class ));
        // end add spring security
        ctx.setWar(new ClassPathResource("webapp").getURI().toString());

        ctx.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed",
                "false");

        GenericWebApplicationContext webApplicationContext =
                new GenericWebApplicationContext();
        webApplicationContext.setParent(applicationContext);
        webApplicationContext.refresh();
        ctx.setAttribute(
                WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
                webApplicationContext);


        ctx.addEventListener(new ReservationWebInit());

        return ctx;
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server jettyServer() throws IOException {


        Server server = new Server();
        ServerConnector httpConnector = new ServerConnector(server);
        httpConnector.setPort(jettyPort);
        server.addConnector(httpConnector);
        server.setHandler(jettyWebAppContext());

        return server;
    }

}
