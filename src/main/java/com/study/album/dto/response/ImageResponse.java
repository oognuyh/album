package com.study.album.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "이미지 요청 응답")
public class ImageResponse {

  @Schema(description = "이미지 주소 목록")
  private List<String> imageUrls;
}
