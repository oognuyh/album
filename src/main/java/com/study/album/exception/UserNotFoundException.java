package com.study.album.exception;

import com.study.album.error.ErrorCode;
import java.util.UUID;

public class UserNotFoundException extends NotFoundException {

  public UserNotFoundException(UUID userId) {
    super(ErrorCode.USER_NOT_FOUND, "There is no matching user with id " + userId);
  }
}
