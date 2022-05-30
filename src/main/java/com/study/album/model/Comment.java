package com.study.album.model;

import com.study.album.dto.request.UpdateCommentRequest;
import com.study.album.error.ErrorCode;
import com.study.album.exception.SecurityException;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@EqualsAndHashCode(callSuper = false)
public class Comment extends Traceable {

  @Id private UUID commentId;

  @Column(nullable = false)
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "authorId")
  private User author;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "postId")
  @ToString.Exclude
  private Post post;

  @Builder
  public Comment(UUID commentId, String content, User author, Post post) {
    this.commentId = Objects.isNull(commentId) ? UUID.randomUUID() : commentId;
    this.content = content;
    this.author = author;
    this.post = post;
  }

  public void update(UpdateCommentRequest updateCommentRequest) {
    if (!updateCommentRequest.getAuthorId().equals(this.author.getUserId())) {
      throw new SecurityException(ErrorCode.NO_PERMISSION);
    }

    this.content = updateCommentRequest.getContent();
  }
}
