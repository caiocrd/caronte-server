package com.caronte.server.controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.caronte.server.entity.Embarcacao;
import com.caronte.server.entity.Movimentacao;
import com.caronte.server.entity.Proprietario;
import com.caronte.server.entity.TipoMovimentacao;
import com.caronte.server.exception.EmbarcacaoNotFoundExceprion;
import com.caronte.server.repository.EmbarcacaoRepository;
import com.caronte.server.repository.MovimentacaoRepository;
import com.caronte.server.service.FileSaveService;




@RestController	
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
	@RequestMapping(value = "/embarcacoes", method = RequestMethod.GET)
	List<Embarcacao> all(@RequestParam(required = false) String nome) {
		ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny()
			      .withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		Embarcacao emb = new Embarcacao(nome);
	    Example<Embarcacao> examples = Example.of(emb, customExampleMatcher);
		return repository.findAll(examples);
	}
	
	@CrossOrigin
	@RequestMapping(value = "/embarcacoes", method = RequestMethod.POST)
	@Transactional
	ResponseEntity<?> newEmbarcacao( @ModelAttribute Embarcacao emb) {
		
		//Salvar arquivos
		Proprietario p = new Proprietario();
		p.setId(Long.valueOf(emb.getProprietario_id()));
		emb.setProprietario(p);
		
		Embarcacao nova = repository.save(emb);
		emb.setCaminhoImagem(nova.getId() + "_foto." + FilenameUtils.getExtension(emb.getImagem().getOriginalFilename()));;
		emb.setCaminhoDocumento(nova.getId() + "_documento." + FilenameUtils.getExtension(emb.getDocumento().getOriginalFilename()));;
		 try {
		        fileSaveService.save(emb.getImagem(), emb.getCaminhoImagem());	
		        fileSaveService.save(emb.getDocumento(), emb.getCaminhoDocumento());
		        repository.save(emb);
		    } catch (IOException e) {
		    	System.out.println(e);
		    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		    }
		 return new ResponseEntity<>(HttpStatus.CREATED);

	}
	
	@CrossOrigin
	@RequestMapping(value = "/embarcacao/{id}/movimentacao", method = RequestMethod.POST)
	Movimentacao newMovimentacao(@RequestBody String ocorrencia, @PathVariable Long id) {
		
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

		return movimentacaoRepository.save(movimentacao);

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
