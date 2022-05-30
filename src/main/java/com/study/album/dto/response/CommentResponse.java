package com.study.album.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.study.album.model.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "댓글 요청 응답")
public class CommentResponse {

  @Schema(description = "댓글 고유번호")
  private UUID commentId;

  @Schema(description = "댓글 내용")
  private String content;

  private UserResponse author;

  @Schema(description = "댓글 생성일자")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdAt;

  @Schema(description = "댓글 수정일자")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime modifiedAt;

  public static CommentResponse from(Comment comment) {
    return CommentResponse.builder()
        .commentId(comment.getCommentId())
        .content(comment.getContent())
        .author(UserResponse.from(comment.getAuthor()))
        .createdAt(comment.getCreatedAt())
        .modifiedAt(comment.getModifiedAt())
        .build();
  }
}
