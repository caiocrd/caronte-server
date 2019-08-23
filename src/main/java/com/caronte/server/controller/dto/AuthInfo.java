package com.caronte.server.controller.dto;

import com.caronte.server.entity.User;
import com.caronte.server.security.UserPrincipal;

public class AuthInfo {

	private UserPrincipal usuario;
	
	private String jwt;
	
	public AuthInfo(UserPrincipal usuario, String jwt){
        this.usuario = usuario;
        this.jwt = jwt;
    }
	public UserPrincipal getUsuario() {
		return this.usuario;
	}

	public void setUsuario(UserPrincipal usuario) {
		this.usuario = usuario;
	}
    public String getJwt() {
		return this.jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
	
	
	
	
}
