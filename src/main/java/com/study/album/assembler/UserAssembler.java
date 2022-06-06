package com.study.album.assembler;

import com.study.album.domain.User;
import com.study.album.dto.response.UserResponse;
import com.study.album.endpoints.UserEndpoints;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class UserAssembler implements RepresentationModelAssembler<User, UserResponse> {

  @Override
  public UserResponse toModel(User user) {
    UserResponse userResponse = new UserResponse();

    BeanUtils.copyProperties(user, userResponse);

    return userResponse
        .add(
            WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(UserEndpoints.class).getCurrentUser(null))
                .withSelfRel())
        .add(
            WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(UserEndpoints.class)
                        .getUserSummaryByUserId(user.getUserId()))
                .withRel("summary"));
  }
}
