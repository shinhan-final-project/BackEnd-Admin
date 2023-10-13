package com.shinhan.friends_stock_admin.DTO.termGame;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostTermQuestionDTO {
    private long termId;
    private String explanation;
    private List<PostTermQuestionOptionDTO> questionItem;
    private int plusPoint;
    private int minusPoint;
}
