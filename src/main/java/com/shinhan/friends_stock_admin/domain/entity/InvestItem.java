package com.shinhan.friends_stock_admin.domain.entity;

import com.shinhan.friends_stock_admin.DTO.investGame.ResponseInvestOpenApiDTO;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="invest_item")
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class) // 추가
public class InvestItem {
    //퀴즈 id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private long itemId;

    //회사 이름
    @Column(name = "company_name")
    @NotNull
    private String companyName;

    //주식 고유 코드
    @Column(name = "stock_code", unique = true)
    @NotNull
    private int stockCode;

    @OneToMany(
            mappedBy = "stockItem",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<StockNewsYear> news;


    //문제로 출제 여부
    @Column(name = "is_published", columnDefinition = "TINYINT(1)")
    @ColumnDefault("0")
    @NotNull
    private boolean isPublished;


    //이 회사가 주식 상장 시작 날짜
    @Column(name = "stock_start_year")
    private Integer stockStartYear;

    //회사 설명
    @Column(name = "company_info")
    private String companyInfo;

    //문제 출제자가 설정한 시작 날짜
    @Column(name = "quiz_start_year")
    private Integer quizStartYear;

    @NotNull
    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public InvestItem(String companyName, int stockCode){
        this.companyName = companyName;
        this.stockCode = stockCode;
    }

    public static InvestItem of(ResponseInvestOpenApiDTO.DataBodyItem dataBodyItem) {
        return InvestItem.builder()
                .companyName(dataBodyItem.getStbdNm())
                .stockCode(Integer.parseInt(dataBodyItem.getStockCode()))
                .build();
    }

    public void updateInfo(String companyInfo, int quizStartYear, boolean isPublished) {
        this.companyInfo = companyInfo;
        this.quizStartYear = quizStartYear;
        this.isPublished = isPublished;
    }
}
