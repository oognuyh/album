package com.study.album.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.study.album.model.User;
import com.study.album.model.User.Role;
import com.study.album.model.UserSummary;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(Include.NON_NULL)
@Schema(name = "사용자 요청 응답")
public class UserResponse {

  @Schema(description = "사용자 고유번호")
  private UUID userId;

  @Schema(description = "사용자 이름")
  private String name;

  @Schema(description = "사용자 닉네임")
  private String nickname;

  @Schema(description = "사용자 이메일")
  private String email;

  @Schema(description = "사용자 권한")
  private Set<Role> roles;

  @Schema(description = "사용자 생성일자")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdAt;

  @Schema(description = "사용자 수정일자")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime modifiedAt;

  public static UserResponse from(User user) {
    return UserResponse.builder()
        .userId(user.getUserId())
        .name(user.getName())
        .nickname(user.getNickname())
        .email(user.getEmail())
        .roles(user.getRoles())
        .createdAt(user.getCreatedAt())
        .modifiedAt(user.getModifiedAt())
        .build();
  }

  public static UserResponse from(UserSummary userSummary) {
    return UserResponse.builder()
        .userId(userSummary.getUserId())
        .nickname(userSummary.getNickname())
        .build();
  }
}
