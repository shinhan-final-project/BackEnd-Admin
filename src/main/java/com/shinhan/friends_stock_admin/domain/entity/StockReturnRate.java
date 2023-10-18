package com.shinhan.friends_stock_admin.domain.entity;

import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "stock_return_rate")
@Getter
@EntityListeners(AuditingEntityListener.class)
public class StockReturnRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rate_id")
    private Long rateId;

    @ManyToOne
    @JoinColumn(name = "stock_item_id")
    private InvestItem investItem;

    @Column(name = "year")
    private Integer year;

    @Column(name = "rate", precision = 4, scale = 2)
    private BigDecimal rate;

    @Column(name = "last_price")
    private Integer lastPrice;

    public StockReturnRate(InvestItem investItem, int year, BigDecimal rate, int lastPrice) {
        this.investItem = investItem;
        this.year = year;
        this.rate = rate;
        this.lastPrice = lastPrice;
    }
}
