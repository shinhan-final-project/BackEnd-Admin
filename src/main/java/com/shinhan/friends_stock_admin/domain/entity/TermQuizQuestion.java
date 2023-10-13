package com.shinhan.friends_stock_admin.domain.entity;

import com.shinhan.friends_stock_admin.DTO.termGame.ResponseFromOpenAPIDTO;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "term_quiz")
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class TermQuizQuestion {
    //  문제번호
    @Id
    @Column(name = "quiz_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long quizId;

    //  용어
    @Column(name = "term")
    @NotNull
    private String term;

//    한국예탁결제원 정의
    @Column(name = "description", columnDefinition = "TEXT")
    @NotNull
    private String description;

    //    관리자가 입력한 해설
    @Column(name = "explanation", columnDefinition = "TEXT")
    private String explanation;

    //    문제로 출제 여부
    @Column(name = "is_published", columnDefinition = "TINYINT(1)")
    @NotNull
    @ColumnDefault("0")
    private Boolean isPublished;

    //    정답 선택지 번호 -> FK
    @Column(name = "answer_item_id")
    private Long answerItemId;

    //    정답 시 상승할 호감도
    @Column(name = "plus_point")
    @NotNull
    @ColumnDefault("30")
    private int plusPoint;

    //    오답 시 하락할 호감도
    @Column(name = "minus_point")
    @NotNull
    @ColumnDefault("10")
    private int minusPoint;

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

    //    문제로 출제한 일시
    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @OneToMany(
            mappedBy = "quizId",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<TermQuizItem> items;

    @Builder
    public TermQuizQuestion(String term, String description, boolean isPublished, int plusPoint, int minusPoint, Long answerItemId){
        this.term = term;
        this.description = description;
        this.isPublished = isPublished;
        this.plusPoint = plusPoint;
        this.minusPoint = minusPoint;
        this.answerItemId = answerItemId;
    }
    public static List<TermQuizQuestion> convertToTermquizList(ResponseFromOpenAPIDTO responseFromOpenAPIDTO){
        return responseFromOpenAPIDTO.getResponse().getBody().getItems().getItem().stream().map(TermQuizQuestion::of).collect(Collectors.toList());
    }

    private static TermQuizQuestion of(ResponseFromOpenAPIDTO.Item item) {
        return TermQuizQuestion.builder()
                .term(item.getFnceDictNm())
                .description(item.getKsdFnceDictDescContent())
                .isPublished(false)
                .plusPoint(30)
                .minusPoint(10)
                .answerItemId(null)
                .build();
    }

    public void updateExplanation(String explanation, Boolean isPublished, int plusPoint, int minusPoint) {
        this.explanation = explanation;
        this.isPublished = isPublished;
        this.plusPoint = plusPoint;
        this.minusPoint = minusPoint;
    }

    public void updateAnswerItemId(long answerItemId) {
        this.answerItemId = answerItemId;
    }
}

