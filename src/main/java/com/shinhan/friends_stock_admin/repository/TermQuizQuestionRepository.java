package com.shinhan.friends_stock_admin.repository;

import com.shinhan.friends_stock_admin.domain.entity.TermQuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface TermQuizQuestionRepository extends JpaRepository<TermQuizQuestion, Long> {

    List<TermQuizQuestion> findByIsPublished(boolean b);
}
