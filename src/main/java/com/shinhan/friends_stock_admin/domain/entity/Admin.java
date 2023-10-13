package com.shinhan.friends_stock_admin.domain.entity;

import com.shinhan.friends_stock_admin.domain.Role;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name="admin")
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class) // 추가
public class Admin{
    @Id
    @Column(name = "admin_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "password")
    @NotNull
    private String password;

    @Column(name = "role")
    @NotNull
    private String role;

    @Column(name = "department")
    @NotNull
    private String department;

    @Column(name = "is_approved", columnDefinition = "TINYINT(1)")
    @ColumnDefault("0")
    private Boolean isApproved;

    @Column(name = "created_at")
    @NotNull
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @NotNull
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public Admin(String name, String password, Role role, String department, Boolean isApproved){
        this.name = name;
        this.password = password;
        this.role = role.getValue();
        this.department = department;
        this.isApproved = isApproved;
    }

    public void updateRole(Role role, boolean isApproved) {
        this.role = role.getValue();
        this.isApproved = isApproved;
    }
}
