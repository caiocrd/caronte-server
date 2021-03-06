package com.caronte.server.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.caronte.server.controller.dto.UserDTO;
import com.caronte.server.entity.Role;
import com.caronte.server.entity.User;
import com.caronte.server.exception.AppException;
import com.caronte.server.repository.RoleRepository;
import com.caronte.server.repository.UserRepository;
import com.caronte.server.service.UserService;

@RestController	
@RequestMapping("users")
public class UserController {

	@Autowired
	private UserRepository repository;
	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserService userService;
	
	@CrossOrigin
	@GetMapping
	List<User> all(@RequestParam(required = false, defaultValue = "") String nome) {
		
		return repository.findByNameContainingIgnoreCase(nome);
	}
	
	@CrossOrigin
	@PostMapping
	ResponseEntity<?> newUser( @RequestBody UserDTO userDTO, UriComponentsBuilder uriBuilder) {
		
		User nova = this.userService.createUser(userDTO);
		
		URI uri = uriBuilder.path("/users/{id}").buildAndExpand(nova.getId()).toUri();
		return ResponseEntity.created(uri).body(nova);

	}
	
	@CrossOrigin
	@GetMapping("/{id}")
	ResponseEntity<User> one(@PathVariable Long id) {
		Optional<User> user = repository.findById(id);
		if(user.isPresent())
			return ResponseEntity.ok().body(user.get());
		return ResponseEntity.notFound().build();
	}
	@CrossOrigin
	@PutMapping("/{id}")
	ResponseEntity<?> replaceUser(@RequestBody UserDTO newUser, @PathVariable Long id, UriComponentsBuilder uriBuilder) {
		
		Optional<User> userOptional = repository.findById(id);
		User user = new User();
		if(userOptional.isPresent()){
			user = userOptional.get();
			user.setName(newUser.getName());
			user.setEmail(newUser.getEmail());
			Role userRole = roleRepository.findByName(newUser.getRoleName())
					.orElseThrow(() -> new AppException("User Role not set."));
			Role oldRole = user.getRoles().iterator().next();
			user.getRoles().remove(oldRole);
			user.getRoles().add(userRole);
			
			if(newUser.getPassword() != null && !newUser.getPassword().isEmpty())
				user.setPassword(passwordEncoder.encode(newUser.getPassword()));
			user.setUsername(newUser.getUsername());
			
			repository.save(user);
			
		}
		URI uri = uriBuilder.path("/users/{id}").buildAndExpand(id).toUri();
		return ResponseEntity.created(uri).body(user);
	}
	@CrossOrigin
	@DeleteMapping("/{id}")
	void deleteUser(@PathVariable Long id) {
		repository.deleteById(id);
	}

}
