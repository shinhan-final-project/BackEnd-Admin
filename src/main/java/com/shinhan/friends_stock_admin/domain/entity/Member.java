package com.shinhan.friends_stock_admin.domain.entity;

import com.shinhan.friends_stock_admin.domain.Role;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name="member")
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Member implements UserDetails {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "nickname")
    @NotNull
    private String name;

    @Column(name = "password")
    @NotNull
    private String password;

    @Column(name = "gender")
    @NotNull
    private String gender;

    @Column(name = "age")
    @NotNull
    private int age;

    @Column(name = "invest_career_year")
    @NotNull
    private int investCareerYear;

    @Column(name = "is_onboded", columnDefinition = "TINYINT(1)")
    @ColumnDefault("0")
    @NotNull
    private boolean isOnboded;

    @Column(name = "role")
    @NotNull
    private Role role;

    @Column(name = "created_at")
    @NotNull
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @NotNull
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collectors = new ArrayList<>();
        collectors.add(()-> "ROLE_"+this.role);

        return collectors;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
