package com.shinhan.friends_stock_admin.service;

import com.shinhan.friends_stock_admin.DTO.auth.ResponseApplyAdminRoleDTO;
import com.shinhan.friends_stock_admin.domain.Role;
import com.shinhan.friends_stock_admin.domain.entity.Admin;
import com.shinhan.friends_stock_admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ManagerService {
    private final AdminRepository adminRepository;
    public List<ResponseApplyAdminRoleDTO> getApplyAdminRole(boolean isApproved) {
        return adminRepository.findByIsApproved(isApproved).stream().map(ResponseApplyAdminRoleDTO::of).toList();
    }

    @Transactional
    public void approveApply(List<Long> applyMembersId) {
        List<Admin> list = applyMembersId.stream().map(adminRepository::findById)
                .map(Optional::orElseThrow)
                .toList();
        for(Admin admin:list){
            admin.updateRole(Role.ADMIN, true);
        }
    }
    @Transactional
    public void refuseApply(List<Long> applyMembersId) {
        for(long i: applyMembersId)
            adminRepository.deleteById(i);
    }
}
