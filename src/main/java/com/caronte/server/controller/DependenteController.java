package com.caronte.server.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.caronte.server.entity.Dependente;
import com.caronte.server.repository.DependenteRepository;

@RestController	
@RequestMapping("dependentes")
public class DependenteController {

	private final DependenteRepository repository;

	public DependenteController(DependenteRepository rep) {
		this.repository = rep;
	}
	
	@CrossOrigin
	@GetMapping
	public ResponseEntity<List<Dependente>> all() {
		return ResponseEntity.ok().body(repository.findAll());
	}
	
	@CrossOrigin
	@DeleteMapping(value = "/{id}")
	ResponseEntity<?> deleteProprietario(@PathVariable Long id) {
		repository.deleteById(id);
		return ResponseEntity.ok().build();
	}

}
