package com.study.album.security;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.study.album.model.User;
import com.study.album.util.SecurityUtil;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (Objects.isNull(accessToken)) {
      log.info("No access token in header. Proceed as anonymous user");
    } else if (SecurityUtil.containsPrefix(accessToken)) {
      accessToken = SecurityUtil.removePrefix(accessToken);

      log.info("Found the access token({})", accessToken);

      User user = SecurityUtil.getUserIfTokenIsValid(accessToken);

      log.info("Successfully verified");

      SecurityContextHolder.getContext()
          .setAuthentication(
              new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));

      log.info("Set authentication to SecurityContextHolder");
    } else {
      log.info("Found the access token. But, can't find the token prefix");
    }

    filterChain.doFilter(request, response);
  }
}
