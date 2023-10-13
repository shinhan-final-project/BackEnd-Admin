package com.shinhan.friends_stock_admin.service;

import com.shinhan.friends_stock_admin.DTO.termGame.ResponseTermInfoDTO;
import com.shinhan.friends_stock_admin.repository.TermQuizQuestionRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminService {
    private final TermQuizQuestionRepository termQuizQuestionRepository;
    public List<ResponseTermInfoDTO> getTermAll() {
        return termQuizQuestionRepository.findAll().stream().map(ResponseTermInfoDTO::of).toList();
    }

    public ResponseTermInfoDTO getTerm(long id) {
        return ResponseTermInfoDTO.of(termQuizQuestionRepository.findById(id).orElseThrow());
    }
}
