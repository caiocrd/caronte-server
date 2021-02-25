package com.caronte.server.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.caronte.server.entity.Proprietario;

public interface ProprietarioRepository extends JpaRepository<Proprietario, Long>{
	public List<Proprietario> findByNomeContainingIgnoreCaseOrderByNomeAsc(String nome);
	
	public Optional<Proprietario> findByUserUsername(String username);

}
