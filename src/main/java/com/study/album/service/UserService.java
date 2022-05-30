package com.study.album.service;

import java.util.UUID;

import com.study.album.dto.request.LogInRequest;
import com.study.album.dto.request.RegisterUserRequest;
import com.study.album.dto.response.TokenResponse;
import com.study.album.dto.response.UserResponse;
import com.study.album.error.ErrorCode;
import com.study.album.exception.NotFoundException;
import com.study.album.exception.SecurityException;
import com.study.album.model.User;
import com.study.album.model.UserSummary;
import com.study.album.repository.UserRepository;
import com.study.album.util.SecurityUtil;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final AuthenticationManager authenticationManager;

  @Transactional(readOnly = true)
  public Page<UserResponse> getUserSummaries(String q, Pageable pageable) {
    log.debug("Received the query term({}) and pageable({})", q, pageable);

    return userRepository.getUserSummaries(q, pageable).map(UserResponse::from);
  }

  @Transactional(readOnly = true)
  public UserResponse getUserSummaryByUserId(UUID userId) {
    UserSummary userSummary =
        userRepository
            .getUserSummaryByUserId(userId)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        ErrorCode.USER_NOT_FOUND,
                        "There is no matching user with user id " + userId));

    return UserResponse.from(userSummary);
  }

  @Transactional(readOnly = true)
  public UserResponse getUserByUserId(UUID userId) {
    User user =
        userRepository
            .getUserByUserId(userId)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        ErrorCode.USER_NOT_FOUND,
                        "There is no matching user with user id " + userId));

    return UserResponse.from(user);
  }

  @Transactional(readOnly = true)
  public TokenResponse login(LogInRequest signInRequest) {
    try {
      log.debug("Received a signIn request({})", signInRequest);

      User user =
          (User)
              authenticationManager.authenticate(
                  new UsernamePasswordAuthenticationToken(
                      signInRequest.getEmail(), signInRequest.getPassword())).getPrincipal();

      return SecurityUtil.generateTokens(user);
    } catch (BadCredentialsException e) {
      throw new SecurityException(ErrorCode.WRONG_EMAIL_OR_PASSWORD);
    } catch (DisabledException e) {
      throw new SecurityException(ErrorCode.DISABLED_USER);
    } catch (LockedException e) {
      throw new SecurityException(ErrorCode.LOCKED_USER);
    } catch (Exception e) {
      throw new SecurityException(ErrorCode.WRONG_EMAIL_OR_PASSWORD, e.getMessage());
    }
  }

  @Transactional
  public UserResponse register(RegisterUserRequest registerUserRequest) {
    User user =
        User.builder()
            .name(registerUserRequest.getName())
            .nickname(registerUserRequest.getNickname())
            .email(registerUserRequest.getEmail())
            .password(passwordEncoder.encode(registerUserRequest.getPassword()))
            .build();

    return UserResponse.from(userRepository.save(user));
  }

  public TokenResponse refreshToken() {
    return null;
  }
}
