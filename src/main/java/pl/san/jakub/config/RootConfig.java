package pl.san.jakub.config;

import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Controller;



/**
 * Created by Jakub on 07.11.2015.
 */

@Configuration
@Import({JettyConfig.class, HibernateConfig.class, SecurityConfig.class})
@ComponentScan(basePackages = {"pl.san.jakub"},
        excludeFilters = {@ComponentScan.Filter(Controller.class),
                @ComponentScan.Filter(Configuration.class)})
public class RootConfig {


        /**
         * Allows access to properties. eg) @Value("${jetty.port}").
         */
        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            return new PropertySourcesPlaceholderConfigurer();
        }





}
