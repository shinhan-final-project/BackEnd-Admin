package com.shinhan.friends_stock_admin.DTO.termGame;

import com.shinhan.friends_stock_admin.domain.entity.TermQuizQuestion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ResponseTermInfoDTO {
    private long id;
    private String term;
    private String description;

    public static ResponseTermInfoDTO of(TermQuizQuestion termQuizQuestion) {
        return new ResponseTermInfoDTO(termQuizQuestion.getQuizId(), termQuizQuestion.getTerm(), termQuizQuestion.getDescription());
    }
}
