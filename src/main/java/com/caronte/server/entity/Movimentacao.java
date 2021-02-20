package com.caronte.server.entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Movimentacao {

	@Id
	@GeneratedValue
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private TipoMovimentacao tipo;
	
	private Calendar data;
	
	private String descricao;

	private boolean ocorrencia;
	
	@ManyToOne
	@JsonIgnore
	private Embarcacao embarcacao;

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

	public boolean getOcorrencia() {
		return ocorrencia;
	}

	public void setOcorrencia(boolean ocorrencia) {
		this.ocorrencia = ocorrencia;
	}

	public String getDescricao(){
		return this.descricao;
	}
	public void setDescricao(String descricao){
		this.descricao = descricao;
	}

	public Embarcacao getEmbarcacao() {
		return embarcacao;
	}

	public void setEmbarcacao(Embarcacao embarcacao) {
		this.embarcacao = embarcacao;
	}
	public String getDataFormatada(){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		String strDate = dateFormat.format(this.getData().getTime());
		return strDate;
	}
	
}
