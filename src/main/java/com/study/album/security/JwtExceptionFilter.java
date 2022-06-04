package com.study.album.security;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.album.error.ErrorCode;
import com.study.album.error.ErrorResponse;
import com.study.album.exception.SecurityException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

  private final ObjectMapper objectMapper;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (TokenExpiredException e) {
      ResponseEntity<ErrorResponse> result =
          ErrorResponse.from(new SecurityException(ErrorCode.EXPIRED_TOKEN));
      PrintWriter out = response.getWriter();

      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setStatus(result.getStatusCodeValue());

      out.write(objectMapper.writeValueAsString(result.getBody()));
    }
  }
}
