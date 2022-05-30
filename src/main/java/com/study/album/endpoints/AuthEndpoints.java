package com.study.album.endpoints;

import javax.validation.Valid;

import com.study.album.dto.request.LogInRequest;
import com.study.album.dto.response.TokenResponse;
import com.study.album.error.ErrorResponse;
import com.study.album.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Auth Endpoints")
@RequestMapping("/auth")
public class AuthEndpoints {

  private final UserService userService;

  @Operation(
      summary = "로그인")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Success",
            content = @Content(schema = @Schema(implementation = TokenResponse.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      })
  @PostMapping("/login")
  public ResponseEntity<TokenResponse> login(@Valid @RequestBody LogInRequest signInRequest) {
    return new ResponseEntity<>(userService.login(signInRequest), HttpStatus.OK);
  }

  @Operation(
      summary = "Refresh token")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Success",
            content = @Content(schema = @Schema(implementation = TokenResponse.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Bad Request",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
      })
  @PostMapping("/refresh")
  public ResponseEntity<TokenResponse> refreshToken(
      @RequestHeader(name = "x-refresh-token") String refreshToken) {
    return new ResponseEntity<>(userService.refreshToken(), HttpStatus.OK);
  }
}
