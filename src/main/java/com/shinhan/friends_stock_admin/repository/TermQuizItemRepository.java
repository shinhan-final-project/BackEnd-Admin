package com.shinhan.friends_stock_admin.repository;

import com.shinhan.friends_stock_admin.domain.entity.Admin;
import com.shinhan.friends_stock_admin.domain.entity.TermQuizItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermQuizItemRepository extends JpaRepository<TermQuizItem, Long> {
}
