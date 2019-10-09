package com.caronte.server.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.web.multipart.MultipartFile;

@Entity
public class Dependente {

	@Id
	@GeneratedValue
	private Long id;
	
	private String nome;
	private String caminhoHabilitacao;
	private String caminhoDocumento;
	
	@NotNull
	@Transient
	private MultipartFile habilitacao;
	
	@NotNull
	@Transient
	private MultipartFile documento;
	
	@ManyToOne
	@JsonIgnore
	private Proprietario titular;
	
	public Dependente() {
		// TODO Auto-generated constructor stub
	}
	public Dependente(String nome) {
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
	public MultipartFile getHabilitacao() {
		return habilitacao;
	}
	public void setHabilitacao(MultipartFile habilitacao) {
		this.habilitacao = habilitacao;
	}
	public MultipartFile getDocumento() {
		return documento;
	}
	public void setDocumento(MultipartFile documento) {
		this.documento = documento;
	}
	public String getCaminhoHabilitacao() {
		return caminhoHabilitacao;
	}
	public void setCaminhoHabilitacao(String caminhoHabilitacao) {
		this.caminhoHabilitacao = caminhoHabilitacao;
	}
	public String getCaminhoDocumento() {
		return caminhoDocumento;
	}
	public void setCaminhoDocumento(String caminhoDocumento) {
		this.caminhoDocumento = caminhoDocumento;
	}
	public Proprietario getTitular() {
		return titular;
	}
	public void setTitular(Proprietario titular) {
		this.titular = titular;
	}
	
	
}
