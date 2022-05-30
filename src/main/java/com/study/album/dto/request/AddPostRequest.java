package com.study.album.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "게시글 추가 요청")
public class AddPostRequest {

  @Schema(nullable = false, description = "제목")
  @NotBlank(message = "제목을 입력하세요.")
  private String title;

  @Schema(nullable = false, description = "내용")
  private String content;

  @Schema(nullable = false, description = "태그 목록")
  private List<String> tags;

  @Schema(nullable = false, description = "이미지 주소 목록")
  private List<String> imageUrls;

  @Schema(hidden = true)
  private UUID authorId;
}
