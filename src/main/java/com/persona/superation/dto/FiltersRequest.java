package com.persona.superation.dto;

import lombok.Data;

@Data
public class FiltersRequest<T> {

	private Integer page;
	private Integer size;
	private T filters;
	
}
