package com.shinhan.friends_stock_admin.service;

import com.shinhan.friends_stock_admin.DTO.auth.ResponseAdminDTO;
import com.shinhan.friends_stock_admin.DTO.auth.SignInRequestDTO;
import com.shinhan.friends_stock_admin.domain.Role;
import com.shinhan.friends_stock_admin.domain.entity.Admin;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.shinhan.friends_stock_admin.repository.AdminRepository;


@Service
@RequiredArgsConstructor
public class UnapprovedService implements UserDetailsService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    //최종 관리자에게 관리자 요청 보내기
    public void signIn(SignInRequestDTO signInRequestDTO){
        Admin admin = Admin.builder()
                .name(signInRequestDTO.getName())
                .password(passwordEncoder.encode(signInRequestDTO.getPassword()))
                .role(Role.UNAPPROVED)
                .isApproved(false)
                .department(signInRequestDTO.getDepartment())
                .build();
        adminRepository.save(admin);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(username);
        Admin admin = adminRepository.findByName(username).orElseThrow();
        return ResponseAdminDTO.of(admin);
    }
}
