package com.persona.superation.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OutfitCreateEditRequest {

	@Max(99999999)
	private Long id;

	@NotBlank(message = "El nombre es obligatorio")
	@Size(min = 1, max = 100, message = "El nombre no debe exceder los 100 caracteres")
	private String nombre;

	@NotBlank(message = "El color es obligatorio")
	private String color;

	@NotBlank(message = "La categoria es obligatoria")
	private String categoria;
	
	@NotBlank(message = "El tiempo del día es obligatoria")
	private String tiempoDia;

	private MultipartFile imagen;
	
	@Size(min = 1, message = "Debe haber minimo una prenda superior")
	private List<Long> prendasSuperiores; // UNICAMENTE SERÁN LOS ID DE LAS PRENDAS
	
	@Size(min = 1, message = "Debe haber minimo una prenda inferior")
	private List<Long> prendasInferiores; // UNICAMENTE SERÁN LOS ID DE LAS PRENDAS
	
	@Size(min = 1, message = "Debe haber minimo un calzado")
	private List<Long> prendasCalzado; // UNICAMENTE SERÁN LOS ID DE LAS PRENDAS
	
	@Size(min = 1, message = "Debe haber minimo un accesorio")
	private List<Long> prendasAccesorios; // UNICAMENTE SERÁN LOS ID DE LAS PRENDAS
	
	
}
