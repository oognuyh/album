package com.study.album.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "로그인 요청")
public class LogInRequest {

  @NotBlank(message = "이메일을 입력하세요.")
  @Email(message = "이메일 양식이 올바르지 않습니다.")
  @Schema(description = "사용자 이메일")
  private String email;

  @NotBlank(message = "비밀번호를 입력하세요.")
  @Schema(description = "사용자 비밀번호")
  private String password;
}
