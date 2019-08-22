package com.caronte.server.controller;

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
@RequestMapping(value = "/proprietarios")
public class ProprietarioController {

	private final ProprietarioRepository repository;
	
	@Autowired
	private FileSaveService fileSaveService;

	public ProprietarioController(ProprietarioRepository repository) {
		this.repository = repository;
	}
	@CrossOrigin
	@GetMapping
	List<Proprietario> all(@RequestParam(required = false) String nome) {
		ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny()
			      .withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		Proprietario emb = new Proprietario(nome);
	    Example<Proprietario> examples = Example.of(emb, customExampleMatcher);
		return repository.findAll(examples);
	}
	@CrossOrigin
	@PostMapping
	ResponseEntity<?> newProprieteario(@ModelAttribute Proprietario proprietario) {
		Proprietario nova = repository.save(proprietario);
		proprietario.setCaminhoHabilitacao(nova.getId() + "_habilitacao." + FilenameUtils.getExtension(proprietario.getHabilitacao().getOriginalFilename()));;
		proprietario.setCaminhoDocumento(nova.getId() + "_documento_proprietario." + FilenameUtils.getExtension(proprietario.getDocumento().getOriginalFilename()));;
		 try {
		        fileSaveService.save(nova.getDocumento(), proprietario.getCaminhoDocumento());	
		        fileSaveService.save(nova.getHabilitacao(), proprietario.getCaminhoHabilitacao());
		        repository.save(proprietario);
		    } catch (IOException e) {
		    	System.out.println(e);
		    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		    }
		 return new ResponseEntity<>(HttpStatus.CREATED);
	}
	@CrossOrigin
	@GetMapping("{id}")
	Proprietario one(@PathVariable Long id) {
		Proprietario retorno = repository.findById(id).orElseThrow(() -> new ProprietarioNotFoundExceprion(id));
		retorno.getEmbarcacoes().size();
		return retorno;
	}
	@CrossOrigin
	@PutMapping("{id}")
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
	@DeleteMapping("{id}")
	void deleteProprietario(@PathVariable Long id) {
		repository.deleteById(id);
	}

}
