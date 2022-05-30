package com.study.album.endpoints;

import java.util.UUID;

import javax.validation.Valid;

import com.study.album.dto.request.RegisterUserRequest;
import com.study.album.dto.response.UserResponse;
import com.study.album.error.ErrorResponse;
import com.study.album.model.User;
import com.study.album.service.UserService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "User Endpoints")
@RequestMapping("/users")
public class UserEndpoints {

  private final UserService userService;

  @Operation(
      summary = "사용자 요약 정보 조회")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
      })
  @GetMapping
  public ResponseEntity<Page<UserResponse>> getUserSummaries(
      @RequestParam String q,
      @PageableDefault(
              sort = {"nickname"},
              direction = Direction.ASC)
          Pageable pageable) {
    return new ResponseEntity<>(userService.getUserSummaries(q, pageable), HttpStatus.OK);
  }

  @Operation(
      summary = "특정 사용자 요약 정보 조회")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Success",
            content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
      })
  @GetMapping("/{userId}")
  public ResponseEntity<UserResponse> getUserSummaryByUserId(@PathVariable UUID userId) {
    return new ResponseEntity<>(userService.getUserSummaryByUserId(userId), HttpStatus.OK);
  }

  @Operation(summary = "현재 사용자 정보 조회")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Success",
            content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
      })
  @GetMapping("/me")
  public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal User user) {
    return new ResponseEntity<>(userService.getUserByUserId(user.getUserId()), HttpStatus.OK);
  }

  @Operation(summary = "회원가입")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Created",
            content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(
            responseCode = "400",
            description = "BadRequest",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
      })
  @PostMapping
  public ResponseEntity<UserResponse> register(
      @Parameter @Valid @RequestBody RegisterUserRequest registerUserRequest) {
    return new ResponseEntity<>(userService.register(registerUserRequest), HttpStatus.CREATED);
  }
}
