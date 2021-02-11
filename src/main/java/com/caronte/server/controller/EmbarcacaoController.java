package com.caronte.server.controller;

import java.io.IOException;
import java.net.URI;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FilenameUtils;
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
	List<Embarcacao> all(@RequestParam(required = false, defaultValue = "") String nome) {
		
		return repository.findByNomeContainingIgnoreCaseOrderByNomeAsc(nome);
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
			if(emb.getDocumento() != null) {
				emb.setCaminhoDocumento(nova.getId() + "_documento." + FilenameUtils.getExtension(emb.getDocumento().getOriginalFilename()));
				emb.setCaminhoDocumentoPng(emb.getCaminhoDocumento());
				fileSaveService.save(emb.getDocumento(), emb.getCaminhoDocumento());
				if(FilenameUtils.getExtension(emb.getDocumento().getOriginalFilename()).equalsIgnoreCase("pdf")) {
					emb.setCaminhoDocumentoPng(nova.getId() + "_documento." +  "png");
					fileSaveService.createImageFromPdf(emb.getCaminhoDocumento());
				}
			}
			if(emb.getImagem() != null) {
				emb.setCaminhoImagem(nova.getId() + "_foto." + FilenameUtils.getExtension(emb.getImagem().getOriginalFilename()));
				fileSaveService.save(emb.getImagem(), emb.getCaminhoImagem());	
			}
		        repository.save(emb);
	    } catch (IOException e) {
	    	System.out.println(e);
	    		return ResponseEntity.badRequest().body(null);
	    }
		URI uri = uriBuilder.path("/embarcacoes/{id}").buildAndExpand(nova.getId()).toUri();
		return ResponseEntity.created(uri).body(nova);

	}
	
	@CrossOrigin
	@PostMapping("/{id}/movimentacao")
	ResponseEntity<Movimentacao> newMovimentacao(@RequestBody Movimentacao movimentacao, @PathVariable Long id, UriComponentsBuilder uriBuilder) {
		
		Embarcacao embarcacao = new Embarcacao(id);
		movimentacao.setEmbarcacao(embarcacao);
		movimentacao.setData(Calendar.getInstance());
		if(movimentacao.getOcorrencia())
			movimentacao.setTipo(TipoMovimentacao.OCORRENCIA);
		else{
			Movimentacao last = movimentacaoRepository.findFirstByEmbarcacaoAndTipoNotOrderByIdDesc(embarcacao, TipoMovimentacao.OCORRENCIA);
			if(last == null || last.getTipo() == TipoMovimentacao.ENTRADA)
				movimentacao.setTipo(TipoMovimentacao.SAIDA);
			else
				movimentacao.setTipo(TipoMovimentacao.ENTRADA);
		}
		movimentacaoRepository.save(movimentacao);
		URI uri = uriBuilder.path("/embarcacoes/{id}/movimentacoes").buildAndExpand(movimentacao.getId()).toUri();
		return ResponseEntity.created(uri).body(movimentacao);

	}
	
	@CrossOrigin
	@GetMapping("/{id}")
	ResponseEntity<Embarcacao> one(@PathVariable Long id) {
		Optional<Embarcacao> embarcacao = repository.findById(id);
		if(embarcacao.isPresent())
			return ResponseEntity.ok().body(embarcacao.get());
		return ResponseEntity.notFound().build();
	}
	@CrossOrigin
	@PutMapping("/{id}")
	ResponseEntity<?> replaceEmbarcacao(@ModelAttribute Embarcacao newEmbarcacao, @PathVariable Long id, UriComponentsBuilder uriBuilder) {
		
		Optional<Embarcacao> embarcacaoOptional = repository.findById(id);
		if(embarcacaoOptional.isPresent()){
			Embarcacao embarcacao = embarcacaoOptional.get();
			embarcacao.setNome(newEmbarcacao.getNome());
			embarcacao.setDescricao(newEmbarcacao.getDescricao());
			Proprietario p = new Proprietario();
			p.setId(Long.valueOf(newEmbarcacao.getProprietario_id()));
			embarcacao.setProprietario(p);
			try {
				if(newEmbarcacao.getDocumento() != null){
					embarcacao.setCaminhoDocumento(embarcacao.getId() + "_documento." + FilenameUtils.getExtension(newEmbarcacao.getDocumento().getOriginalFilename()));;
					embarcacao.setCaminhoDocumentoPng(embarcacao.getCaminhoDocumento());
					fileSaveService.save(newEmbarcacao.getDocumento(), embarcacao.getCaminhoDocumento());	
					if(FilenameUtils.getExtension(newEmbarcacao.getDocumento().getOriginalFilename()).equalsIgnoreCase("pdf")) {
						fileSaveService.createImageFromPdf(embarcacao.getCaminhoDocumento());
						embarcacao.setCaminhoDocumentoPng(embarcacao.getId() + "_documento.png");
					}
				}
				if(newEmbarcacao.getImagem() != null){
					embarcacao.setCaminhoImagem(embarcacao.getId() + "_foto." + FilenameUtils.getExtension(newEmbarcacao.getImagem().getOriginalFilename()));;
					fileSaveService.save(newEmbarcacao.getImagem(), embarcacao.getCaminhoImagem());	
				}
			} catch (IOException e) {
		    	System.out.println(e);
		    		return ResponseEntity.badRequest().body(null);
			}
			repository.save(embarcacao);
			
		}
		URI uri = uriBuilder.path("/embarcacoes/{id}").buildAndExpand(id).toUri();
		return ResponseEntity.created(uri).build();
	}
	@CrossOrigin
	@DeleteMapping("/{id}")
	ResponseEntity<?> deleteEmbarcacao(@PathVariable Long id) {
		Embarcacao embarcacao = repository.findById(id).get();
		try {
			fileSaveService.remove(embarcacao.getCaminhoDocumento());
			fileSaveService.remove(embarcacao.getCaminhoImagem());
			if(!embarcacao.getCaminhoDocumento().equals(embarcacao.getCaminhoDocumentoPng())) {
				fileSaveService.remove(embarcacao.getCaminhoDocumentoPng());
			}

		} catch (IOException e) {
			System.out.println("NO IMAGE TO DELETE");
		}
		repository.deleteById(id);
		return ResponseEntity.ok().build();
	}

}
