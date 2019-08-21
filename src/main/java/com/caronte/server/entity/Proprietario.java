package com.caronte.server.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
@Entity
public class Proprietario {

	@Id
	@GeneratedValue
	private Long id;
	
	private String nome;
	private String caminhoHabilitacao;
	private String caminhoDocumento;
	
	@Transient
	private MultipartFile habilitacao;
	
	@Transient
	private MultipartFile documento;
	
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
	
}
