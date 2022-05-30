package com.study.album.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.study.album.model.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "게시글 요청 응답")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponse {

  @Schema(description = "게시글 고유번호")
  private UUID postId;

  @Schema(description = "게시글 작성자")
  private UserResponse author;

  @Schema(description = "게시글 제목")
  private String title;

  @Schema(description = "게시글 내용")
  private String content;

  @Schema(description = "게시글 이미지 주소 목록")
  private List<String> imageUrls;

  @Schema(description = "게시글 태그 목록")
  private List<String> tags;

  @Schema(description = "게시글 생성일자")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdAt;

  @Schema(description = "게시글 수정일자")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime modifiedAt;

  public static PostResponse from(Post post) {
    return PostResponse.builder()
        .postId(post.getPostId())
        .author(UserResponse.from(post.getAuthor()))
        .title(post.getTitle())
        .content(post.getContent())
        .imageUrls(post.getImageUrls())
        .tags(post.getTags())
        .createdAt(post.getCreatedAt())
        .modifiedAt(post.getModifiedAt())
        .build();
  }
}
