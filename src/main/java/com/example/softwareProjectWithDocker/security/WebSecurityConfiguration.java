package com.example.softwareProjectWithDocker.security;

import com.example.softwareProjectWithDocker.security.custom.CustomEntryEndpoint;
import com.example.softwareProjectWithDocker.security.jwt.JwtConfig;
import com.example.softwareProjectWithDocker.security.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import com.example.softwareProjectWithDocker.security.jwt.JwtVerifier;
import com.example.softwareProjectWithDocker.security.service.ApplicationUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.crypto.SecretKey;

import static com.example.softwareProjectWithDocker.security.ApplicationUserPermission.SOURCE_READ;
import static com.example.softwareProjectWithDocker.security.ApplicationUserRole.ADMIN;
import static com.example.softwareProjectWithDocker.security.ApplicationUserRole.USER;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    private static final Logger log = LoggerFactory.getLogger(WebSecurityConfiguration.class);

    private final SecretKey jwtSecretKey;

    private final JwtConfig jwtConfig;

    private final ApplicationUserService applicationUserService;
    @Autowired
    public WebSecurityConfiguration(SecretKey jwtSecretKey, JwtConfig jwtConfig, ApplicationUserService applicationUserService) {
        this.jwtSecretKey = jwtSecretKey;
        this.jwtConfig = jwtConfig;
        this.applicationUserService = applicationUserService;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder){
        authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(applicationUserService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomEntryEndpoint customEntryEndpoint, AuthenticationManager authenticationManager) throws Exception {
        JwtUsernameAndPasswordAuthenticationFilter customAuthFilter = new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager,jwtConfig,jwtSecretKey);
        customAuthFilter.setFilterProcessesUrl("/api/v1/login");
        return http.csrf(csrf -> csrf.disable())
                .addFilter(customAuthFilter)
                .addFilterAfter(new JwtVerifier(jwtSecretKey,jwtConfig), JwtUsernameAndPasswordAuthenticationFilter.class)
                // permissions based and role based
                .authorizeHttpRequests(request ->
                        request.requestMatchers("/api/v1/se/all/**").permitAll()
                                .requestMatchers("/api/v1/se/update/{id}").hasRole(ADMIN.name())
                                .requestMatchers("/api/v1/login").permitAll()
                                .requestMatchers("/api/v1/refresh-token").permitAll()
                                .requestMatchers("/api/v1/se/with-pagination").hasAnyRole(ADMIN.name(), USER.name())
                                .requestMatchers("/api/v1/se/create").hasRole(ADMIN.name())
                                .requestMatchers("/api/v1/se/with-pagination-and-sorting").hasAuthority(SOURCE_READ.getPermission())
                                .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .logout(logout -> {
                    logout.logoutUrl("/logout")
                            .permitAll()
                            .invalidateHttpSession(true)
                            .clearAuthentication(true)
                            .logoutRequestMatcher(new AntPathRequestMatcher("/logout", HttpMethod.GET.toString() + ""))
                            .deleteCookies("JSESSIONID", "remember-me");
                })
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .exceptionHandling(exception -> exception.authenticationEntryPoint(customEntryEndpoint))
                .authenticationProvider(daoAuthenticationProvider())
                .build();
    }

}
