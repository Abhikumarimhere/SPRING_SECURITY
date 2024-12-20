package com.Abhishek.Client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {
private static final String[] WHITE_LIST_URLS={
        "/register",
        "/hello",
        "/VerifyRegistration" ,
        "/resend",
        "/ResetPassword",
        "/SavePassword",
        "/changePassword"
};

    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder(11);
    }
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf->csrf.disable())
                .cors(cors->cors.disable())
                .authorizeHttpRequests((authz)-> authz
                        .requestMatchers(WHITE_LIST_URLS).permitAll()
                        .anyRequest().authenticated());
        return httpSecurity.build();
    }
}
