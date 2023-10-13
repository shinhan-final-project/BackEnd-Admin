package com.shinhan.friends_stock_admin.controller;

import com.shinhan.friends_stock_admin.DTO.termGame.ResponseTermInfoDTO;
import com.shinhan.friends_stock_admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/api/admin/term")
    public List<ResponseTermInfoDTO> getTermInfoAll(){
        return adminService.getTermAll();
    }
    @GetMapping("/api/admin/term/{id}")
    public ResponseTermInfoDTO getTermInfo(@PathVariable("id") long id){
        return adminService.getTerm(id);
    }
}
