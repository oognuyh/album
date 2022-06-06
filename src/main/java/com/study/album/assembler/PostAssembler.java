package com.study.album.assembler;

import com.study.album.domain.Post;
import com.study.album.domain.UserSummary;
import com.study.album.dto.response.PostResponse;
import com.study.album.endpoints.CommentEndpoints;
import com.study.album.endpoints.PostEndpoints;
import com.study.album.endpoints.UserEndpoints;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostAssembler implements RepresentationModelAssembler<Post, PostResponse> {

  private final UserSummaryAssembler userSummaryAssembler;

  @Override
  public PostResponse toModel(Post post) {
    PostResponse postResponse = new PostResponse();

    BeanUtils.copyProperties(post, postResponse);

    UserSummary userSummary =
        new UserSummary() {

          @Override
          public UUID getUserId() {
            return post.getAuthor().getUserId();
          }

          @Override
          public String getNickname() {
            return post.getAuthor().getNickname();
          }
        };

    postResponse.setAuthor(userSummaryAssembler.toModel(userSummary));

    return postResponse
        .add(
            WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(PostEndpoints.class)
                        .getPostByPostId(post.getPostId()))
                .withSelfRel())
        .add(
            WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(CommentEndpoints.class)
                        .getCommentsByPostId(post.getPostId(), null))
                .withRel("comments"))
        .add(
            WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(UserEndpoints.class)
                        .getUserSummaryByUserId(post.getAuthor().getUserId()))
                .withRel("author"));
  }
}
