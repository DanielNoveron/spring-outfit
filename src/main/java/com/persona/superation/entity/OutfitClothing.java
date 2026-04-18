package com.persona.superation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "outfit_clothings")
public class OutfitClothing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con Outfit
    @Column(name = "id_outfit", nullable = false)
    private Long outfit;

    // Relación con Prenda
    @Column(name = "id_prenda", nullable = false)
    private Long prenda;
    
    @Column(name = "tipo_prenda_clothing_id")
    private String tipoPrendaClothingId;

}
