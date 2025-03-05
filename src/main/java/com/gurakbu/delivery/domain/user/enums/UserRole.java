package com.gurakbu.delivery.domain.user.enums;

public enum UserRole {
    USER,
    OWNER,
    ADMIN;

    public boolean isAdmin() {
        return this == ADMIN;
    }

    public boolean isOwner() {
        return this == OWNER;
    }
}
