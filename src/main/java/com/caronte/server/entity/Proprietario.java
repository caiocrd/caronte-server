package com.caronte.server.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

@Entity
public class Proprietario {

	@Id
	@GeneratedValue
	private Long id;
	
	private String nome;
	private String caminhoHabilitacao;
	private String caminhoHabilitacaoPng;
	private String caminhoDocumento;
	private String caminhoDocumentoPng;
	
	@Transient
	private MultipartFile habilitacao;

	@Transient
	private MultipartFile documento;
	
	@OneToMany(mappedBy = "proprietario", cascade = CascadeType.ALL)
	private List<Embarcacao> embarcacoes;
	
	@OneToMany(mappedBy = "titular", cascade = CascadeType.ALL)
	private List<Dependente> dependentes;

	public Proprietario() {
		// TODO Auto-generated constructor stub
	}
	public Proprietario(String nome) {
		this.nome = nome;
	}
	public Proprietario(Long id) {
		this.id = id;
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

	public List<Dependente> getDependentes() {
		return dependentes;
	}

	public void setDependentes(List<Dependente> dependentes) {
		this.dependentes = dependentes;
	}
	public String getCaminhoHabilitacaoPng() {
		return caminhoHabilitacaoPng;
	}
	public void setCaminhoHabilitacaoPng(String caminhoHabilitacaoPng) {
		this.caminhoHabilitacaoPng = caminhoHabilitacaoPng;
	}
	public String getCaminhoDocumentoPng() {
		return caminhoDocumentoPng;
	}
	public void setCaminhoDocumentoPng(String caminhoDocumentoPng) {
		this.caminhoDocumentoPng = caminhoDocumentoPng;
	}
	
	
}
