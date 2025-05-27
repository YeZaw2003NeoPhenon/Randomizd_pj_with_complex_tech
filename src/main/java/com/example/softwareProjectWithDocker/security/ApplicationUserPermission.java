package com.example.softwareProjectWithDocker.security;

public enum ApplicationUserPermission {

    SOURCE_READ("source:read"),
    SOURCE_WRITE("source:write");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

}
