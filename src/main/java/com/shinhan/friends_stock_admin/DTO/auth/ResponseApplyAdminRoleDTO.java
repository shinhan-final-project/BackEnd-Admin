package com.shinhan.friends_stock_admin.DTO.auth;

import com.shinhan.friends_stock_admin.domain.entity.Admin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ResponseApplyAdminRoleDTO {
    private final long id;
    private final String name;
    private final String department;
    public static ResponseApplyAdminRoleDTO of(Admin admin) {
        return new ResponseApplyAdminRoleDTO(admin.getId(), admin.getName(), admin.getDepartment());
    }
}
