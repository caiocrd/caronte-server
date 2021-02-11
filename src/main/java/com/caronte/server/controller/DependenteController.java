package com.caronte.server.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.caronte.server.entity.Dependente;
import com.caronte.server.repository.DependenteRepository;
import com.caronte.server.service.FileSaveService;

@RestController	
@RequestMapping("dependentes")
public class DependenteController {

	private final DependenteRepository repository;
	@Autowired
	private FileSaveService fileSaveService;


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
	ResponseEntity<?> deleteDependente(@PathVariable Long id) {
		Dependente dependente = repository.findById(id).get();
		try {
			fileSaveService.remove(dependente.getCaminhoDocumento());
			fileSaveService.remove(dependente.getCaminhoHabilitacao());

		} catch (IOException e) {
			System.out.println("Nao conseguiu deletar os arquivos");
		}
		repository.deleteById(id);
		return ResponseEntity.ok().build();
	}

}
