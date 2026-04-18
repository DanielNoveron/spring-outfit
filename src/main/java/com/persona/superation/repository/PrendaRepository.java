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

import com.persona.superation.dto.PrendaDTO;
import com.persona.superation.entity.Prenda;

@Repository
public interface PrendaRepository extends JpaRepository<Prenda, Long> {

	@Query("""
			    SELECT p FROM Prenda p
			    WHERE (:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
			    AND (:tipoPrenda IS NULL OR p.tipoPrenda IN :tipoPrenda)
			      AND (
			        (:fechaCreacionInicio IS NULL OR :fechaCreacionFin IS NULL)
			        OR (p.fechaCreacion BETWEEN :fechaCreacionInicio AND :fechaCreacionFin)
			      )
			      AND (
			        (:fechaActualizacionInicio IS NULL OR :fechaActualizacionFin IS NULL)
			        OR (p.fechaActualizacion BETWEEN :fechaActualizacionInicio AND :fechaActualizacionFin)
			      )
			      AND (:colores IS NULL OR p.color IN :colores)
			""")
	Page<PrendaDTO> findAllByFilters(@Param("nombre") String nombre, @Param("tipoPrenda") List<String> tipoPrenda,
			@Param("fechaCreacionInicio") LocalDateTime  fechaCreacionInicio, @Param("fechaCreacionFin") LocalDateTime  fechaCreacionFin,
			@Param("fechaActualizacionInicio") LocalDateTime  fechaActualizacionInicio,
			@Param("fechaActualizacionFin") LocalDateTime  fechaActualizacionFin, @Param("colores") List<String> colores,
			Pageable pageable);

	public Optional<Prenda> findByNombre(String nombre);
	
	public List<Prenda> findByIdIn(List<Long> ids);


}
