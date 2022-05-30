package com.study.album.repository;

import com.study.album.config.DatabaseConfig;
import com.study.album.model.Post;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
@Import({DatabaseConfig.class})
public class PostRepositoryTest {

  @Autowired private PostRepository postRepository;

  @Test
  public void shouldReturnMatchingPostWhenGetPostByPostId() {
    // given
    UUID postId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

    // when
    Optional<Post> actual = postRepository.getPostByPostId(postId);

    // then
    Assertions.assertThat(actual.isPresent()).isTrue();
    Assertions.assertThat(actual.get().getPostId()).isEqualTo(postId);
  }

  @Test
  public void shouldReturnMatchingPostWhenGetPostsByTagName() {
    // given
    String q = "01";

    // when
    Page<Post> actual = postRepository.searchPosts(q, PageRequest.of(0, 10));

    // then
    Assertions.assertThat(actual.getTotalElements()).isGreaterThanOrEqualTo(1);
    Assertions.assertThat(
        actual.getContent().stream()
            .allMatch((post) -> post.getTags().stream().anyMatch((tag) -> tag.contains(q))));
  }
}
