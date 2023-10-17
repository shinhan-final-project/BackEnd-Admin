package com.shinhan.friends_stock_admin.repository;

import com.shinhan.friends_stock_admin.domain.entity.StockNewsYear;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestItemNewsRepository extends JpaRepository<StockNewsYear, Long> {
}
