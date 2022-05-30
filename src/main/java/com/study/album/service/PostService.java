package com.study.album.service;

import com.study.album.dto.request.AddPostRequest;
import com.study.album.dto.request.UpdatePostRequest;
import com.study.album.dto.response.PostResponse;
import com.study.album.error.ErrorCode;
import com.study.album.exception.NotFoundException;
import com.study.album.model.Post;
import com.study.album.repository.PostRepository;
import com.study.album.repository.UserRepository;

import java.util.UUID;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;

  private final UserRepository userRepository;

  @Transactional(readOnly = true)
  public Page<PostResponse> getPosts(Pageable pageable) {
    return postRepository.getPosts(pageable)
        .map(PostResponse::from);
  }

  @Transactional(readOnly = true)
  public Page<PostResponse> searchPosts(String q, Pageable pageable) {
    return postRepository.searchPosts(q, pageable)
        .map(PostResponse::from);
  }

  @Transactional
  public PostResponse addPost(AddPostRequest addPostRequest) {
    
    Post post = Post.builder()
      .title(addPostRequest.getTitle())
      .author(userRepository.getById(addPostRequest.getAuthorId()))
      .content(addPostRequest.getContent())
      .imageUrls(addPostRequest.getImageUrls())
      .tags(addPostRequest.getTags())
      .build();

    return PostResponse.from(postRepository.save(post));
  }

  @Transactional
  public PostResponse updatePost(UpdatePostRequest updatePostRequest) {
    Post post = postRepository.getPostByPostId(updatePostRequest.getPostId())
      .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

    log.debug("Found {}", post);

    post.update(updatePostRequest);

    return PostResponse.from(post);
  }

  @Transactional
  public void deletePostByPostId(UUID postId) {
    try {
      // postRepository.deletePostByPostId(postId);
    } catch (EntityNotFoundException e) {
      throw new NotFoundException(ErrorCode.POST_NOT_FOUND);
    }
  }
}
