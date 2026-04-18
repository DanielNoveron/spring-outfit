package com.persona.superation.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.persona.superation.entity.Prenda;

import lombok.Data;

@Data
public class OutfitDTO {
	
	private Long id;
	private String nombre;
	private String color;
	private String categoria;
	private String tiempoDia;
	private String imagen;
	private Boolean esFavorito;
	private LocalDateTime fechaUltimoUso;
	private LocalDateTime fechaCreacion;
	private LocalDateTime fechaActualización;
	
	private List<Prenda> prendasSuperiores;
	private List<Prenda> prendasInferiores;
	private List<Prenda> prendasCalzado;
	private List<Prenda> prendasAccesorios;
	
	 // Constructor necesario para JPQL
    public OutfitDTO(Long id, String nombre, String color, String categoria, String tiempoDia,
                     String imagen, Boolean esFavorito, LocalDateTime fechaUltimoUso, LocalDateTime fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.color = color;
        this.categoria = categoria;
        this.tiempoDia = tiempoDia;
        this.imagen = imagen;
        this.esFavorito = esFavorito;
        this.fechaUltimoUso = fechaUltimoUso;
        this.fechaCreacion = fechaCreacion;
    }
	

}
