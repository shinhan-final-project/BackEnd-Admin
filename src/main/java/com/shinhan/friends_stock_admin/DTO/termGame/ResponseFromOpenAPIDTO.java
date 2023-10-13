package com.shinhan.friends_stock_admin.DTO.termGame;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ResponseFromOpenAPIDTO {
    private Response response;
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Response {
        private Header header;
        private Body body;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Header {
        private String resultCode;
        private String resultMsg;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Body {
        private int pageNo;
        private int totalCount;
        private Items items;
        private int numOfRows;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Items {
        private List<Item> item;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Item {
        private String ksdFnceDictDescContent;
        private String fnceDictNm;
    }
}
