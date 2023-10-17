package com.shinhan.friends_stock_admin.domain.entity;

import com.shinhan.friends_stock_admin.repository.InvestItemRepository;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name="stock_news_year")
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class) // 추가
public class StockNewsYear {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    @NotNull
    private Long newsId;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "stock_item_id")
    private InvestItem stockItem;

    @Column(name = "year")
    @NotNull
    private int year;

    @Column(name = "title")
    @NotNull
    private String title;

    @Column(name = "url", length = 2000)
    @NotNull
    private String url;

    @Builder
    public StockNewsYear(InvestItem investItem, int year, String title, String url){
        this.stockItem = investItem;
        this.year = year;
        this.title = title;
        this.url = url;
    }

    public static StockNewsYear of(InvestItem investItem, int year, String title, String url) {
        return StockNewsYear.builder()
                .investItem(investItem)
                .year(year)
                .title(title)
                .url(url).build();
    }
}
