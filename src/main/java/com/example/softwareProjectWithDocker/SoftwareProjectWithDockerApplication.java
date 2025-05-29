package com.example.softwareProjectWithDocker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SoftwareProjectWithDockerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoftwareProjectWithDockerApplication.class, args);
	}

}
