package com.example.softwareProjectWithDocker.entity;

import com.example.softwareProjectWithDocker.entity.enums.Gender;
import jakarta.persistence.*;

@Entity
@Table(name = "engineer")
public class SoftwareEngineer {

    @Id
    @SequenceGenerator(name = "software-engineer-seq",
            sequenceName = "software-engineer-seq_id",
            initialValue = 1,
            allocationSize = 50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "software-engineer-seq")
    private Long id;

    @Column(nullable = false)
    private String first_name;

    @Column(nullable = false)
    private String last_name;

    @Column(nullable = false)
    private String tech_stack;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    public SoftwareEngineer(Long id, String first_name, String last_name, String tech_stack) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.tech_stack = tech_stack;
    }

    public SoftwareEngineer(String first_name, String last_name, String tech_stack,Gender gender) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.tech_stack = tech_stack;
        this.gender = gender;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public SoftwareEngineer() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getTech_stack() {
        return tech_stack;
    }

    public void setTech_stack(String tech_stack) {
        this.tech_stack = tech_stack;
    }

}
