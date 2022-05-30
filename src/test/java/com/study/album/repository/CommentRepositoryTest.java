package com.study.album.repository;

import com.study.album.config.DatabaseConfig;
import com.study.album.model.Comment;
import com.study.album.model.Post;
import com.study.album.model.User;
import java.time.LocalDateTime;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
@Import({DatabaseConfig.class})
public class CommentRepositoryTest {

  @Autowired private CommentRepository commentRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private PostRepository postRepository;

  @Test
  public void shouldNotBeNullWhenSaveComment() {
    // given
    UUID postId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
    User author = userRepository.getById(UUID.fromString("9b6ed57eb61f-4e0d-b4d2-eb16-005ac698"));
    Post post = postRepository.getById(postId);

    Comment expected = Comment.builder().content("content").author(author).post(post).build();

    // when
    Comment actual = commentRepository.save(expected);

    // then
    Assertions.assertThat(actual.getCommentId()).isNotNull();
    Assertions.assertThat(actual.getContent()).isEqualTo(expected.getContent());
    Assertions.assertThat(actual.getCreatedAt()).isBefore(LocalDateTime.now());
  }

  @Test
  public void shouldBeSameWhenUpdateComment() {
    // given
    UUID commentId = UUID.fromString("5562d5a3-0beb-4f52-8f48-779c488038c1");

    // when
    Comment expected = commentRepository.findById(commentId).orElseThrow();

    expected.setContent("new content");

    Comment actual =
        commentRepository
            .getCommentByCommentId(UUID.fromString("5562d5a3-0beb-4f52-8f48-779c488038c1"))
            .orElseThrow();

    // then
    Assertions.assertThat(actual.getContent()).isEqualTo(expected.getContent());
  }

  @Test
  public void shouldbeNothingWhenDeleteComment() {
    // given
    UUID commentId = UUID.fromString("5562d5a3-0beb-4f52-8f48-779c488038c1");

    // when
    commentRepository.deleteById(commentId);

    boolean actual = commentRepository.existsById(commentId);

    // then
    Assertions.assertThat(actual).isFalse();
  }

  @Test
  public void shouldReturnPageTypeWhenGetByPostId() {
    // given
    UUID postId = UUID.fromString("1d471356-419a-4c3f-8e81-56a717a112a2");
    Pageable pageable = PageRequest.of(0, 10);

    Page<Comment> actual = commentRepository.getCommentsByPostId(postId, pageable);

    System.out.println(actual);

    Assertions.assertThat(actual.getTotalPages()).isGreaterThan(0);
  }
}
