package com.study.album.service;

import com.study.album.dto.response.ImageResponse;
import com.study.album.error.ErrorCode;
import com.study.album.exception.ServerException;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

  @Value("${image.base-url}")
  private String IMAGE_BASE_URL;

  @Value("${minio.bucket}")
  private String BUCKET;

  private final MinioClient minioClient;

  public GetObjectResponse getImageByImageId(UUID imageId) {
    try {
      return minioClient.getObject(
          GetObjectArgs.builder().bucket(BUCKET).object(imageId.toString()).build());
    } catch (Exception e) {
      log.error("Failed to get the image({})", e.getMessage());

      throw new ServerException(ErrorCode.IMAGE_SERVER_ERROR);
    }
  }

  private String uploadImage(MultipartFile file) {
    try {
      InputStream image = file.getInputStream();
      String imageId = UUID.randomUUID().toString();

      minioClient.putObject(
          PutObjectArgs.builder().bucket(BUCKET).object(imageId).stream(
                  image, image.available(), -1)
              .contentType(file.getContentType())
              .build());

      return String.format("%s/%s", IMAGE_BASE_URL, imageId);
    } catch (Exception e) {
      log.error("Failed to upload the image({})", e.getMessage());

      throw new ServerException(ErrorCode.IMAGE_SERVER_ERROR);
    }
  }

  public ImageResponse uploadImages(List<MultipartFile> files) {
    return ImageResponse.builder()
        .imageUrls(files.stream().map(this::uploadImage).collect(Collectors.toList()))
        .build();
  }
}
