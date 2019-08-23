package com.caronte.server.controller.dto;

import java.util.Calendar;

import com.caronte.server.entity.Movimentacao;
import com.caronte.server.entity.TipoMovimentacao;

public class MovimentacaoDTO {
	
	private Long id;

	private TipoMovimentacao tipo;
	
	private Calendar data;
	
	private String ocorrencia;
	
	public MovimentacaoDTO(Movimentacao movimentacao) {
		this.data = movimentacao.getData();
		this.id = movimentacao.getId();
		
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoMovimentacao getTipo() {
		return tipo;
	}

	public void setTipo(TipoMovimentacao tipo) {
		this.tipo = tipo;
	}

	public Calendar getData() {
		return data;
	}

	public void setData(Calendar data) {
		this.data = data;
	}

	public String getOcorrencia() {
		return ocorrencia;
	}

	public void setOcorrencia(String ocorrencia) {
		this.ocorrencia = ocorrencia;
	}
	
}
