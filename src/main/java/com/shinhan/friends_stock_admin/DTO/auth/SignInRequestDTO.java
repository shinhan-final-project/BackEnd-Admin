package com.shinhan.friends_stock_admin.DTO.auth;

import lombok.Data;

@Data
public class SignInRequestDTO {
    private String name;
    private String password;
    private String department;
}
