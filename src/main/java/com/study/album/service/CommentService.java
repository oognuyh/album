package com.study.album.service;

import com.study.album.assembler.CommentAssembler;
import com.study.album.domain.Comment;
import com.study.album.dto.request.AddCommentRequest;
import com.study.album.dto.request.UpdateCommentRequest;
import com.study.album.dto.response.CommentResponse;
import com.study.album.error.ErrorCode;
import com.study.album.exception.NotFoundException;
import com.study.album.repository.CommentRepository;
import com.study.album.repository.PostRepository;
import com.study.album.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

  private final PostRepository postRepository;

  private final CommentRepository commentRepository;

  private final UserRepository userRepository;

  private final CommentAssembler commentAssembler;

  private final PagedResourcesAssembler<Comment> pagedResourcesAssembler;

  @Transactional(readOnly = true)
  public PagedModel<CommentResponse> getCommentsByPostId(UUID postId, Pageable pageable) {
    log.debug("Received a request with post id({}) and pageable({})", postId, pageable);

    return pagedResourcesAssembler.toModel(
        commentRepository.getCommentsByPostId(postId, pageable), commentAssembler);
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

    return commentAssembler.toModel(commentRepository.save(comment));
  }

  @Transactional
  public CommentResponse updateComment(UpdateCommentRequest updateCommentRequest) {
    Comment comment =
        commentRepository
            .getCommentByCommentId(updateCommentRequest.getCommentId())
            .orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND));

    log.debug("Found existing comment with id {}", comment.getCommentId());

    comment.update(updateCommentRequest);

    log.debug("Comment({}) was updated Successfully", comment.getCommentId());

    return commentAssembler.toModel(comment);
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
