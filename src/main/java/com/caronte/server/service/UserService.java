package com.caronte.server.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.caronte.server.controller.dto.UserDTO;
import com.caronte.server.entity.Role;
import com.caronte.server.entity.RoleName;
import com.caronte.server.entity.User;
import com.caronte.server.exception.AppException;
import com.caronte.server.repository.RoleRepository;
import com.caronte.server.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository repository;
	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	
	public User createUser(UserDTO userDTO) {
		User user = new User();
		user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		user.setEmail(userDTO.getEmail());
		user.setName(userDTO.getName());
		user.setUsername(userDTO.getUsername());

		Role userRole = roleRepository.findByName(userDTO.getRoleName())
						.orElseThrow(() -> new AppException("User Role not set."));

		user.setRoles(Collections.singleton(userRole));
		
		return repository.save(user);
		
	}
	
	public User createProprietarioUser(User user) {
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		

		Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
						.orElseThrow(() -> new AppException("User Role not set."));

		user.setRoles(Collections.singleton(userRole));
		
		return repository.save(user);
		
	}
}
