package com.caronte.server.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSaveService {
	
	@Value("${file.upload-dir}")
	private String IMAGEM_DIR;;
	
	public void save(MultipartFile file, String nome) throws IOException {
		 if (file.isEmpty()) {
	            return; //next pls
	        }
	        byte[] bytes = file.getBytes();
	        Path path = Paths.get(IMAGEM_DIR + nome);
	        Files.write(path, bytes);

	}
	public void remove(String nome) throws IOException {
		Path path = Paths.get(IMAGEM_DIR + nome);
		System.out.println(path);
		Files.delete(path);
	}
	
	public void createImageFromPdf(String pdfFileName) {
		try {
	        String sourceDir = IMAGEM_DIR + pdfFileName; // Pdf files are read from this folder

	        File sourceFile = new File(sourceDir);
	        File destinationFile = new File(IMAGEM_DIR);
	       
	        if (sourceFile.exists()) {
	            System.out.println("Images copied to Folder: "+ destinationFile.getName());             
	            PDDocument document = PDDocument.load(sourceDir);
	            List<PDPage> list = document.getDocumentCatalog().getAllPages();
	            System.out.println("Total files to be converted -> "+ list.size());

	            String fileName = sourceFile.getName().replace(".pdf", "");             
	            int pageNumber = 1;
	            for (PDPage page : list) {
	                BufferedImage image = page.convertToImage();
	                File outputfile = new File(IMAGEM_DIR + fileName + ".png");
	                System.out.println("Image Created -> "+ outputfile.getName());
	                ImageIO.write(image, "png", outputfile);
	                pageNumber++;
	                break;
	            }
	            document.close();
	            System.out.println("Converted Images are saved at -> "+ destinationFile.getAbsolutePath());
	        } else {
	            System.err.println(sourceFile.getName() +" File not exists");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	
}
