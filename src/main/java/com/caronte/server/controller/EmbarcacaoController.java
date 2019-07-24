package com.caronte.server.controller;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
	@CrossOrigin
	@RequestMapping(value = "/embarcacoes", method = RequestMethod.GET)
	List<Embarcacao> all(@RequestParam(required = false) String nome) {
		ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny()
			      .withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		Embarcacao emb = new Embarcacao(nome);
	    Example<Embarcacao> examples = Example.of(emb, customExampleMatcher);
		return repository.findAll(examples);
	}
	
	@CrossOrigin
	@RequestMapping(value = "/embarcacoes/{id}", method = RequestMethod.GET)
	Embarcacao one(@PathVariable Long id) {

		return repository.findById(id).orElseThrow(() -> new EmbarcacaoNotFoundExceprion(id));
	}
	@CrossOrigin
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
	@CrossOrigin
	@RequestMapping(value = "/embarcacao/{id}", method = RequestMethod.DELETE)
	void deleteEmbarcacao(@PathVariable Long id) {
		repository.deleteById(id);
	}

}
