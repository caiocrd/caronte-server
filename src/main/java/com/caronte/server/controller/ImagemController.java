package com.caronte.server.controller;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController	
@RequestMapping(value = "/recursos")
public class ImagemController {
	
	@Value("${file.upload-dir}")
	private String IMAGEM_DIR;
	
	@GetMapping(value = "/imagem")
	public @ResponseBody void getImageWithMediaType(HttpServletResponse response, @RequestParam(required = false) String caminho) throws IOException {
		File file = new File(IMAGEM_DIR+"/"+caminho);
	    InputStream in = new FileInputStream(file);
	    response.setContentType(MediaType.IMAGE_JPEG_VALUE);
	    IOUtils.copy(in, response.getOutputStream());
	}

	
	@GetMapping(path = "/documento")
    public @ResponseBody void download(HttpServletResponse response, @RequestParam("caminho") String caminho) throws IOException {
		File file = new File(IMAGEM_DIR+"/"+caminho);
		InputStream in = new FileInputStream(file);
		if(FilenameUtils.getExtension(caminho).equalsIgnoreCase("pdf")){
			response.setContentType(MediaType.APPLICATION_PDF_VALUE);
		}else{
			response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		}
	    IOUtils.copy(in, response.getOutputStream());
    }
	
	
}
