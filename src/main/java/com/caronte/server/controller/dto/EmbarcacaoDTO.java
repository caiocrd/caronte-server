package com.caronte.server.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.caronte.server.entity.Embarcacao;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class EmbarcacaoDTO {

	private Long id;
	
	private String nome;
	private String descricao;
	private String imagem;
	private String documento;
	
	
	@JsonIgnoreProperties("embarcacoes")
	private ProprietarioDTO proprietario;
	
	private List<MovimentacaoDTO> movimentacoes;
	
	public EmbarcacaoDTO(Embarcacao embarcacao) {
		this.nome = embarcacao.getNome();
		this.descricao = embarcacao.getDescricao();
		this.proprietario = new ProprietarioDTO(embarcacao.getProprietario());
		this.imagem = "/imagem?id="+embarcacao.getId();
		this.documento = "/documento-embarcacao?id="+embarcacao.getId();
		this.movimentacoes = embarcacao.getMovimentacoes().stream().map(movimentacao -> new MovimentacaoDTO(movimentacao)).collect(Collectors.toList());
	}
	public EmbarcacaoDTO(Embarcacao embarcacao, ProprietarioDTO proprietarioDTO) {
		this.nome = embarcacao.getNome();
		this.descricao = embarcacao.getDescricao();
		this.proprietario = proprietarioDTO;
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

	public ProprietarioDTO getProprietario() {
		return proprietario;
	}

	public void setProprietario(ProprietarioDTO proprietario) {
		this.proprietario = proprietario;
	}
	
	public String getImagem() {
		return imagem;
	}
	public void setImagem(String imagem) {
		this.imagem = imagem;
	}
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public List<MovimentacaoDTO> getMovimentacoes() {
		return movimentacoes;
	}

	public void setMovimentacoes(List<MovimentacaoDTO> movimentacoes) {
		this.movimentacoes = movimentacoes;
	}
	public static List<EmbarcacaoDTO> converter(List<Embarcacao> embarcacaos) {
		return embarcacaos.stream().map(EmbarcacaoDTO::new).collect(Collectors.toList());
	}
	
	
}
