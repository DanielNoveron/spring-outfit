package com.persona.superation.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.persona.superation.constants.SPConstants;
import com.persona.superation.exception.ExceptionCustomSP;

@Service
public class FileStorageService {
	
	@Value("${app.uploads.base-path}")
	private String PATH_SAVE_FILE;
	
	 public String guardarArchivo(MultipartFile archivo, String subcarpeta) throws ExceptionCustomSP {
		 try {
			 if (archivo.isEmpty()) {
				 throw new ExceptionCustomSP("El archivo no está presente", HttpStatus.BAD_REQUEST, SPConstants.SHORT_CODE_ERROR);
			 }
			 String carpetaDestino = this.PATH_SAVE_FILE + (subcarpeta != null ? subcarpeta + "/" : "");
		        Files.createDirectories(Paths.get(carpetaDestino));

		        String nombreArchivo = System.currentTimeMillis() + "_" + archivo.getOriginalFilename();
		        Path rutaCompleta = Paths.get(carpetaDestino + nombreArchivo);

		        archivo.transferTo(rutaCompleta.toFile());

		        return nombreArchivo;
		 } catch (Exception e) {
			 throw new ExceptionCustomSP(e.getMessage(), HttpStatus.BAD_REQUEST, SPConstants.SHORT_CODE_ERROR);
		 }
	        
	    }
	 
	 public void eliminarArchivoAnterior(String nombreArchivoAnterior) throws ExceptionCustomSP {
		 try {
			 String pathFile = this.PATH_SAVE_FILE + nombreArchivoAnterior;
			 File file = new File(pathFile);
			 file.delete();
			 
		 } catch (Exception e) {
			 throw new ExceptionCustomSP(e.getMessage(), HttpStatus.BAD_REQUEST, SPConstants.SHORT_CODE_ERROR);
		 }
	 }

}
