package com.caronte.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.caronte.server.entity.Embarcacao;

public interface EmbarcacaoRepository extends JpaRepository<Embarcacao, Long>{

	public List<Embarcacao> findByNomeContainingIgnoreCaseOrderByNomeAsc(String nome);
}
