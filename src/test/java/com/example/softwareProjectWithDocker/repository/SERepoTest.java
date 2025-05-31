package com.example.softwareProjectWithDocker.repository;

import com.example.softwareProjectWithDocker.entity.SoftwareEngineer;
import com.example.softwareProjectWithDocker.entity.enums.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SERepoTest {

    private final SERepo seRepo;

    @Autowired
    public SERepoTest(SERepo seRepo) {
        this.seRepo = seRepo;
    }

    @BeforeEach
    public void setUp() {

      List<SoftwareEngineer> softwareEngineers =  Arrays.asList(
        new SoftwareEngineer("Joe","Smith","Java", Gender.MALE),
        new SoftwareEngineer("John","Doe","Python", Gender.MALE),
        new SoftwareEngineer("Jane","Doe","C++", Gender.FEMALE)
      );

      seRepo.saveAll(softwareEngineers);
    }

    @DisplayName("Should return software engineers, tracked down by first name, last name, and tech stack")
    @Test
    public void testFindByMultipleAttributes() {

        // Test specific valid attributes
        List<SoftwareEngineer> foundedEngineers = seRepo.findByMultipleAttributes("John", "Doe", "Python");
        assertThat(foundedEngineers).isNotEmpty();
        assertThat(foundedEngineers).hasSize(1);
        assertThat(foundedEngineers.get(0).getFirst_name()).isEqualTo("John");
        assertThat(foundedEngineers.get(0).getLast_name()).isEqualTo("Doe");
        assertThat(foundedEngineers.get(0).getTech_stack()).isEqualTo("Python");


        List<SoftwareEngineer> notFoundEngineers = seRepo.findByMultipleAttributes("NonExistent", "Doe", "Java");
        assertThat(notFoundEngineers).isEmpty();

        List<SoftwareEngineer> allEngineers = seRepo.findByMultipleAttributes(null, null, null);
        assertThat(allEngineers).hasSize(3);
    }

}
