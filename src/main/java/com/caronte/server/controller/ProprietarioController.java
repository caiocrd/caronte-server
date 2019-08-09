package com.caronte.server.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.caronte.server.entity.Proprietario;
import com.caronte.server.exception.ProprietarioNotFoundExceprion;
import com.caronte.server.repository.ProprietarioRepository;
import com.caronte.server.service.FileSaveService;

@RestController	
public class ProprietarioController {

	private final ProprietarioRepository repository;
	
	@Autowired
	private FileSaveService fileSaveService;

	public ProprietarioController(ProprietarioRepository repository) {
		this.repository = repository;
	}
	@CrossOrigin
	@RequestMapping(value = "/proprietarios", method = RequestMethod.GET)
	List<Proprietario> all(@RequestParam(required = false) String nome) {
		ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny()
			      .withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		Proprietario emb = new Proprietario(nome);
	    Example<Proprietario> examples = Example.of(emb, customExampleMatcher);
		return repository.findAll(examples);
	}
	@CrossOrigin
	@RequestMapping(value = "/proprietarios", method = RequestMethod.POST)
	ResponseEntity<?> newProprieteario(@RequestBody Proprietario proprietario) {
		Proprietario nova = repository.save(proprietario);
		 try {
		        fileSaveService.save(nova.getDocumento(), nova.getId() + "_documento_proprietario");	
		        fileSaveService.save(nova.getHabilitacao(), nova.getId() + "_habilitacao");
		    } catch (IOException e) {
		    	System.out.println(e);
		    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		    }
		 return new ResponseEntity<>(HttpStatus.CREATED);
	}
	@CrossOrigin
	@RequestMapping(value = "/proprietarios/{id}", method = RequestMethod.GET)
	Proprietario one(@PathVariable Long id) {
		Proprietario retorno = repository.findById(id).orElseThrow(() -> new ProprietarioNotFoundExceprion(id));
		retorno.getEmbarcacoes().size();
		return retorno;
	}
	@CrossOrigin
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
	@CrossOrigin
	@RequestMapping(value = "/proprietario/{id}", method = RequestMethod.DELETE)
	void deleteProprietario(@PathVariable Long id) {
		repository.deleteById(id);
	}

}
