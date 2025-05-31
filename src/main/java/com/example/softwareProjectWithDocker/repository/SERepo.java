package com.example.softwareProjectWithDocker.repository;

import com.example.softwareProjectWithDocker.entity.SoftwareEngineer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SERepo extends JpaRepository<SoftwareEngineer, Long> {

    // Find software engineers by first name, last name, and tech stack
    @Query("SELECT se FROM SoftwareEngineer se WHERE (:firstName IS NULL OR se.first_name = :firstName) " +
            "AND (:lastName IS NULL OR se.last_name = :lastName) " +
            "AND (:techStack IS NULL OR se.tech_stack = :techStack)")
    List<SoftwareEngineer> findByMultipleAttributes(@Param("firstName") String firstName,
                                                    @Param("lastName") String lastName,
                                                    @Param("techStack") String techStack);

}
