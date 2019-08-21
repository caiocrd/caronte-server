package com.caronte.server.controller;

import java.io.IOException;
import java.net.URI;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.caronte.server.controller.dto.EmbarcacaoDTO;
import com.caronte.server.controller.dto.MovimentacaoDTO;
import com.caronte.server.entity.Embarcacao;
import com.caronte.server.entity.Movimentacao;
import com.caronte.server.entity.Proprietario;
import com.caronte.server.entity.TipoMovimentacao;
import com.caronte.server.repository.EmbarcacaoRepository;
import com.caronte.server.repository.MovimentacaoRepository;
import com.caronte.server.service.FileSaveService;




@RestController	
@RequestMapping("embarcacoes")
public class EmbarcacaoController {

	private final EmbarcacaoRepository repository;
	private final MovimentacaoRepository movimentacaoRepository;

	@Autowired
	private FileSaveService fileSaveService;
	
	public EmbarcacaoController(EmbarcacaoRepository repository, MovimentacaoRepository movimentacaoRepository) {
		this.repository = repository;
		this.movimentacaoRepository = movimentacaoRepository;
	}
	@CrossOrigin
	@GetMapping
	List<EmbarcacaoDTO> all(@RequestParam(required = false, defaultValue = "") String nome) {
		
		return EmbarcacaoDTO.converter(repository.findByNomeContainingIgnoreCase(nome));
	}
	
	@CrossOrigin
	@PostMapping
	ResponseEntity<?> newEmbarcacao( @ModelAttribute Embarcacao emb, UriComponentsBuilder uriBuilder) {
		
		//Salvar arquivos
		Proprietario p = new Proprietario();
		p.setId(Long.valueOf(emb.getProprietario_id()));
		emb.setProprietario(p);
		Embarcacao nova = repository.save(emb);
		 try {
		        fileSaveService.save(emb.getImagem(), nova.getId() + "_foto");	
		        fileSaveService.save(emb.getDocumento(), nova.getId() + "_documento");
		    } catch (IOException e) {
		    	System.out.println(e);
		    		return ResponseEntity.badRequest().body(null);
		    }
		 URI uri = uriBuilder.path("/embarcacoes/{id}").buildAndExpand(nova.getId()).toUri();
		 return ResponseEntity.created(uri).body(new EmbarcacaoDTO(nova));

	}
	
	@CrossOrigin
	@PostMapping("/{id}/movimentacao")
	ResponseEntity<MovimentacaoDTO> newMovimentacao(@RequestBody String ocorrencia, @PathVariable Long id, UriComponentsBuilder uriBuilder) {
		
		Embarcacao embarcacao = new Embarcacao(id);
		Movimentacao last = movimentacaoRepository.findFirstByEmbarcacaoOrderByIdDesc(embarcacao);
		Movimentacao movimentacao = new Movimentacao();
		movimentacao.setOcorrencia(ocorrencia);
		movimentacao.setEmbarcacao(embarcacao);
		movimentacao.setData(Calendar.getInstance());
		if(last == null || last.getTipo() == TipoMovimentacao.ENTRADA)
			movimentacao.setTipo(TipoMovimentacao.SAIDA);
		else
			movimentacao.setTipo(TipoMovimentacao.ENTRADA);
		URI uri = uriBuilder.path("/embarcacoes/{id}/movimentacoes").buildAndExpand(movimentacao.getId()).toUri();
		return ResponseEntity.created(uri).body(new MovimentacaoDTO(movimentacao));

	}
	
	@CrossOrigin
	@GetMapping("/{id}")
	ResponseEntity<EmbarcacaoDTO> one(@PathVariable Long id) {
		Optional<Embarcacao> embarcacao = repository.findById(id);
		if(embarcacao.isPresent())
			return ResponseEntity.ok().body(new EmbarcacaoDTO(embarcacao.get()));
		return ResponseEntity.notFound().build();
	}
	@CrossOrigin
	@PutMapping("/{id}")
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
	@DeleteMapping("/{id}")
	void deleteEmbarcacao(@PathVariable Long id) {
		repository.deleteById(id);
	}

}
