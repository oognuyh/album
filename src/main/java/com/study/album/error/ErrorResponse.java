package com.study.album.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.study.album.exception.BaseException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime timestamp = LocalDateTime.now();

  private String code;

  private String message;

  private String description;

  private List<Field> fields;

  @Getter
  @Setter
  @AllArgsConstructor
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class Field {

    private String name;

    private String message;

    private String rejectedValue;
  }

  private ErrorResponse(String code, String message, String description, List<Field> fields) {
    this.code = code;
    this.message = message;
    this.description = description;
    this.fields = fields;
  }

  public static ResponseEntity<ErrorResponse> from(BaseException exception) {
    return new ResponseEntity<>(
        new ErrorResponse(
            exception.getCode(),
            exception.getMessage(),
            exception.getDescription(),
            exception.getFields()),
        exception.getStatus());
  }
}
