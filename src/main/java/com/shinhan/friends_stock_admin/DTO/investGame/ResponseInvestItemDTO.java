package com.shinhan.friends_stock_admin.DTO.investGame;

import com.shinhan.friends_stock_admin.domain.entity.InvestItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResponseInvestItemDTO {

    private final long id;
    private final String companyName;
    private final int stockCode;

    public static ResponseInvestItemDTO of(InvestItem investItem) {
        return new ResponseInvestItemDTO(investItem.getItemId(), investItem.getCompanyName(), investItem.getStockCode());
    }
}
