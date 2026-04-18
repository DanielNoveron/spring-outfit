package com.persona.superation.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "outfits")
public class Outfit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_outfit", nullable = false, length = 255)
    private String nombreOutfit;

    @Column(nullable = false, length = 255)
    private String imagen;
    
    @Column(nullable = false, length = 400, name="tiempo_dia")
    private String tiempoDia;
    
    @Column(nullable = false, length = 400)
    private String categoria;

    @Column(name = "es_favorito")
    private Boolean esFavorito = false;

    @Column(name = "color_principal", nullable = false, length = 50)
    private String colorPrincipal;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_ultimo_uso", nullable = false)
    private LocalDateTime fechaUltimoUso;

    
}