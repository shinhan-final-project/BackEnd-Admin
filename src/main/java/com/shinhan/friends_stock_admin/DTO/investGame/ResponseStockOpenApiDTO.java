package com.shinhan.friends_stock_admin.DTO.investGame;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class ResponseStockOpenApiDTO {
    private Protocol protocol;
    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    public static class Protocol {
        private Chartdata chartdata;

    }
    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    public static class Chartdata {
        private int symbol;
        private String timeframe;
        private int precision;
        private String name;
        private int count;
        private String origintime;
        private List<Item> item;

    }
    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    public static class Item {
        private String data;
    }

}
