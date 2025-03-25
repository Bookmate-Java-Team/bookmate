package com.bookmate.bookmate.post.entity;


import com.bookmate.bookmate.post.dto.PostUpdateRequestDto;
import com.bookmate.bookmate.post.entity.enums.PostCategory;
import com.bookmate.bookmate.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "post")
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "content", nullable = false, columnDefinition = "TEXT")
  private String content;

  @Enumerated(EnumType.STRING)
  @Column(name = "category", nullable = false)
  private PostCategory category;

  @Column(name = "recruit_start_date")
  private LocalDate recruitStartDate;

  @Column(name = "recruit_end_date")
  private LocalDate recruitEndDate;

  @Column(name = "capacity")
  private Integer capacity;

  @Column(name = "current_participants")
  private Integer currentParticipants;

  @Column(name = "total_likes")
  private Integer totalLikes = 0;

  @Column(updatable = false)
  @CreatedDate
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;

  public Post updatePost(PostUpdateRequestDto postUpdateRequestDto) {
    if (postUpdateRequestDto.getTitle() != null) {
      this.title = postUpdateRequestDto.getTitle();
    }
    if (postUpdateRequestDto.getContent() != null) {
      this.content = postUpdateRequestDto.getContent();
    }
    return this;
  }

  public void addTotalLikes(Integer totalLikes) {
    this.totalLikes += totalLikes;
  }

  public void decreaseTotalLikes(Integer totalLikes) {
    this.totalLikes -= totalLikes;
  }
}
