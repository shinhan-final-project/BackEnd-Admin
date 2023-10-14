package com.shinhan.friends_stock_admin.service.Manager;

import com.shinhan.friends_stock_admin.DTO.auth.Response;
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
    public Response<List<ResponseApplyAdminRoleDTO>> getApplyAdminRole(boolean isApproved) throws Exception {
        try {
            return Response.success(adminRepository.findByIsApproved(isApproved).stream().map(ResponseApplyAdminRoleDTO::of).toList());
        }
        catch (Exception e){
            throw new Exception("관리자 권한을 요청한 명단을 불러올 수 없습니다.");
        }
    }

    @Transactional
    public Response<String> approveApply(List<Long> applyMembersId) throws Exception {
        try {
            List<Admin> list = applyMembersId.stream().map(adminRepository::findById)
                    .map(Optional::orElseThrow)
                    .toList();
            for (Admin admin : list) {
                admin.updateRole(Role.ADMIN, true);
            }
        }
        catch (Exception e){
            throw new Exception("관리자 권한 요청을 승인할 수 없습니다.");
        }
        return Response.success("요청 성공");
    }
    @Transactional
    public Response<String> refuseApply(List<Long> applyMembersId) throws Exception {
        try{
            for(long i: applyMembersId)
                adminRepository.deleteById(i);
        }
        catch (Exception e){
            throw new Exception("관리자 권한 요청을 거절할 수 없습니다.");
        }
        return Response.success("요청 성공");
    }
}
