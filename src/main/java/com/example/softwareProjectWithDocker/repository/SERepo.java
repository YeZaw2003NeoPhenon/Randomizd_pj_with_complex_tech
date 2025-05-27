package com.example.softwareProjectWithDocker.repository;

import com.example.softwareProjectWithDocker.entity.SoftwareEngineer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SERepo extends JpaRepository<SoftwareEngineer, Long> {
}
