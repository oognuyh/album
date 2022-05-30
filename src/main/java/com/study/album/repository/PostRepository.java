package com.study.album.repository;

import com.study.album.model.Post;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {

  @Query("SELECT p FROM post p")
  Page<Post> getPosts(Pageable pageable);

  @Query(
      "SELECT p FROM post p JOIN p.author author JOIN p.tags tags "
          + "WHERE UPPER(author.name) LIKE CONCAT('%', UPPER(:q), '%') "
          + "OR UPPER(p.title) LIKE CONCAT('%', UPPER(:q), '%') "
          + "OR UPPER(tags) LIKE CONCAT('%', UPPER(:q), '%')")
  Page<Post> searchPosts(@Param("q") String q, Pageable pageable);

  Optional<Post> getPostByPostId(UUID postId);
}
