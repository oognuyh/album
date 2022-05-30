package com.study.album.security;

import com.study.album.error.ErrorCode;
import com.study.album.exception.SecurityException;
import com.study.album.model.User;
import com.study.album.repository.UserRepository;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LocalUserAuthenticationProvider implements AuthenticationProvider {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String email = (String) authentication.getPrincipal();
    String password = (String) authentication.getCredentials();

    User user =
        userRepository
            .getUserByEmail(email)
            .orElseThrow(() -> new SecurityException(ErrorCode.WRONG_EMAIL_OR_PASSWORD));

    if (passwordEncoder.matches(password, user.getPassword())) {
      return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    } else {
      throw new SecurityException(ErrorCode.WRONG_EMAIL_OR_PASSWORD);
    }
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return clazz.equals(UsernamePasswordAuthenticationToken.class);
  }
}
