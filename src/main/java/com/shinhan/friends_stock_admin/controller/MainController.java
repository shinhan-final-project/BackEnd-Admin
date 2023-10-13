package com.shinhan.friends_stock_admin.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/main")
    public String main(Authentication authentication) {
        if (authentication.getAuthorities().stream().anyMatch(
                a -> a.getAuthority().equals("ROLE_ADMIN")
        )) {
            return "forward:/admin/index";
        } else if (authentication.getAuthorities().stream().anyMatch(
                a -> a.getAuthority().equals("ROLE_MANAGER")
        )) {
            return "forward:/manager/index";
        }
        return "main";  // 접근 거부 페이지로
    }
}
