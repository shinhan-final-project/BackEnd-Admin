package com.shinhan.friends_stock_admin.controller.Unapproved;

import com.shinhan.friends_stock_admin.DTO.auth.Response;
import com.shinhan.friends_stock_admin.DTO.auth.SignInRequestDTO;
import com.shinhan.friends_stock_admin.DTO.investGame.PostInvestQuestionDTO;
import com.shinhan.friends_stock_admin.service.Admin.AdminService;
import com.shinhan.friends_stock_admin.service.Unapproved.UnapprovedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UnapprovedController {
    private final UnapprovedService unapprovedService;
    private final AdminService adminService;
    //최종 관리자에게 관리자 권한 요청하기

    @PostMapping("/api/signIn")
    public ResponseEntity<Long> signIn(@RequestBody SignInRequestDTO signInRequestDTO) throws Exception {
        unapprovedService.signIn(signInRequestDTO);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/test")
    public Response<String> registerInvestQuestion(@RequestBody PostInvestQuestionDTO postInvestQuestionDTO) throws Exception{
        return adminService.registerInvestQuestion(postInvestQuestionDTO);
    }
}
