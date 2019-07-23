package com.caronte.server.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.caronte.server.entity.Proprietario;
import com.caronte.server.exception.ProprietarioNotFoundExceprion;
import com.caronte.server.repository.ProprietarioRepository;

@RestController	
public class ProprietarioController {

	private final ProprietarioRepository repository;

	public ProprietarioController(ProprietarioRepository repository) {
		this.repository = repository;
	}

	@RequestMapping(value = "/proprietarios", method = RequestMethod.GET)
	List<Proprietario> all() {
		return repository.findAll();
	}

	@RequestMapping(value = "/proprietarios", method = RequestMethod.POST)
	Proprietario newProprieteario(@RequestBody Proprietario proprietario) {
		return repository.save(proprietario);
	}

	@RequestMapping(value = "/proprietarios/{id}", method = RequestMethod.GET)
	Proprietario one(@PathVariable Long id) {

		return repository.findById(id).orElseThrow(() -> new ProprietarioNotFoundExceprion(id));
	}

	@RequestMapping(value = "/proprietario/{id}", method = RequestMethod.PUT)
	Proprietario replaceProprietario(@RequestBody Proprietario newProprietario, @PathVariable Long id) {

		return repository.findById(id).map(proprietario -> {
			proprietario.setNome(newProprietario.getNome());
			return repository.save(proprietario);
		}).orElseGet(() -> {
			newProprietario.setId(id);
			return repository.save(newProprietario);
		});
	}

	@RequestMapping(value = "/proprietario/{id}", method = RequestMethod.DELETE)
	void deleteProprietario(@PathVariable Long id) {
		repository.deleteById(id);
	}

}
