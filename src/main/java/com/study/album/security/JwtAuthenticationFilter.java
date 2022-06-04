package com.study.album.security;

import com.study.album.domain.User;
import com.study.album.util.SecurityUtil;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (!Objects.isNull(accessToken) && SecurityUtil.containsPrefix(accessToken)) {
      accessToken = SecurityUtil.removePrefix(accessToken);

      log.info("Found the access token({})", accessToken);

      Optional<User> userWrapper = SecurityUtil.getUserIfTokenIsValid(accessToken);
      userWrapper.ifPresent(
          (user) -> {
            log.info("Successfully verified");

            SecurityContextHolder.getContext()
                .setAuthentication(
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));

            log.info("Set authentication to SecurityContextHolder");
          });
    } else {
      log.info("No valid access token in header. Proceed as anonymous user");
    }

    filterChain.doFilter(request, response);
  }
}
