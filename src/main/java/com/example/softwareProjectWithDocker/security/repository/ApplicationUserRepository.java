package com.example.softwareProjectWithDocker.security.repository;

import com.example.softwareProjectWithDocker.security.UserApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ApplicationUserRepository extends JpaRepository<UserApplication, Integer> {

   Optional<UserApplication> findByUsername(@Param("username") String username);

}
