package com.study.album.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.study.album.dto.request.AddCommentRequest;
import com.study.album.dto.request.UpdateCommentRequest;
import com.study.album.error.ErrorCode;
import com.study.album.exception.SecurityException;

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
@Entity(name = "comment")
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Comment extends Traceable {

  @EqualsAndHashCode.Include @Id private UUID commentId;

  @Column(nullable = false)
  private String content;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "authorId")
  private User author;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "postId")
  @ToString.Exclude
  private Post post;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "parentId")
  private Comment parent;

  @OneToMany(fetch = FetchType.EAGER)
  private List<Comment> children;

  @Builder
  public Comment(UUID commentId, String content, User author, Post post) {
    this.commentId = Objects.isNull(commentId) ? UUID.randomUUID() : commentId;
    this.content = content;
    this.author = author;
    this.post = post;
    this.parent = null;
    this.children = new ArrayList<>();
  }

  public void update(UpdateCommentRequest updateCommentRequest) {
    if (!updateCommentRequest.getAuthorId().equals(this.author.getUserId())) {
      throw new SecurityException(ErrorCode.NO_PERMISSION);
    }

    this.content = updateCommentRequest.getContent();
  }

  public void reply(AddCommentRequest addCommentRequest) {
    if (Objects.isNull(this.parent)) {

    }
  }
}
