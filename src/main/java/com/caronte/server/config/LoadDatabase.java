package com.caronte.server.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.caronte.server.entity.Role;
import com.caronte.server.entity.RoleName;
import com.caronte.server.repository.RoleRepository;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class LoadDatabase {

	
	@Bean
	CommandLineRunner initDatabase(RoleRepository repository) {
		return args -> {
			if(repository.findAll().isEmpty()) {
				repository.save(new Role(RoleName.ROLE_ADMIN));
			    repository.save(new Role(RoleName.ROLE_USER));
			}
		};
	}
	
}
