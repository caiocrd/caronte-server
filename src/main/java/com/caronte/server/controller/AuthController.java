package com.caronte.server.controller;

import java.net.URI;
import java.util.Collections;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.caronte.server.controller.dto.AuthInfo;
import com.caronte.server.entity.Role;
import com.caronte.server.entity.RoleName;
import com.caronte.server.entity.User;
import com.caronte.server.exception.AppException;
import com.caronte.server.repository.RoleRepository;
import com.caronte.server.repository.UserRepository;
import com.caronte.server.security.JwtTokenProvider;
import com.caronte.server.security.UserPrincipal;

@RestController
@RequestMapping("/auth")
public class AuthController {

        @Autowired
        AuthenticationManager authenticationManager;

        @Autowired
        UserRepository userRepository;

        @Autowired
        RoleRepository roleRepository;

        @Autowired
        PasswordEncoder passwordEncoder;

        @Autowired
        JwtTokenProvider tokenProvider;

        @RequestMapping(value = "/sigin", method = RequestMethod.POST)
        public ResponseEntity<?> authenticateUser(@Valid @RequestBody User loginRequest) {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(),
                                loginRequest.getPassword()
                        )
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);

                String jwt = tokenProvider.generateToken(authentication);
                AuthInfo auth = new AuthInfo((UserPrincipal) authentication.getPrincipal(), jwt);
                return ResponseEntity.ok((auth));
        }
	
	@RequestMapping(value = "/siginup", method = RequestMethod.POST)
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
                if(userRepository.existsByUsername(user.getUsername())) {
                return new ResponseEntity<Object>("Username is already taken!",
                        HttpStatus.BAD_REQUEST);
                }

                if(userRepository.existsByEmail(user.getEmail())) {
                return new ResponseEntity<Object>("Email Address already in use!",
                        HttpStatus.BAD_REQUEST);
                }

                user.setPassword(passwordEncoder.encode(user.getPassword()));

                Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                                .orElseThrow(() -> new AppException("User Role not set."));

                user.setRoles(Collections.singleton(userRole));

                User result = userRepository.save(user);

                URI location = ServletUriComponentsBuilder
                        .fromCurrentContextPath().path("/api/users/{username}")
                        .buildAndExpand(result.getUsername()).toUri();

                return ResponseEntity.created(location).body( "User registered successfully");
        }
	
}
