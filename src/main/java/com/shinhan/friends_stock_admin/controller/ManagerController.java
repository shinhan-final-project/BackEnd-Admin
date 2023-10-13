package com.shinhan.friends_stock_admin.controller;

import com.shinhan.friends_stock_admin.DTO.auth.Response;
import com.shinhan.friends_stock_admin.DTO.auth.ResponseApplyAdminRoleDTO;
import com.shinhan.friends_stock_admin.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ManagerController {
    private final ManagerService managerService;

    @GetMapping("/api/manager/wait-list")
    public Response<List<ResponseApplyAdminRoleDTO>> getApplyAdminRole() throws Exception {
        return managerService.getApplyAdminRole(false);
    }

    @PostMapping("/api/manager/approve")
    public void approveApply(@RequestBody List<Long> applyMembersId) throws Exception {
        managerService.approveApply(applyMembersId);
    }
    @DeleteMapping("/api/manager/refuse")
    public void refuseApply(@RequestBody List<Long> applyMembersId) throws Exception {
        managerService.refuseApply(applyMembersId);
    }
}
