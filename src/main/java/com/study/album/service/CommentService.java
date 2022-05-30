package com.study.album.service;

import com.study.album.dto.request.AddCommentRequest;
import com.study.album.dto.request.UpdateCommentRequest;
import com.study.album.dto.response.CommentResponse;
import com.study.album.error.ErrorCode;
import com.study.album.exception.NotFoundException;
import com.study.album.model.Comment;
import com.study.album.repository.CommentRepository;
import com.study.album.repository.PostRepository;
import com.study.album.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

  private final PostRepository postRepository;

  private final CommentRepository commentRepository;

  private final UserRepository userRepository;

  @Transactional(readOnly = true)
  public Page<CommentResponse> getCommentsByPostId(UUID postId, Pageable pageable) {
    log.debug("Received a request with post id({}) and pageable({})", postId, pageable);

    return commentRepository.getCommentsByPostId(postId, pageable).map(CommentResponse::from);
  }

  @Transactional
  public CommentResponse addComment(AddCommentRequest addCommentRequest) {
    log.debug("Received {}}", addCommentRequest);

    Comment comment =
        Comment.builder()
            .author(userRepository.getById(addCommentRequest.getAuthorId()))
            .content(addCommentRequest.getContent())
            .post(postRepository.getById(addCommentRequest.getPostId()))
            .build();

    return CommentResponse.from(commentRepository.save(comment));
  }

  @Transactional
  public CommentResponse updateComment(UpdateCommentRequest updateCommentRequest) {
    Comment comment =
        commentRepository
            .getCommentByCommentId(updateCommentRequest.getCommentId())
            .orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND));

    log.debug("Found existing comment with id {}", comment.getCommentId());

    comment.update(updateCommentRequest);

    log.debug("Comment({}) is updated Successfully", comment.getCommentId());

    return CommentResponse.from(comment);
  }

  @Transactional
  public void deleteCommentByCommentId(UUID commentId) {
    try {
      commentRepository.deleteById(commentId);
    } catch (EmptyResultDataAccessException e) {
      throw new NotFoundException(ErrorCode.COMMENT_NOT_FOUND);
    }
  }
}
