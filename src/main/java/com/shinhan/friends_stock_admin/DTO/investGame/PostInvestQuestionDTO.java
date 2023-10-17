package com.shinhan.friends_stock_admin.DTO.investGame;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostInvestQuestionDTO {
    private long itemId;
    private String companyInfo;
    private int quizStartYear;
    private List<NewsInfo> newList;
    //2015:news[NewsInfo1]
    //2015:news[NewsInfo2]
    //2015:news[NewsInfo3]
    //2015:news[NewsInfo4]
    //2015:news[NewsInfo5]

    //2016:news
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class NewsInfo{
        private int year;
        private List<News> news;
    }
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class News{
        private String title;
        private String url;
    }

}
