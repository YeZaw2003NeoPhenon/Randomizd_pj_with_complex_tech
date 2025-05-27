package com.example.softwareProjectWithDocker.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public enum Gender {
    MALE("male"),
    FEMALE("female");

    private String description;

    Gender(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public Gender fromValue(String value){
        for(Gender gender : Gender.values()){
            String currentGender = gender.getDescription();
            if(currentGender.equals(value)){
                return gender;
            }
        }
        throw new IllegalArgumentException("No Gender type verifiable!");
    }

}
