package com.rtcc.demo.infra.config;

public enum UserRole {
    ADMIN("ADMIN"), COORDINATOR("COORDINATOR"), ACADEMIC("ACADEMIC");

    private String role;

    UserRole(String role) {
        this.role = role;

    }

    public String getRole() {
        return role;
    }
}
