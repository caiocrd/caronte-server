package com.caronte.server.controller;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.caronte.server.entity.Dependente;
import com.caronte.server.entity.Proprietario;
import com.caronte.server.entity.User;
import com.caronte.server.exception.ProprietarioNotFoundExceprion;
import com.caronte.server.repository.DependenteRepository;
import com.caronte.server.repository.ProprietarioRepository;
import com.caronte.server.service.FileSaveService;
import com.caronte.server.service.UserService;

@RestController	
@RequestMapping(value = "/proprietarios")
public class ProprietarioController {

	private final ProprietarioRepository repository;
	
	@Autowired
	private DependenteRepository dependenteRepository;

	@Autowired
	private FileSaveService fileSaveService;
	
	@Autowired
	private UserService userService;

	public ProprietarioController(ProprietarioRepository repository) {
		this.repository = repository;
	}
	@CrossOrigin
	@GetMapping
	List<Proprietario> all(@RequestParam(required = false, defaultValue = "") String nome) {
		
		return repository.findByNomeContainingIgnoreCaseOrderByNomeAsc(nome);
	}
	@CrossOrigin
	@PostMapping
	ResponseEntity<?> newProprieteario(@ModelAttribute Proprietario proprietario) {
		
		proprietario.getUser().setName(proprietario.getNome());
		proprietario.getUser().setUsername(proprietario.getUser().getEmail());
		proprietario.setUser(this.userService.createProprietarioUser(proprietario.getUser()));
		Proprietario nova = repository.save(proprietario);
		
		 try {
			if(proprietario.getDocumento() != null) {
				String extensaoDocumento = FilenameUtils.getExtension(proprietario.getDocumento().getOriginalFilename());
				proprietario.setCaminhoDocumento(nova.getId() + "_documento_proprietario." + extensaoDocumento);
				proprietario.setCaminhoDocumentoPng(proprietario.getCaminhoDocumento());
				fileSaveService.save(nova.getDocumento(), proprietario.getCaminhoDocumento());	
				if(extensaoDocumento.equalsIgnoreCase("pdf")) {
					fileSaveService.createImageFromPdf(proprietario.getCaminhoDocumento());
					proprietario.setCaminhoDocumentoPng(nova.getId() + "_documento_proprietario.png");
				}	
			}
			if(proprietario.getHabilitacao() != null) {
				String extensaoHabilitacao = FilenameUtils.getExtension(proprietario.getHabilitacao().getOriginalFilename());
				proprietario.setCaminhoHabilitacao(nova.getId() + "_habilitacao." + extensaoHabilitacao);
				proprietario.setCaminhoHabilitacaoPng(proprietario.getCaminhoHabilitacaoPng());
				fileSaveService.save(nova.getHabilitacao(), proprietario.getCaminhoHabilitacao());
				if(extensaoHabilitacao.equalsIgnoreCase("pdf")) {
					fileSaveService.createImageFromPdf(proprietario.getCaminhoHabilitacao());
					proprietario.setCaminhoHabilitacaoPng(nova.getId() + "_habilitacao.png");
				}
			}
	        repository.save(proprietario);
	    } catch (IOException e) {
	    	System.out.println(e);
	    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }
		 return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@CrossOrigin
	@PostMapping("/{id}/dependente")
	ResponseEntity<Dependente> newDependente(@ModelAttribute Dependente dependente, @PathVariable Long id, UriComponentsBuilder uriBuilder) {
		Proprietario titular = new Proprietario(id);
		dependente.setTitular(titular);
		Dependente novo = dependenteRepository.save(dependente);
		if(dependente.getDocumento() != null && dependente.getHabilitacao() != null){
			try {
				novo.setCaminhoHabilitacao(novo.getId() + "_habilitacao_dependente." + FilenameUtils.getExtension(dependente.getHabilitacao().getOriginalFilename()));;
				novo.setCaminhoDocumento(novo.getId() + "_documento_dependente." + FilenameUtils.getExtension(dependente.getDocumento().getOriginalFilename()));;
				fileSaveService.save(dependente.getDocumento(), novo.getCaminhoDocumento());	
				fileSaveService.save(dependente.getHabilitacao(), novo.getCaminhoHabilitacao());
				dependenteRepository.save(novo);
			} catch (IOException e) {
				System.out.println(e);
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
		URI uri = uriBuilder.path("/embarcacoes/{id}/movimentacoes").buildAndExpand(dependente.getId()).toUri();
		return ResponseEntity.created(uri).body(novo);

	}

	@CrossOrigin
	@GetMapping("{id}")
	Proprietario one(@PathVariable Long id) {
		Proprietario retorno = repository.findById(id).orElseThrow(() -> new ProprietarioNotFoundExceprion(id));
		retorno.getEmbarcacoes().size();
		return retorno;
	}
	
	@CrossOrigin
	@GetMapping("/me")
	Proprietario perfil() {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = "";
		if (principal instanceof UserDetails) {
		   username = ((UserDetails)principal).getUsername();
		} else {
		   username = principal.toString();
		}
		Proprietario retorno = repository.findByUserUsername(username).orElseThrow(() -> new ProprietarioNotFoundExceprion(0l));
		retorno.getEmbarcacoes().size();
		return retorno;
	}
	
	@CrossOrigin
	@PutMapping("/{id}")
	ResponseEntity<?> replaceProprietario(@ModelAttribute Proprietario newProprietario, @PathVariable Long id, UriComponentsBuilder uriBuilder) {
		
		Optional<Proprietario> proprietarioOptional = repository.findById(id);
		if(proprietarioOptional.isPresent()){
			Proprietario proprietario = proprietarioOptional.get();
			proprietario.setNome(newProprietario.getNome());
			
			try {
				if(newProprietario.getDocumento() != null){
					proprietario.setCaminhoDocumento(proprietario.getId() + "_documento_proprietario." + FilenameUtils.getExtension(newProprietario.getDocumento().getOriginalFilename()));;
					proprietario.setCaminhoDocumentoPng(proprietario.getCaminhoDocumento());
					fileSaveService.save(newProprietario.getDocumento(), proprietario.getCaminhoDocumento());
					if(FilenameUtils.getExtension(newProprietario.getDocumento().getOriginalFilename()).equalsIgnoreCase("pdf")) {
						fileSaveService.createImageFromPdf(proprietario.getCaminhoDocumento());
						proprietario.setCaminhoDocumentoPng(proprietario.getId() + "_documento_proprietario.png");;
					}

				}
				if(newProprietario.getHabilitacao() != null){
					proprietario.setCaminhoHabilitacao(proprietario.getId() + "_habilitacao." + FilenameUtils.getExtension(newProprietario.getHabilitacao().getOriginalFilename()));;
					fileSaveService.save(newProprietario.getHabilitacao(), proprietario.getCaminhoHabilitacao());
					if(FilenameUtils.getExtension(newProprietario.getHabilitacao().getOriginalFilename()).equalsIgnoreCase("pdf")) {
						fileSaveService.createImageFromPdf(proprietario.getCaminhoHabilitacao());
						proprietario.setCaminhoHabilitacaoPng(proprietario.getId() + "_habilitacao.png");;
					}
				}
			} catch (IOException e) {
		    	System.out.println(e);
		    	return ResponseEntity.badRequest().body(null);
			}
			repository.save(proprietario);
			
		}
		URI uri = uriBuilder.path("/embarcacoes/{id}").buildAndExpand(id).toUri();
		return ResponseEntity.created(uri).build();
	}
	@CrossOrigin
	@DeleteMapping("{id}")
	ResponseEntity<?> deleteProprietario(@PathVariable Long id) {
		Proprietario proprietario = repository.findById(id).get();
		try {
			fileSaveService.remove(proprietario.getCaminhoDocumento());
			fileSaveService.remove(proprietario.getCaminhoHabilitacao());

		} catch (IOException e) {
			return ResponseEntity.badRequest().body(null);
		}
		repository.deleteById(id);
		return ResponseEntity.ok().build();
		
	}

}
