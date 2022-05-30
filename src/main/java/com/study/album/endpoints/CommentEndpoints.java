package com.study.album.endpoints;

import java.util.UUID;

import javax.validation.Valid;

import com.study.album.dto.request.AddCommentRequest;
import com.study.album.dto.request.UpdateCommentRequest;
import com.study.album.dto.response.CommentResponse;
import com.study.album.error.ErrorResponse;
import com.study.album.model.User;
import com.study.album.service.CommentService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Comment Endpoints")
@RequestMapping("/posts/{postId}/comments")
public class CommentEndpoints {

  private final CommentService commentService;

  @Operation(
    summary = "게시글의 댓글 조회")
  @ApiResponses(
    value = {
      @ApiResponse(
          responseCode = "200",
          description = "Success",
          content = @Content(array = @ArraySchema(schema = @Schema(implementation = CommentResponse.class))))
    })
  @GetMapping
  public ResponseEntity<Page<CommentResponse>> getCommentsByPostId(
      @PathVariable UUID postId, Pageable pageable) {
    return new ResponseEntity<>(
        commentService.getCommentsByPostId(postId, pageable), HttpStatus.OK);
  }

  @Operation(
    summary = "새로운 댓글 추가")
  @ApiResponses(
    value = {
      @ApiResponse(
          responseCode = "200",
          description = "Success",
          content = @Content(schema = @Schema(implementation = CommentResponse.class))),
      @ApiResponse(
          responseCode = "400",
          description = "Bad Request",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
  @PostMapping
  public ResponseEntity<CommentResponse> addComment(
      @PathVariable UUID postId,
      @Valid @RequestBody AddCommentRequest addCommentRequest,
      @AuthenticationPrincipal User user) {
    addCommentRequest.setPostId(postId);
    addCommentRequest.setAuthorId(user.getUserId());

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @Operation(summary = "기존 댓글 수정")
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200", 
      description = "Success", 
      content = @Content(schema = @Schema(implementation = CommentResponse.class)))
  })
  @PutMapping("/{commentId}")
  public ResponseEntity<CommentResponse> updateComment(
      @PathVariable UUID postId,
      @PathVariable UUID commentId,
      @Valid @RequestBody UpdateCommentRequest updateCommentRequest,
      @AuthenticationPrincipal User user) {
    updateCommentRequest.setPostId(postId);
    updateCommentRequest.setAuthorId(user.getUserId());
    updateCommentRequest.setCommentId(commentId);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Operation(summary = "기존 댓글 삭제")
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200", 
      description = "Success", 
      content = @Content(schema = @Schema(implementation = CommentResponse.class)))
  })
  @DeleteMapping("/{commentId}")
  public ResponseEntity<Void> deleteCommentByCommentId(@PathVariable UUID commentId) {
    // commentService.deleteComment(postId, commentId, authorId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
