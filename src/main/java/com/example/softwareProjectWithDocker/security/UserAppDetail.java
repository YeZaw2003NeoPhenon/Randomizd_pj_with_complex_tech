package com.example.softwareProjectWithDocker.security;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;


public class UserAppDetail implements UserDetails {

   private final UserApplication userApplication;

    public UserAppDetail( UserApplication userApplication) {
        this.userApplication = userApplication;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userApplication.getApplicationUserRole().getGrantedAuthorities();
    }

    @Override
    public String getPassword() {
        return userApplication.getPassword();
    }

    @Override
    public String getUsername() {
        return userApplication.getUsername();
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
