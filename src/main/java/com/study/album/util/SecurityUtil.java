package com.study.album.util;

import java.util.Date;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.study.album.dto.response.TokenResponse;
import com.study.album.error.ErrorCode;
import com.study.album.exception.SecurityException;
import com.study.album.model.User;
import com.study.album.model.User.Role;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SecurityUtil {

  private static final String PREFIX = "Bearer ";

  private static final String SECRET_KEY = "albumsecretkey";

  private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);

  private static final long ACCESS_EXPIRATION_TIME = 1000 * 60 * 20;

  private static final long REFRESH_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;

  public static boolean containsPrefix(String accessToken) {
    return accessToken.matches("^(b|B)earer .+$");
  }

  public static String removePrefix(String accessToken) {
    return accessToken.substring(PREFIX.length());
  }

  public static TokenResponse generateTokens(User user) {
    try {
      return TokenResponse.builder()
          .accessToken(
              JWT.create()
                  .withSubject(user.getUserId().toString())
                  .withClaim("name", user.getName())
                  .withClaim("nickname", user.getNickname())
                  .withClaim("email", user.getEmail())
                  .withClaim("roles", user.getRoles().stream().map(Role::name).collect(Collectors.toList()))
                  .withIssuedAt(new Date())
                  .withExpiresAt(new Date(new Date().getTime() + ACCESS_EXPIRATION_TIME))
                  .sign(ALGORITHM))
          .refreshToken(
              JWT.create()
                  .withSubject(user.getUserId().toString())
                  .withIssuedAt(new Date())
                  .withExpiresAt(new Date(new Date().getTime() + REFRESH_EXPIRATION_TIME))
                  .sign(ALGORITHM))
          .build();
    } catch (Exception e) {
      log.error("{}", e);
      throw new SecurityException(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  public static User getUserIfTokenIsValid(String token) {
    try {
      DecodedJWT decodedJwt = JWT.require(ALGORITHM).build().verify(token);

      return User.builder()
          .userId(UUID.fromString(decodedJwt.getSubject()))
          .name(decodedJwt.getClaim("name").asString())
          .nickname(decodedJwt.getClaim("nickname").asString())
          .email(decodedJwt.getClaim("email").asString())
          .roles(new HashSet<>(decodedJwt.getClaim("roles").asList(Role.class)))
          .build();
    } catch (TokenExpiredException e) {
      throw new SecurityException(ErrorCode.EXPIRED_TOKEN, e.getMessage());
    } catch (JWTVerificationException e) {
      throw new SecurityException(ErrorCode.INVALID_TOKEN, e.getMessage());
    } catch (Exception e) {
      throw new SecurityException(ErrorCode.INVALID_TOKEN, e.getMessage());
    }
  }
}
