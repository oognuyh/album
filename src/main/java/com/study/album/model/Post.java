package com.study.album.model;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;

import com.study.album.dto.request.UpdatePostRequest;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name = "post")
@EqualsAndHashCode(callSuper = false)
public class Post extends Traceable {

  @Id private UUID postId;

  @Column(nullable = false)
  private String title;

  private String content;

  @ElementCollection
  @Column(name = "url")
  @JoinTable(name = "image", joinColumns = @JoinColumn(name = "postId"))
  private List<String> imageUrls;

  @ElementCollection
  @Column(name = "name")
  @JoinTable(name = "tag", joinColumns = @JoinColumn(name = "postId"))
  private List<String> tags;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "authorId")
  private User author;

  @Builder
  public Post(UUID postId, String title, String content, List<String> imageUrls, List<String> tags, User author) {
    this.postId = Objects.isNull(postId) ? UUID.randomUUID() : postId;
    this.title = title;
    this.content = content;
    this.tags = tags;
    this.author = author;
  }

  public void update(UpdatePostRequest updatePostRequest) {
    this.title = updatePostRequest.getTitle();
    this.content = updatePostRequest.getContent();
    this.tags = updatePostRequest.getTags();
    this.imageUrls = updatePostRequest.getImageUrls();
  }
}
