package com.study.album.endpoints;

import java.util.UUID;

import javax.validation.Valid;

import com.study.album.dto.request.AddPostRequest;
import com.study.album.dto.request.UpdatePostRequest;
import com.study.album.dto.response.PostResponse;
import com.study.album.error.ErrorResponse;
import com.study.album.model.User;
import com.study.album.service.PostService;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Post Endpoints")
@RequestMapping("/posts")
public class PostEndpoints {

  private final PostService postService;

  @Operation(summary = "게시글 조회 및 검색")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK", content = @Content()),
        @ApiResponse(
            responseCode = "Any Errors",
            description = "Error",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
      })
  @GetMapping
  public ResponseEntity<Page<PostResponse>> getPosts(
      @Parameter(required = false, description = "검색어") @RequestParam(required = false) String q,
      @ParameterObject @PageableDefault(size = 20, page = 0) Pageable pageable) {
    return new ResponseEntity<>(
        StringUtils.hasText(q)
            ? postService.searchPosts(q.strip(), pageable)
            : postService.getPosts(pageable),
        HttpStatus.OK);
  }

  @Operation(summary = "새로운 게시글 작성")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Created"),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
      })
  @PostMapping
  public ResponseEntity<PostResponse> addPost(
      @Valid @RequestBody AddPostRequest addPostRequest, @AuthenticationPrincipal User user) {
    addPostRequest.setAuthorId(user.getUserId());

    return new ResponseEntity<>(postService.addPost(addPostRequest), HttpStatus.CREATED);
  }

  @Operation(summary = "게시글 수정")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
      })
  @PutMapping("/{postId}")
  public ResponseEntity<PostResponse> updatePost(
      @PathVariable UUID postId,
      @Valid @RequestBody UpdatePostRequest updatePostRequest,
      @AuthenticationPrincipal User user) {
    updatePostRequest.setPostId(postId);
    updatePostRequest.setAuthorId(user.getUserId());

    return new ResponseEntity<>(postService.updatePost(updatePostRequest), HttpStatus.OK);
  }

  @Operation(summary = "게시글 삭제", security = @SecurityRequirement(name = "jwt"))
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Ok"),
    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @DeleteMapping("/{postId}")
  public ResponseEntity<Void> deletePostByPostId(@PathVariable UUID postId) {
    postService.deletePostByPostId(postId);

    return new ResponseEntity<>(HttpStatus.OK);
  }
}
