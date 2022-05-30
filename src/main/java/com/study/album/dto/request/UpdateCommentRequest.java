package com.study.album.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "댓글 수정 요청")
public class UpdateCommentRequest {

  @Schema(hidden = true)
  private UUID commentId;

  @Schema(description = "댓글 내용")
  @NotBlank(message = "댓글 내용을 입력하세요")
  private String content;

  @Schema(hidden = true)
  private UUID authorId;

  @Schema(hidden = true)
  private UUID postId;
}
