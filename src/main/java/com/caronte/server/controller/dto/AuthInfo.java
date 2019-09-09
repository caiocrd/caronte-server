package com.caronte.server.controller.dto;

import com.caronte.server.entity.User;
import com.caronte.server.security.UserPrincipal;

public class AuthInfo {

	private UserPrincipal usuario;
	
	private int expiresIn;
	
	private String jwt;
	
	public AuthInfo(UserPrincipal usuario, String jwt, int expriresIn){
        this.usuario = usuario;
        this.jwt = jwt;
        this.expiresIn = expriresIn;
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
	public int getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}
	
	
	
	
}
