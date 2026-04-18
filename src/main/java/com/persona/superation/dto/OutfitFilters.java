package com.persona.superation.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class OutfitFilters {

	private String nombre;
	private List<String> colores;
	private List<String> tiempoDia;
	private List<String> categorias;
	private Date fechaCreacion;
	private Date fechaUltimoUso;

}
