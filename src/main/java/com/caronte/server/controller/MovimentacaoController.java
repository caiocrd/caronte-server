package com.caronte.server.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.caronte.server.entity.Movimentacao;
import com.caronte.server.repository.MovimentacaoRepository;

@RestController	
@RequestMapping("movimentacao")
public class MovimentacaoController {

	private final MovimentacaoRepository repository;

	public MovimentacaoController(MovimentacaoRepository rep) {
		this.repository = rep;
	}
	
	@CrossOrigin
	@GetMapping
	public ResponseEntity<List<Movimentacao>> all() {
		return ResponseEntity.ok().body(repository.findAll());
	}
	
	@CrossOrigin
	@DeleteMapping(value = "/{id}")
	ResponseEntity<?> deleteProprietario(@PathVariable Long id) {
		repository.deleteById(id);
		return ResponseEntity.ok().build();
	}

}
