package com.shinhan.friends_stock_admin.domain;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ROLE_ADMIN"),
    MANAGER("ROLE_MANAGER"),
    UNAPPROVED("ROLE_UNAPPROVED");

    private final String value;

    Role(String value) {
        this.value = value;
    }
}
