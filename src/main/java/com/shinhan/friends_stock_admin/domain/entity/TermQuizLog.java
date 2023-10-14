package com.shinhan.friends_stock_admin.domain.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "term_quiz_log")
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class TermQuizLog {
    @Id
    @Column(name = "log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long logId;

    @ManyToOne(
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "member_id")
    private Member memberId;

    @ManyToOne(
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "game_id")
    @ColumnDefault("1")
    private Game gameId;

    @ManyToOne(
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "quiz_id")
    private TermQuizQuestion termQuizId;

    @ManyToOne(
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "selected_item_id")
    private TermQuizItem termQuizItemId;

    @Column(name = "is_correct", columnDefinition = "TINYINT(1)")
    @NotNull
    private boolean isCorrect;

    @Column(name = "created_at")
    @NotNull
    @CreatedDate
    private LocalDateTime createdAt;


}
