package com.shinhan.friends_stock_admin.controller.Admin;

import com.shinhan.friends_stock_admin.DTO.auth.Response;
import com.shinhan.friends_stock_admin.DTO.investGame.PostInvestQuestionDTO;
import com.shinhan.friends_stock_admin.DTO.investGame.ResponseInvestItemDTO;
import com.shinhan.friends_stock_admin.DTO.termGame.PostTermQuestionDTO;
import com.shinhan.friends_stock_admin.DTO.termGame.ResponseTermInfoDTO;
import com.shinhan.friends_stock_admin.service.Admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    //용어 퀴즈
    @GetMapping("/api/admin/term")
    public Response<List<ResponseTermInfoDTO>> getTermInfoAll() throws Exception {
        return adminService.getTermAll();
    }
    @GetMapping("/api/admin/term/{id}")
    public Response<ResponseTermInfoDTO> getTermInfo(@PathVariable("id") long id) throws Exception {
        return adminService.getTerm(id);
    }

    @PostMapping("/api/admin/term")
    public Response<String> registerTermQuestion(@RequestBody PostTermQuestionDTO postTermQuestion, Principal principal) throws Exception {
        return adminService.registerTermQuestion(postTermQuestion, principal);
    }

    @GetMapping("/api/admin/invest-items")
    public Response<List<ResponseInvestItemDTO>> getInvestItems() throws Exception {
        return adminService.getInvestItems();
    }

    @PostMapping("/api/admin/invest")
    public Response<String> registerInvestQuestion(@RequestBody PostInvestQuestionDTO postInvestQuestionDTO) throws Exception{
        return adminService.registerInvestQuestion(postInvestQuestionDTO);
    }
}
