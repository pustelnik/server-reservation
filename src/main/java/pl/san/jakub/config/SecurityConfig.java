package pl.san.jakub.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

import javax.sql.DataSource;

/**
 * Created by Jakub on 14.11.2015.
 */
@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .authoritiesByUsernameQuery("select username,authority from authorities where username=?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
                    .failureUrl("/login?error")

                .and()
                    .logout()
                    .logoutSuccessUrl("/login?logout")
                .and()
                    .rememberMe()
                        .tokenRepository(new InMemoryTokenRepositoryImpl())
                        .tokenValiditySeconds(2419200)
                        .key("servKey")
                .and()
                    .httpBasic()
                    .realmName("ServerReservation")

                .and()
                    .authorizeRequests()
                        .antMatchers("/").permitAll()
                        .antMatchers(HttpMethod.POST, "/profile/register").permitAll()
                        .antMatchers("/servers/**").hasAuthority("ROLE_USER")
                        .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().permitAll();

    }
}
