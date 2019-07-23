package com.caronte.server.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class Embarcacao {

	@Id
	@GeneratedValue
	private Long id;
	
	private String nome;
	private String descricao;
	
	@ManyToOne
	private Proprietario proprietario;
	
	public Embarcacao() {
		// TODO Auto-generated constructor stub
	}
	public Embarcacao(String nome) {
		this.nome = nome;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	
}
