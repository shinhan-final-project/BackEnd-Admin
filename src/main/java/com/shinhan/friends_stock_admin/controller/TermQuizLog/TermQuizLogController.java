package com.shinhan.friends_stock_admin.controller.TermQuizLog;

import com.shinhan.friends_stock_admin.DTO.auth.Response;
import com.shinhan.friends_stock_admin.DTO.log.ResponseLogDTO;
import com.shinhan.friends_stock_admin.service.TermQuizLog.TermQuizLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TermQuizLogController {
    private final TermQuizLogService termQuizLogService;

    @GetMapping("/api/log")
    public Response<List<ResponseLogDTO>> getTermQuizLogAll() throws Exception {
        return termQuizLogService.getTermQuizLogAll();
    }

    @GetMapping("/api/log/{logId}")
    public Response<ResponseLogDTO> getTermQuizLog(@PathVariable("logId") long logId) throws Exception {
        return termQuizLogService.getTermQuizLog(logId);
    }
}
