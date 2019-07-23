package com.caronte.server.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.caronte.server.entity.Embarcacao;
import com.caronte.server.exception.EmbarcacaoNotFoundExceprion;
import com.caronte.server.repository.EmbarcacaoRepository;

@RestController	
public class EmbarcacaoController {

	private final EmbarcacaoRepository repository;

	public EmbarcacaoController(EmbarcacaoRepository repository) {
		this.repository = repository;
	}

	@RequestMapping(value = "/embarcacaos", method = RequestMethod.GET)
	List<Embarcacao> all() {
		return repository.findAll();
	}

	@RequestMapping(value = "/embarcacaos", method = RequestMethod.POST)
	Embarcacao newProprieteario(@RequestBody Embarcacao embarcacao) {
		return repository.save(embarcacao);
	}

	@RequestMapping(value = "/embarcacaos/{id}", method = RequestMethod.GET)
	Embarcacao one(@PathVariable Long id) {

		return repository.findById(id).orElseThrow(() -> new EmbarcacaoNotFoundExceprion(id));
	}

	@RequestMapping(value = "/embarcacao/{id}", method = RequestMethod.PUT)
	Embarcacao replaceEmbarcacao(@RequestBody Embarcacao newEmbarcacao, @PathVariable Long id) {

		return repository.findById(id).map(embarcacao -> {
			embarcacao.setNome(newEmbarcacao.getNome());
			return repository.save(embarcacao);
		}).orElseGet(() -> {
			newEmbarcacao.setId(id);
			return repository.save(newEmbarcacao);
		});
	}

	@RequestMapping(value = "/embarcacao/{id}", method = RequestMethod.DELETE)
	void deleteEmbarcacao(@PathVariable Long id) {
		repository.deleteById(id);
	}

}
