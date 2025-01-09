package com.ocr.paymybuddy.PayMyBuddy.configuration;

import com.ocr.paymybuddy.PayMyBuddy.filter.JwtFilter;
import com.ocr.paymybuddy.PayMyBuddy.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
//@RequiredArgsConstructor
public class SecurityConfig {


    private JwtUtils jwtUtils;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public JwtFilter jwtFilter(UserService userService, JwtUtils jwtUtils) {
        return new JwtFilter(userService, jwtUtils);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        return http.authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/login", "/register")
                            .permitAll();
                    auth.anyRequest()
                            .authenticated();
                })
                .formLogin(form -> {
                    form.loginPage("/login");
                    form.usernameParameter("email");
                    form.passwordParameter("password");
                    form.successForwardUrl("/register");
                    form.failureUrl("/login?error");

                })
                .logout(logout -> {
                    logout.logoutUrl("/logout");
                    logout.logoutSuccessUrl("/login?logout");
                    logout.invalidateHttpSession(true);
                    logout.deleteCookies("JSESSIONID");

                })

                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
                    httpSecurityExceptionHandlingConfigurer.accessDeniedPage("/accessDenied");
                })
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


//
//    Aller au contenu
//    Choisir la langue
//    Aller à la recherche
//
//    Sorry, Web RTC is not available in your browser
//    Sorry, Web RTC is not available in your browser
//    Vous
//    Sorry, Web RTC is not available in your browser
//    Abdoulaye Wane
//19:56
//    Session de mentorat
//            Chat
//    Abdoulaye Wane19:51
//    https://javatechonline.com/spring-boot-thymeleaf-security-login-example/
//    Abdoulaye Wane19:55
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        return http.authorizeHttpRequests(auth -> {
//                    auth.requestMatchers("/css/*")
//                            .permitAll();
//                    auth.requestMatchers("/register", "/registerSuccess")
//                            .permitAll();
//                    auth.requestMatchers("/authenticated/*")
//                            .hasRole("USER");
//                    auth.anyRequest()
//                            .authenticated();
//                })
//                .formLogin(form -> {
//                    form.loginPage("/login");
//                    form.usernameParameter("email");
//                    form.passwordParameter("password");
//                    form.failureUrl("/login?error");
//                    form.permitAll();
//                })
//                .rememberMe(remember -> {
//                    remember.rememberMeParameter("remember-me");
//                    remember.key("its-me-key");
//                    remember.rememberMeCookieName("remember-me-cookie");
//                    remember.useSecureCookie(true);
//                })
//                .logout(logout -> {
//                    logout.logoutUrl("/logout");
//                    logout.logoutSuccessUrl("/login?logout");
//                    logout.invalidateHttpSession(true);
//                    logout.deleteCookies("JSESSIONID");
//                    logout.permitAll();
//                })
//                .build();
//    }
//    Votre message


}
