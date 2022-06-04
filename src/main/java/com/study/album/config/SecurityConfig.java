package com.study.album.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.album.error.ErrorCode;
import com.study.album.error.ErrorResponse;
import com.study.album.exception.SecurityException;
import com.study.album.repository.UserRepository;
import com.study.album.security.JwtAuthenticationFilter;
import com.study.album.security.JwtExceptionFilter;
import com.study.album.security.LocalUserAuthenticationProvider;
import java.io.PrintWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserRepository userRepository;

  private final ObjectMapper objectMapper;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .httpBasic()
        .disable()
        .formLogin()
        .disable()
        .authorizeRequests()
        .anyRequest()
        .authenticated()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint())
        .accessDeniedHandler(accessDeniedHandler())
        .and()
        .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(new JwtExceptionFilter(objectMapper), JwtAuthenticationFilter.class);
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring()
        .antMatchers("/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
        .regexMatchers(HttpMethod.GET, "/users(?!/me$)(/.*|[?].*)?$")
        .antMatchers(HttpMethod.GET, "/images/**", "/posts/**", "/comments/**")
        .antMatchers(HttpMethod.POST, "/auth/login");
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
