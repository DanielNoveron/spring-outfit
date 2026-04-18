package com.persona.superation.exception;

import org.springframework.http.HttpStatus;

public class ExceptionCustomSP extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	private final HttpStatus httpStatus;
	private final String shortCode;

	public ExceptionCustomSP(String message, HttpStatus httpStatus, String shortCode) {
		super(message);
		this.httpStatus = httpStatus;
		this.shortCode = shortCode;
	}
	

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public String getShortCode() {
		return shortCode;
	}
}
