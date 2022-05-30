package com.study.album.exception;

import com.study.album.error.ErrorCode;

public class SecurityException extends BaseException {

  public SecurityException(ErrorCode errorCode) {
    super(errorCode, null, null);
  }

  public SecurityException(ErrorCode errorCode, String description) {
    super(errorCode, description, null);
  }
}
