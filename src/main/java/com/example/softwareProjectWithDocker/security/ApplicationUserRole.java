package com.example.softwareProjectWithDocker.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.softwareProjectWithDocker.security.ApplicationUserPermission.SOURCE_READ;
import static com.example.softwareProjectWithDocker.security.ApplicationUserPermission.SOURCE_WRITE;

public enum ApplicationUserRole {

   ADMIN(Sets.newHashSet(SOURCE_READ,SOURCE_WRITE)),
   USER(Sets.newHashSet(SOURCE_READ));

    private final Set<ApplicationUserPermission> permissions;

     ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        Set<SimpleGrantedAuthority> authorities = permissions.stream()
                                                             .map(ApplicationUserPermission::getPermission)
                                                             .map(SimpleGrantedAuthority::new)
                                                             .collect(Collectors.toSet());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }

}
