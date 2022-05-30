package com.study.album.exception;

import com.study.album.error.ErrorCode;

public class NotFoundException extends BaseException {

  public NotFoundException(ErrorCode errorCode) {
    super(errorCode, null, null);
  }

  public NotFoundException(ErrorCode errorCode, String description) {
    super(errorCode, description, null);
  }
}
