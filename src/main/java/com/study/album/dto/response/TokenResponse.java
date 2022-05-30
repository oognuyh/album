package com.study.album.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "토큰 요청 응답")
public class TokenResponse {

  @Schema(description = "Access token")
  private String accessToken;

  @Schema(description = "Refresh Token")
  private String refreshToken;

  @Builder.Default
  @Schema(description = "Token Type")
  private String type = "Bearer";
}
