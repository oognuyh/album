package com.study.album.repository;

import java.util.Optional;
import java.util.UUID;

import com.study.album.config.DatabaseConfig;
import com.study.album.model.User;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({DatabaseConfig.class})
public class UserRepositoryTest {

  @Autowired private UserRepository userRepository;

  @Test
  public void shouldReturnMatchingUserWhenGetUserByUserId() {
    // given
    UUID userId = UUID.fromString("9b6ed57e-b61f-4e0d-b4d2-eb16005ac698");

    // when
    Optional<User> actual = userRepository.getUserByUserId(userId);

    // then
    Assertions.assertThat(actual.isPresent()).isTrue();
    Assertions.assertThat(actual.get().getUserId()).isEqualTo(userId);
  }

  @Test
  public void shouldReturnMatchingUserWhenGetUserByEmail() {
    // given
    String email = "tester@test.org";

    // when
    Optional<User> actual = userRepository.getUserByEmail(email);

    Assertions.assertThat(actual.isPresent()).isTrue();
    Assertions.assertThat(actual.get().getEmail()).isEqualTo(email);
  }
}
