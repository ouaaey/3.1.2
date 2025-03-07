package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandler successUserHandler;
    private final UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN") // доступ только ADMIN
                .antMatchers("/user/**").hasRole("USER")   // доступ только USER
                .antMatchers("/login").permitAll()         // доступна всем
                .anyRequest().authenticated()              // запросы требуют аутентификации
                .and()
                .formLogin()
                .loginPage("/login")                       //  вход
                .defaultSuccessUrl("/default")             // перенаправление после входа
                .failureUrl("/login?error=true")           // перенаправление при ошибке аутентификации
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/login")               // перенаправление после выхода
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin@example.ru")
                .password(passwordEncoder().encode("admin")) // пароль1
                .roles("ADMIN")
                .and()
                .withUser("user@example.ru")
                .password(passwordEncoder().encode("user"))
                .roles("USER");
    }

    @Autowired
    public WebSecurityConfig(SuccessUserHandler successUserHandler, UserDetailsService userDetailsService) {
        this.successUserHandler = successUserHandler;
        this.userDetailsService = userDetailsService;
    }


    @GetMapping("/default")
    public String defaultAfterLogin(HttpServletRequest request) {
        if (request.isUserInRole("ADMIN")) {
            return "redirect:/admin";
        } else if (request.isUserInRole("USER")) {
            return "redirect:/user";
        }
        return "redirect:/login";
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}