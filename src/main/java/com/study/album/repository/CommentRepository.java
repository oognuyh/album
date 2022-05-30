package com.study.album.repository;

import com.study.album.model.Comment;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {

  @Query("SELECT c FROM comment c WHERE c.post.postId = :postId")
  Page<Comment> getCommentsByPostId(@Param("postId") UUID postId, Pageable pageable);

  @Query("SELECT c FROM comment c WHERE c.commentId = :commentId")
  Optional<Comment> getCommentByCommentId(@Param("commentId") UUID commentId);
}
