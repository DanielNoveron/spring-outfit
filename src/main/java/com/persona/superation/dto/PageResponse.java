package com.persona.superation.dto;

import lombok.Data;

@Data
public class PageResponse<T> {
	
	private Integer elementsPage;
	private Integer totalPages;
	private Long totalElements;
	private Integer currentPage;
	private T content;

}
