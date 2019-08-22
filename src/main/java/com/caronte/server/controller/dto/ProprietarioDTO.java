package com.caronte.server.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.caronte.server.entity.Proprietario;


public class ProprietarioDTO {
	
	private Long id;
	
	private String nome;

	private List<EmbarcacaoDTO> embarcacoes;

	public ProprietarioDTO(Proprietario proprietario) {
		this.id = proprietario.getId();
		this.nome = proprietario.getNome();
		this.embarcacoes = proprietario.getEmbarcacoes().stream().map(embarcacao -> new EmbarcacaoDTO(embarcacao, this)).collect(Collectors.toList());
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

	public List<EmbarcacaoDTO> getEmbarcacoes() {
		return embarcacoes;
	}

	public void setEmbarcacoes(List<EmbarcacaoDTO> embarcacoes) {
		this.embarcacoes = embarcacoes;
	}
	
	
	
}
