package com.study.album.error;

import com.study.album.error.ErrorResponse.Field;
import com.study.album.exception.BadRequestException;
import com.study.album.exception.BaseException;
import com.study.album.exception.ServerException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({MethodArgumentNotValidException.class})
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
      MethodArgumentNotValidException exception) {
    log.error("MethodArgumentNotValidException occurs({})", exception.getMessage());

    List<Field> fields =
        exception.getBindingResult().getFieldErrors().stream()
            .map(
                (fieldError) ->
                    new Field(
                        fieldError.getField(),
                        fieldError.getDefaultMessage(),
                        fieldError.getRejectedValue() + ""))
            .collect(Collectors.toList());

    return this.handleAllExceptions(new BadRequestException(null, fields));
  }

  @ExceptionHandler({BaseException.class})
  public ResponseEntity<ErrorResponse> handleAllExceptions(BaseException exception) {
    log.error("Exception occurs({})", exception.getMessage());

    return ErrorResponse.from(exception);
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<ErrorResponse> handleAllExceptions(Exception exception) {
    log.error("Unknown Exception occurs({})", exception.getMessage());

    return ErrorResponse.from(
        new ServerException(ErrorCode.INTERNAL_SERVER_ERROR, exception.getLocalizedMessage()));
  }
}
