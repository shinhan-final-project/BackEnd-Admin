package com.shinhan.friends_stock_admin.repository;

import com.shinhan.friends_stock_admin.domain.entity.InvestItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvestItemRepository extends JpaRepository<InvestItem, Long> {
    boolean existsByStockCode(int stockCode);

    List<InvestItem> findByIsPublished(boolean b);
}
