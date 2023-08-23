package com.ltp.gradesubmission.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.ltp.gradesubmission.security.filter.AuthenticationFilter;
import com.ltp.gradesubmission.security.filter.ExceptionHandlerFilter;
import com.ltp.gradesubmission.security.filter.JWTAuthorizationFilter;
import com.ltp.gradesubmission.security.manager.CustomAuthenticationManager;

import lombok.AllArgsConstructor;

import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    CustomAuthenticationManager customAuthenticationManager;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(customAuthenticationManager);
        authenticationFilter.setFilterProcessesUrl("/authenticate");

        http
                .headers(headers -> headers.frameOptions().disable())
                .csrf(csrf -> {
                    try {
                        csrf.disable()
                                .authorizeHttpRequests(requests -> requests
                                        .antMatchers("/h2/**").permitAll()
                                        .antMatchers(HttpMethod.POST, SecurityConstants.REGISTER_PATH).permitAll()
                                        .anyRequest().authenticated()
                                        .and()
                                        .addFilterBefore(new ExceptionHandlerFilter(), AuthenticationFilter.class)
                                        .addFilter(authenticationFilter))
                                .addFilterAfter(new JWTAuthorizationFilter(), AuthenticationFilter.class)
                                .sessionManagement(management -> management
                                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                });
        return http.build();
    }

}