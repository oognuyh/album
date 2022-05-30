package com.study.album.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "0001", "There is no matching user"),

  IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "0002", "There is no matching image"),

  POST_NOT_FOUND(HttpStatus.NOT_FOUND, "0003", "There is no matching post"),

  COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "0003", "There is no matching comment"),

  WRONG_EMAIL_OR_PASSWORD(HttpStatus.BAD_REQUEST, "0004", "Wrong email or password"),

  DISABLED_USER(HttpStatus.BAD_REQUEST, "0005", "The account is disabled"),

  LOCKED_USER(HttpStatus.BAD_REQUEST, "0006", "The account is locked"),

  BAD_REQUEST(HttpStatus.BAD_REQUEST, "0007", "Invalid request"),

  INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "0008", "Invalid token"),

  EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "0009", "The token is expired"),

  NO_PERMISSION(HttpStatus.FORBIDDEN, "0010", "You have no permission"),

  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "0011", "Internal server error occurs"),

  IMAGE_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "0012", "Could not upload images"),

  NEED_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "0013", "You should sign in");

  private HttpStatus status;

  private String code;

  private String message;

  private ErrorCode(HttpStatus status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
