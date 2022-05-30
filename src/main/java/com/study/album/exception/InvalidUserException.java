package com.study.album.exception;

import com.study.album.error.ErrorCode;

public class InvalidUserException extends SecurityException {

  public InvalidUserException() {
    super(ErrorCode.WRONG_EMAIL_OR_PASSWORD);
  }
}
