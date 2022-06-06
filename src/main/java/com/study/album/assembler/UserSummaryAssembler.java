package com.study.album.assembler;

import com.study.album.domain.UserSummary;
import com.study.album.dto.response.UserResponse;
import com.study.album.endpoints.UserEndpoints;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class UserSummaryAssembler
    implements RepresentationModelAssembler<UserSummary, UserResponse> {

  @Override
  public UserResponse toModel(UserSummary userSummary) {
    UserResponse userResponse = new UserResponse();

    BeanUtils.copyProperties(userSummary, userResponse);

    return userResponse.add(
        WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UserEndpoints.class)
                    .getUserSummaryByUserId(userSummary.getUserId()))
            .withSelfRel());
  }
}
