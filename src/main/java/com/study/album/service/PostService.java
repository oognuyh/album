package com.study.album.service;

import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.album.assembler.PostAssembler;
import com.study.album.domain.Post;
import com.study.album.dto.request.AddPostRequest;
import com.study.album.dto.request.UpdatePostRequest;
import com.study.album.dto.response.PostResponse;
import com.study.album.error.ErrorCode;
import com.study.album.exception.NotFoundException;
import com.study.album.repository.PostRepository;
import com.study.album.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;

  private final UserRepository userRepository;

  private final PostAssembler postAssembler;

  private final PagedResourcesAssembler<Post> pagedResourcesAssembler;

  @Transactional(readOnly = true)
  public PagedModel<PostResponse> getPosts(Pageable pageable) {
    return pagedResourcesAssembler.toModel(postRepository.getPosts(pageable), postAssembler);
  }

  @Transactional(readOnly = true)
  public PagedModel<PostResponse> searchPosts(String q, Pageable pageable) {
    return pagedResourcesAssembler.toModel(postRepository.searchPosts(q, pageable), postAssembler);
  }

  @Transactional(readOnly = true)
  public PostResponse getPostByPostId(UUID postId) {
    return postAssembler.toModel(
        postRepository
            .getPostByPostId(postId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND)));
  }

  @Transactional
  public PostResponse addPost(AddPostRequest addPostRequest) {
    Post post =
        Post.builder()
            .title(addPostRequest.getTitle())
            .author(userRepository.getById(addPostRequest.getAuthorId()))
            .content(addPostRequest.getContent())
            .imageUrls(addPostRequest.getImageUrls())
            .tags(addPostRequest.getTags())
            .build();

    return postAssembler.toModel(postRepository.save(post));
  }

  @Transactional
  public PostResponse updatePost(UpdatePostRequest updatePostRequest) {
    Post post =
        postRepository
            .getPostByPostId(updatePostRequest.getPostId())
            .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

    post.update(updatePostRequest);

    return postAssembler.toModel(post);
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
