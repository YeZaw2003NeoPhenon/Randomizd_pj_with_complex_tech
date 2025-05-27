package com.example.softwareProjectWithDocker.entity.record;

import com.example.softwareProjectWithDocker.entity.enums.Gender;

public record SoftwareEngineerRecord(Long id,String first_name, String last_name, String tech_stack, Gender gender){}
