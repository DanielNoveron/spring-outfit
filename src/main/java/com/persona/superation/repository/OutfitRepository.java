package com.persona.superation.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.persona.superation.dto.OutfitDTO;
import com.persona.superation.entity.Outfit;

@Repository
public interface OutfitRepository extends JpaRepository<Outfit, Long> {

	@Query("""
		    SELECT new com.persona.superation.dto.OutfitDTO(
		        o.id,
		        o.nombreOutfit,
		        o.colorPrincipal,
		        o.categoria,
		        o.tiempoDia,
		        o.imagen,
		        o.esFavorito,
		        o.fechaUltimoUso,
		        o.fechaCreacion
		    )
		    FROM Outfit o
		    WHERE (:nombre IS NULL OR LOWER(o.nombreOutfit) LIKE LOWER(CONCAT('%', :nombre, '%')))
		      AND (:categorias IS NULL OR o.categoria IN :categorias)
		      AND (:tiempoDia IS NULL OR o.tiempoDia IN :tiempoDia)
		      AND (:colores IS NULL OR o.colorPrincipal IN :colores)
		      AND (:fechaCreacionInicio IS NULL OR o.fechaCreacion >= :fechaCreacionInicio)
		      AND (:fechaCreacionFin IS NULL OR o.fechaCreacion <= :fechaCreacionFin)
		      AND (:fechaUsoInicio IS NULL OR o.fechaUltimoUso >= :fechaUsoInicio)
		      AND (:fechaUsoFin IS NULL OR o.fechaUltimoUso <= :fechaUsoFin)
		""")
		Page<OutfitDTO> findAllByFilters(
		    @Param("nombre") String nombre,
		    @Param("categorias") List<String> categorias,
		    @Param("tiempoDia") List<String> tiempoDia,
		    @Param("fechaCreacionInicio") LocalDateTime fechaCreacionInicio,
		    @Param("fechaCreacionFin") LocalDateTime fechaCreacionFin,
		    @Param("fechaUsoInicio") LocalDateTime fechaUsoInicio,
		    @Param("fechaUsoFin") LocalDateTime fechaUsoFin,
		    @Param("colores") List<String> colores,
		    Pageable pageable
		);

	public Optional<Outfit> findByNombreOutfit(String nombre);
	
	
}
