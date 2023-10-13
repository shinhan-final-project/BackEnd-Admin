package com.shinhan.friends_stock_admin.domain.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "term_quiz_item")
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class TermQuizItem {
    //  선택지번호
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long itemId;

    //  문제번호 ->FK
    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "quiz_id")
    private TermQuizQuestion quizId;

    // 선택지 text
    @Column(name = "content", columnDefinition = "TEXT")
    @NotNull
    private String content;

    //작성한 관리자 번호
    @Column(name = "writer_id")
    @NotNull
    private long writerId;

    //    용어 추가일
    @Column(name = "created_at")
    @NotNull
    @CreatedDate
    private LocalDateTime createdAt;

    //    수정일
    @Column(name = "updated_at")
    @NotNull
    @LastModifiedDate
    private LocalDateTime updatedAt;

}
