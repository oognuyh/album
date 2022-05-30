package com.study.album.repository;

import com.study.album.model.User;
import com.study.album.model.UserSummary;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  @Query("SELECT u FROM user u")
  Page<UserSummary> getUserSummaries(Pageable pageable);

  @Query("SELECT u FROM user u WHERE (:q IS NULL) OR (u.nickname LIKE CONCAT('%', :q, '%'))")
  Page<UserSummary> getUserSummaries(@Param("q") String q, Pageable pageable);

  @Query("SELECT u FROM user u WHERE u.email = :email")
  Optional<User> getUserByEmail(@Param("email") String email);

  @Query("SELECT u FROM user u WHERE u.userId = :userId")
  Optional<UserSummary> getUserSummaryByUserId(@Param("userId") UUID userId);

  @Query("SELECT u FROM user u WHERE u.userId = :userId")
  Optional<User> getUserByUserId(@Param("userId") UUID userId);

  boolean existsUserByEmail(String email);
}
