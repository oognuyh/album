package com.study.album.endpoints;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.study.album.dto.response.ImageResponse;
import com.study.album.service.ImageService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.minio.GetObjectResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Image Endpoints")
@RequestMapping("/images")
public class ImageEndpoints {

  private final ImageService imageService;

  @Operation(
      summary = "이미지 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK"), 
      @ApiResponse(responseCode = "404", description = "Not Found"),
      @ApiResponse(responseCode = "500", description = "Internal Server Error")})
  @GetMapping(value = "/{imageId}")
  public ResponseEntity<byte[]> getImageByImageUrl(@PathVariable UUID imageId) throws IOException {
    GetObjectResponse image = imageService.getImageByImageId(imageId);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_TYPE, image.headers().get(HttpHeaders.CONTENT_TYPE))
        .body(image.readAllBytes());
  }

  @Operation(
      summary = "이미지 업로드")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK"), 
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "500", description = "Internal Server Error")})
  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<ImageResponse> uploadImages(
      @RequestPart("files") List<MultipartFile> files) {
    return new ResponseEntity<>(imageService.uploadImages(files), HttpStatus.CREATED);
  }
}
