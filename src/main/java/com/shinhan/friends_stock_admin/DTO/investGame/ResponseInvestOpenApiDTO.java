package com.shinhan.friends_stock_admin.DTO.investGame;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ResponseInvestOpenApiDTO {
    private DataHeader dataHeader;
    private List<DataBodyItem> dataBody;
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class DataHeader {
        private String category;
        private String resultCode;
        private String resultMessage;
        private String successCode;

    }
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class DataBodyItem {
        private int rank;
        @JsonProperty("stbd_nm")
        private String stbdNm;
        @JsonProperty("stock_code")
        private String stockCode;

    }
}
