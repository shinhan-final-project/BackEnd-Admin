package com.shinhan.friends_stock_admin.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf().disable().headers().frameOptions().disable()// h2-console 화면 사용하기 위함
                .and()
                .authorizeRequests()
                    .antMatchers("/login", "/api/signIn","/signup", "/test").permitAll() // 누구나 접근 가능
                    .antMatchers("/admin/**", "/api/admin/**").hasRole("ADMIN") // ADMIN 만 접근 가능
                    .antMatchers("/manager/**", "/api/manager/**").hasRole("MANAGER") // MANAGER 만 접근 가능
                    .antMatchers("/main").access("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('UNAPPROVED')")
                    //.anyRequest().authenticated() // 나머지는 권한이 있기만 하면 접근 가능
                .and()
                    .formLogin() // 로그인에 대한 설정
                    .loginPage("/login") // 로그인 페이지 링크
                    .defaultSuccessUrl("/main", true) // 로그인 성공시 연결되는 주소
                    .failureUrl("/login?error=true")
                .and()
                .logout() // 로그아웃 관련 설정
                    .logoutSuccessUrl("/login") // 로그아웃 성공시 연결되는 주소
                    .invalidateHttpSession(true) // 로그아웃시 저장해 둔 세션 날리기
                    .and().build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}