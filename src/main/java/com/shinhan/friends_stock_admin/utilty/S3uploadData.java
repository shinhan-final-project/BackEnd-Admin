package com.shinhan.friends_stock_admin.utilty;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class S3uploadData {
    private List<Data> list;
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Data{
        private String date;
        private String price;
    }
}
