package com.ltp.gradesubmission.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import lombok.AllArgsConstructor;

import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers(headers -> headers.frameOptions().disable())
                .csrf(csrf -> {
                    try {
                        csrf.disable()
                                .authorizeHttpRequests(requests -> requests
                                        .antMatchers("/h2/**").permitAll() // New Line: allows us to access the h2
                                                                           // console without the need to authenticate.
                                                                           // ' ** ' instead of ' * ' because multiple
                                                                           // path levels will follow /h2.
                                        .anyRequest().authenticated());
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                })
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

}