package com.persona.superation.dto;

import java.time.LocalDateTime;

public interface PrendaDTO {
	
	Long getId();
	String getNombre();
	String getColor();
	String getImagen();
	String getTipoPrenda();
	LocalDateTime getFechaCreacion();
	LocalDateTime getFechaActualizacion();

}
