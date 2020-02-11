package com.caronte.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.caronte.server.entity.Embarcacao;
import com.caronte.server.entity.Movimentacao;
import com.caronte.server.entity.TipoMovimentacao;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long>{
	public Movimentacao findFirstByEmbarcacaoAndTipoNotOrderByIdDesc(Embarcacao embarcacao, TipoMovimentacao tipo);
	    
}
