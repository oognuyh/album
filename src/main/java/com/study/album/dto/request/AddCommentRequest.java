package com.study.album.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "댓글 추가 요청")
public class AddCommentRequest {

  @Schema(nullable = false, description = "내용")
  @NotBlank(message = "내용을 입력하세요.")
  private String content;

  @Schema(hidden = true)
  private UUID postId;

  @Schema(hidden = true)
  private UUID authorId;
}
