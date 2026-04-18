package com.persona.superation.dto;

import lombok.Data;

@Data
public class ResponseSP<T> {
	
	private Integer status;
	private String shortCode;
	private String message;
	private T data;

}
