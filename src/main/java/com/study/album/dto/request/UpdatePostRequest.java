package com.study.album.dto.request;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "게시글 수정 요청")
public class UpdatePostRequest {

  @Schema(nullable = false, description = "게시글 제목")
  @NotBlank(message = "제목을 입력하세요.")
  private String title;

  @Schema(nullable = false, description = "게시글 내용")
  private String content;

  @Schema(nullable = false, description = "게시글 태그 목록")
  private List<String> tags;

  @Schema(nullable = false, description = "게시글 이미지 목록")
  private List<String> imageUrls;

  @Schema(hidden = true)
  private UUID postId;

  @Schema(hidden = true)
  private UUID authorId;
}
