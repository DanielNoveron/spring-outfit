package com.persona.superation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeleteGenericRequest {
	
	@NotNull(message="El id es requerido")
	@Min(value = 1, message = "El id debe ser positivo")
	private Long id;

}
 