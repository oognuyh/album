package com.study.album.exception;

import com.study.album.error.ErrorCode;
import com.study.album.error.ErrorResponse.Field;
import java.util.List;

public class BadRequestException extends BaseException {

  public BadRequestException(String description, List<Field> fields) {
    super(ErrorCode.BAD_REQUEST, description, fields);
  }
}
