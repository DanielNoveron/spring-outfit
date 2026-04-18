package com.persona.superation.handlers;

import java.net.BindException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.persona.superation.constants.SPConstants;
import com.persona.superation.dto.ResponseSP;
import com.persona.superation.exception.ExceptionCustomSP;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ExceptionCustomSP.class)
	public ResponseEntity<ResponseSP<Object>> handleExceptionCustomSP(ExceptionCustomSP ex) {
		ResponseSP<Object> response = new ResponseSP<>();
		response.setStatus(ex.getHttpStatus().value());
		response.setShortCode(ex.getShortCode());
		response.setMessage(ex.getMessage());
		response.setData(null);

		return new ResponseEntity<>(response, ex.getHttpStatus());
	}

	@ExceptionHandler({ MethodArgumentNotValidException.class, BindException.class })
	public ResponseEntity<Object> handleValidationException(Exception ex) {
		BindingResult bindingResult;

		if (ex instanceof MethodArgumentNotValidException) {
			bindingResult = ((MethodArgumentNotValidException) ex).getBindingResult();
		} else if (ex instanceof BindException) {
			bindingResult = ((org.springframework.validation.BindException) ex).getBindingResult();
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error de validación inesperado");
		}

		FieldError fieldError = (FieldError) bindingResult.getAllErrors().get(0);

		ResponseSP<Object> response = new ResponseSP<>();
		response.setData(null);
		response.setMessage(String.format("%s", fieldError.getDefaultMessage()));
		response.setShortCode(SPConstants.SHORT_CODE_ERROR);
		response.setStatus(HttpStatus.BAD_REQUEST.value());

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

}
