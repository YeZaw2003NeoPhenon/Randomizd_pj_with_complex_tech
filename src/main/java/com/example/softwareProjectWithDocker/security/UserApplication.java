package com.example.softwareProjectWithDocker.security;

import jakarta.persistence.*;

@Entity
@Table(name = "user_app")
public class UserApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private ApplicationUserRole applicationUserRole;

    public UserApplication() {}

    public UserApplication(Integer id, String username, String password, ApplicationUserRole applicationUserRole) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.applicationUserRole = applicationUserRole;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ApplicationUserRole getApplicationUserRole() {
        return applicationUserRole;
    }

    public void setApplicationUserRole(ApplicationUserRole applicationUserRole) {
        this.applicationUserRole = applicationUserRole;
    }
}
