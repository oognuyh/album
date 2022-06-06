package com.study.album.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "게시글 요청 응답")
@Relation(collectionRelation = "posts")
public class PostResponse extends RepresentationModel<PostResponse> {

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
}
