package com.example.MCM.base.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeRequests(
            authorizeRequests -> authorizeRequests
                .requestMatchers("/**")
                .permitAll()
        )
        .oauth2Login(
                oauth2Login -> oauth2Login
                        .loginPage("/member/login")
        )
        .csrf().ignoringRequestMatchers(
            new AntPathRequestMatcher("/**"))
        .and()
        .formLogin()
        .loginPage("/member/login")
        .defaultSuccessUrl("/")
        .and()
        .logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
        .logoutSuccessUrl("/")
        .invalidateHttpSession(true);


    return http.build();
  }


  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
}
