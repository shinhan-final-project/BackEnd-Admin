package com.shinhan.friends_stock_admin.controller;

import com.shinhan.friends_stock_admin.DTO.auth.Response;
import com.shinhan.friends_stock_admin.DTO.termGame.PostTermQuestionDTO;
import com.shinhan.friends_stock_admin.DTO.termGame.ResponseTermInfoDTO;
import com.shinhan.friends_stock_admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

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

}
