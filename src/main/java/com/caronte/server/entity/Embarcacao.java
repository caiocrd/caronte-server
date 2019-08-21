package com.caronte.server.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
public class Embarcacao {

	@Id
	@GeneratedValue	
	private Long id;
	
	private String nome;
	private String descricao;
	
	private String caminhoImagem;
	
	private String caminhoDocumento;
	
	@Transient
	private MultipartFile imagem;
	
	@Transient
	private MultipartFile documento;
	
	@Transient
	private String proprietario_id;
	
	@ManyToOne
	@JsonIgnoreProperties("embarcacoes")
	private Proprietario proprietario;
	
	@OneToMany(mappedBy = "embarcacao")
	private List<Movimentacao> movimentacoes;
	
	
	public Embarcacao() {
		// TODO Auto-generated constructor stub
	}
	public Embarcacao(String nome) {
		this.nome = nome;
	}
	public Embarcacao(Long id) {
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
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Proprietario getProprietario() {
		return proprietario;
	}
	public void setProprietario(Proprietario proprietario) {
		this.proprietario = proprietario;
	}
	public List<Movimentacao> getMovimentacoes() {
		return movimentacoes;
	}
	public void setMovimentacoes(List<Movimentacao> movimentacoes) {
		this.movimentacoes = movimentacoes;
	}
	public MultipartFile getImagem() {
		return imagem;
	}
	public void setImagem(MultipartFile imagem) {
		this.imagem = imagem;
	}
	public MultipartFile getDocumento() {
		return documento;
	}
	public void setDocumento(MultipartFile documento) {
		this.documento = documento;
	}
	public String getProprietario_id() {
		return proprietario_id;
	}
	public void setProprietario_id(String proprietario_id) {
		this.proprietario_id = proprietario_id;
	}
	public String getCaminhoImagem() {
		return caminhoImagem;
	}
	public void setCaminhoImagem(String caminhoImagem) {
		this.caminhoImagem = caminhoImagem;
	}
	public String getCaminhoDocumento() {
		return caminhoDocumento;
	}
	public void setCaminhoDocumento(String caminhoDocumento) {
		this.caminhoDocumento = caminhoDocumento;
	}
	
	
}
