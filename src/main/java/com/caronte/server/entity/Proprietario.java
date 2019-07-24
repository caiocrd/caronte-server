package com.caronte.server.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;

@Data
@Entity
public class Proprietario {

	@Id
	@GeneratedValue
	private Long id;
	
	private String nome;
	
	@OneToMany(mappedBy = "proprietario", cascade = CascadeType.ALL)
	private List<Embarcacao> embarcacoes;
	
	public Proprietario() {
		// TODO Auto-generated constructor stub
	}
	public Proprietario(String nome) {
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
	public List<Embarcacao> getEmbarcacoes() {
		return embarcacoes;
	}
	public void setEmbarcacoes(List<Embarcacao> embarcacoes) {
		this.embarcacoes = embarcacoes;
	}
	
	
	
}
