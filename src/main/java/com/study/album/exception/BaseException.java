package com.study.album.exception;

import com.study.album.error.ErrorCode;
import com.study.album.error.ErrorResponse.Field;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public abstract class BaseException extends RuntimeException {

  private HttpStatus status;

  private String code;

  private String message;

  private String description;

  private List<Field> fields;

  public BaseException(ErrorCode errorCode, String description, List<Field> fields) {
    this(errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage(), description, fields);
  }
}
