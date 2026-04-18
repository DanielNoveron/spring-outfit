package com.persona.superation.dto;


import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PrendaCreateEditRequest {

	@Max(99999999)
	private Long id;

	@NotBlank(message = "El nombre es obligatorio")
	@Size(min = 1, max = 100, message = "El nombre no debe exceder los 100 caracteres")
	private String nombre;

	@NotBlank(message = "El color es obligatorio")
	@Size(min = 1, max = 50, message = "El color no debe exceder los 50 caracteres")
	private String color;

	@NotBlank(message = "El tipoPrenda es obligatorio")
	@Size(min = 1, max = 50, message = "El tipo de prenda no debe exceder los 50 caracteres")
	private String tipoPrenda;

	private MultipartFile imagen;
	
	
}
