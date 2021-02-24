package com.caronte.server.controller.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.caronte.server.entity.RoleName;

public class UserDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
    
    @NotBlank
    private RoleName roleName;

    public UserDTO() {

    }

    public UserDTO(String name, String username, String email, String password, RoleName roleName) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roleName = roleName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public RoleName getRoleName() {
		return roleName;
	}

	public void setRoleName(RoleName roleName) {
		this.roleName = roleName;
	}
    
}