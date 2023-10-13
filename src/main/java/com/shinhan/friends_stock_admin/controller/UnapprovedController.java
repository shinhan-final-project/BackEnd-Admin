package com.shinhan.friends_stock_admin.controller;

import com.shinhan.friends_stock_admin.DTO.auth.SignInRequestDTO;
import com.shinhan.friends_stock_admin.service.UnapprovedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UnapprovedController {
    private final UnapprovedService unapprovedService;
    //최종 관리자에게 관리자 권한 요청하기

    @PostMapping("/api/signIn")
    public ResponseEntity<Long> signIn(@RequestBody SignInRequestDTO signInRequestDTO){
        unapprovedService.signIn(signInRequestDTO);
        return ResponseEntity.ok().build();
    }
}
