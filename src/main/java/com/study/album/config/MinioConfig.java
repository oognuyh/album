package com.study.album.config;

import com.study.album.error.ErrorCode;
import com.study.album.exception.ServerException;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MinioConfig {

  @Value("${minio.url}")
  private String URL;

  @Value("${minio.access-key}")
  private String ACCESS_KEY;

  @Value("${minio.secret-key}")
  private String SECRET_KEY;

  @Value("${minio.bucket}")
  private String BUCKET;

  @Bean
  public MinioClient minioClient() {
    MinioClient minioClient =
        MinioClient.builder().endpoint(URL).credentials(ACCESS_KEY, SECRET_KEY).build();

    try {
      if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET).build())) {
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(BUCKET).build());
      }

      return minioClient;
    } catch (Exception e) {
      log.error("Can't connect to minioClient({})", e.getMessage());

      throw new ServerException(ErrorCode.IMAGE_SERVER_ERROR);
    }
  }
}
