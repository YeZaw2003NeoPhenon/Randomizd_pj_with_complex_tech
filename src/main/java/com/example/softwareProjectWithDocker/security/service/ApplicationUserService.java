package com.example.softwareProjectWithDocker.security.service;
import com.example.softwareProjectWithDocker.security.ApplicationUserRole;
import com.example.softwareProjectWithDocker.security.UserAppDetail;
import com.example.softwareProjectWithDocker.security.UserApplication;
import com.example.softwareProjectWithDocker.security.repository.ApplicationUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class ApplicationUserService implements UserDetailsService {

    private final ApplicationUserRepository applicationUserRepository;

    private final Logger logger = LoggerFactory.getLogger(ApplicationUserService.class);

    @Autowired
    public ApplicationUserService(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserApplication userApplication = applicationUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        Set<String> authorities = userApplication.getApplicationUserRole().getGrantedAuthorities()
                                                 .stream()
                                                 .map(GrantedAuthority::getAuthority)
                                                 .collect(Collectors.toSet());

        // ROLE_ADMIN, ROLE_USER, SOURCE_READ, SOURCE_WRITE

       ApplicationUserRole role = authorities.stream()
                                                .filter(r -> r.startsWith("ROLE_"))
                                                .map(r -> ApplicationUserRole.valueOf(r.replace("ROLE_", "")))
                                                .findAny()
                                                .orElseGet(() -> ApplicationUserRole.USER);

        Set<String> permissions =  authorities.stream()
                                                .filter(r -> !r.startsWith("ROLE_"))
                                                .collect(Collectors.toSet());

        logger.info("User {} has role {}", username, role);

        logger.info("User {} has provided authorities: {} ",username,role.getGrantedAuthorities());

        logger.info("User {} has provided permissions: {} ",username,permissions);

        return new UserAppDetail(userApplication);
//        return User.withUsername(userApplication.getUsername())
//                   .password(userApplication.getPassword())
//                   .authorities(userApplication.getApplicationUserRole()
//                   .getGrantedAuthorities())
//                    .roles(userApplication.getApplicationUserRole()
//                    .name())
//                    .build();
    }
}
