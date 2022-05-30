package com.study.album.dto.request;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "회원가입 요청")
public class RegisterUserRequest {

  @NotBlank(message = "이름을 입력하세요.")
  @Schema(description = "사용자 이름")
  private String name;

  @NotBlank(message = "닉네임을 입력하세요.")
  @Schema(description = "사용자 닉네임")
  private String nickname;

  @NotBlank(message = "이메일을 입력하세요.")
  @Email(message = "이메일 양식이 올바르지 않습니다.")
  @Schema(description = "사용자 이메일")
  private String email;

  @NotBlank(message = "비밀번호를 입력하세요.")
  @Schema(description = "사용자 비밀번호")
  private String password;

  @NotBlank(message = "확인 비밀번호를 입력하세요.")
  @Schema(description = "사용자 비밀번호 확인")
  private String confirmPassword;

  @AssertTrue(message = "비밀번호가 일치하지 않습니다.")
  public boolean confirmPassword() {
    return this.password.equals(this.confirmPassword);
  }
}
