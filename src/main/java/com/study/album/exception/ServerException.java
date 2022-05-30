package com.study.album.exception;

import com.study.album.error.ErrorCode;

public class ServerException extends BaseException {

  public ServerException(ErrorCode errorCode) {
    super(errorCode, null, null);
  }

  public ServerException(ErrorCode errorCode, String description) {
    super(errorCode, description, null);
  }
}
