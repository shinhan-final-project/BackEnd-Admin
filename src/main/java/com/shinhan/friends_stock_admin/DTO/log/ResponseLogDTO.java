package com.shinhan.friends_stock_admin.DTO.log;

import com.shinhan.friends_stock_admin.domain.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ResponseLogDTO {
    private long logId;
    //회원 관련 정보
    private String memberGender;
    private int age;
    private int investCareerYear;

    //game 관련 정보
    private String gameType;
    private String gameReward;

    //문제 정보
    private String term;
    private String explanation;
    //문제의 정답 정보
    //getAnswerId로 TermQuizItem.getContent
    private String answerExplanation;

    //선택한 보기 설명
    //termQuizItem.getSelectedItemId.getContent
    private String selectExplanation;
    private String writerInformation;

    private boolean isCorrect;

    private LocalDateTime createdAt;

    public static ResponseLogDTO of(TermQuizLog termQuizLog, String selectExplanation) {
        Member member = termQuizLog.getMemberId();
        Game game = termQuizLog.getGameId();
        TermQuizQuestion termQuizQuestion = termQuizLog.getTermQuizId();
        TermQuizItem termQuizItem = termQuizLog.getTermQuizItemId();

        return new ResponseLogDTO(
                termQuizLog.getLogId(),
                member.getGender(),
                member.getAge(),
                member.getInvestCareerYear(),
                game.getGameType(),
                game.getReward(),
                termQuizQuestion.getTerm(),
                termQuizQuestion.getExplanation(),
                termQuizItem.getContent(),
                selectExplanation,
                termQuizLog.getTermQuizItemId().getWriterId(),
                termQuizLog.isCorrect(),
                termQuizLog.getCreatedAt()
        );

    }
}
