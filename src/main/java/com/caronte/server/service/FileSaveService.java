package com.caronte.server.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSaveService {
	private final String IMAGEM_DIR = "/Users/caiocesar/progerencial/imagem/";
	
	public void save(MultipartFile file, String nome) throws IOException {
		 if (file.isEmpty()) {
	            return; //next pls
	        }

	        byte[] bytes = file.getBytes();
	        Path path = Paths.get(IMAGEM_DIR + nome);
	        Files.write(path, bytes);
	}
	
	
}
