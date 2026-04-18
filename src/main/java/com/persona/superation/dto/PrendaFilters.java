package com.persona.superation.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class PrendaFilters {
	
	private String nombre;
	private List<String> colores;
	private List<String> tipoPrenda;
	private Date fechaCreacion;
	private Date fechaActualizacion;

}
