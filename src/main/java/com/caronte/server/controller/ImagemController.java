package com.caronte.server.controller;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.h2.util.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController	
public class ImagemController {
	
	private final String IMAGEM_DIR = "/Users/caiocesar/progerencial/imagem/";
	
	@RequestMapping(value = "/imagem", method = RequestMethod.GET)
	public @ResponseBody void getImageWithMediaType(HttpServletResponse response, @RequestParam(required = false) String id) throws IOException {
		File file = new File(IMAGEM_DIR+"/"+id+"_foto");
	    InputStream in = new FileInputStream(file);
	    response.setContentType(MediaType.IMAGE_JPEG_VALUE);
	    IOUtils.copy(in, response.getOutputStream());
	}
	
	@RequestMapping(value = "/documento-embarcacao", method = RequestMethod.GET)
	public @ResponseBody void getDocumentoEmbarcacao(HttpServletResponse response, @RequestParam(required = false) String id) throws IOException {
		File file = new File(IMAGEM_DIR+"/"+id+"_documento");
	    InputStream in = new FileInputStream(file);
	    response.setContentType(MediaType.MULTIPART_FORM_DATA_VALUE);
	    IOUtils.copy(in, response.getOutputStream());
	}
	
}
