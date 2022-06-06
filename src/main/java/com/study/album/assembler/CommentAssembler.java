package com.study.album.assembler;

import com.study.album.domain.Comment;
import com.study.album.domain.UserSummary;
import com.study.album.dto.response.CommentResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentAssembler implements RepresentationModelAssembler<Comment, CommentResponse> {

  private final UserSummaryAssembler userSummaryAssembler;

  @Override
  public CommentResponse toModel(Comment comment) {
    CommentResponse commentResponse = new CommentResponse();

    BeanUtils.copyProperties(comment, commentResponse);

    UserSummary userSummary =
        new UserSummary() {

          @Override
          public UUID getUserId() {
            return comment.getAuthor().getUserId();
          }

          @Override
          public String getNickname() {
            return comment.getAuthor().getNickname();
          }
        };

    commentResponse.setAuthor(userSummaryAssembler.toModel(userSummary));

    return commentResponse;
  }
}
