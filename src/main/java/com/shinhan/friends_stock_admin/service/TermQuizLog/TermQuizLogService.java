package com.shinhan.friends_stock_admin.service.TermQuizLog;

import com.shinhan.friends_stock_admin.DTO.auth.Response;
import com.shinhan.friends_stock_admin.DTO.log.ResponseLogDTO;
import com.shinhan.friends_stock_admin.domain.entity.*;
import com.shinhan.friends_stock_admin.repository.TermQuizItemRepository;
import com.shinhan.friends_stock_admin.repository.TermQuizLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TermQuizLogService {
    private final TermQuizLogRepository termQuizLogRepository;
    private final TermQuizItemRepository termQuizItemRepository;
    @Transactional
    public Response<ResponseLogDTO> getTermQuizLog(long logId) throws Exception {
        TermQuizLog termQuizLog = termQuizLogRepository.findById(logId).orElseThrow();
        String answerContent = termQuizItemRepository.findById(termQuizLog.getTermQuizId().getAnswerItemId())
                .map(TermQuizItem::getContent).orElseThrow();
        return Response.success(ResponseLogDTO.of(termQuizLog, answerContent));
    }

    @Transactional
    public Response<List<ResponseLogDTO>> getTermQuizLogAll() throws Exception {
        List<ResponseLogDTO> responseLogDTOList = termQuizLogRepository.findAll().stream()
                .map(log -> ResponseLogDTO.of(log, termQuizItemRepository.findById(log.getTermQuizId().getAnswerItemId())
                        .map(TermQuizItem::getContent)
                        .orElseThrow())).toList();
        return Response.success(responseLogDTOList);
    }
}
