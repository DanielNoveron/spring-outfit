package com.persona.superation.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.persona.superation.dto.ResponseSP;

public class SPUtils {

	public static <T> ResponseSP<T> generateResponseService(T data, String message, String shortCode, Integer status) {
		ResponseSP<T> responseService = new ResponseSP<>();
		
		responseService.setMessage(message);
		responseService.setShortCode(shortCode);
		responseService.setStatus(status);
		responseService.setData(data);
		
		return responseService;
	}
	
	public static <T> ResponseEntity<T> generateResponseController(T data, Integer status) {
		return ResponseEntity.status(HttpStatus.resolve(status)).body(data);
	} 
}
