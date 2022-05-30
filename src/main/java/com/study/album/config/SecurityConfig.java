package com.study.album.config;

import java.io.PrintWriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.album.error.ErrorCode;
import com.study.album.error.ErrorResponse;
import com.study.album.exception.SecurityException;
import com.study.album.repository.UserRepository;
import com.study.album.security.JwtAuthenticationFilter;
import com.study.album.security.LocalUserAuthenticationProvider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  private final UserRepository userRepository;

  private final ObjectMapper objectMapper;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .authorizeRequests()
        .antMatchers("/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
        .permitAll()
        .antMatchers(HttpMethod.POST, "/users")
        .permitAll()
        .antMatchers("/posts")
        .permitAll()
        .antMatchers(HttpMethod.POST, "/auth/login")
        .permitAll()
        .antMatchers(HttpMethod.GET, "/images/**")
        .permitAll()
        .antMatchers(HttpMethod.POST, "/images")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint())
        .accessDeniedHandler(accessDeniedHandler())
        .and()
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(
        new LocalUserAuthenticationProvider(userRepository, passwordEncoder()));
  }

  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  AuthenticationEntryPoint authenticationEntryPoint() {
    return (request, response, exception) -> {
      ResponseEntity<ErrorResponse> result =
          ErrorResponse.from(new SecurityException(ErrorCode.NEED_AUTHENTICATION));
      PrintWriter out = response.getWriter();

      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setStatus(result.getStatusCodeValue());

      out.write(objectMapper.writeValueAsString(result.getBody()));
    };
  }

  @Bean
  AccessDeniedHandler accessDeniedHandler() {
    return (request, response, exception) -> {
      ResponseEntity<ErrorResponse> result =
          ErrorResponse.from(new SecurityException(ErrorCode.NO_PERMISSION));
      PrintWriter out = response.getWriter();

      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setStatus(result.getStatusCodeValue());

      out.write(objectMapper.writeValueAsString(result.getBody()));
    };
  }
}
