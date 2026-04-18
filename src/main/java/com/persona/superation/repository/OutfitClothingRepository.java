package com.persona.superation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.persona.superation.entity.OutfitClothing;
import com.persona.superation.entity.Prenda;

@Repository
public interface OutfitClothingRepository extends JpaRepository<OutfitClothing, Long> {

	 @Query(value = """
		        SELECT id_outfit
		        FROM outfit_clothings
		        GROUP BY id_outfit
		        HAVING GROUP_CONCAT(id_prenda ORDER BY id_prenda ASC) = :prendas
		        """, nativeQuery = true)
	public List<Long> findOutfitsWithSamePrendas(@Param("prendas") String prendasOrdenadas);
	 
	public Optional<OutfitClothing> findByOutfitAndPrenda(Long idOutfit, Long idPrenda);
	
	@Query("SELECT o.prenda FROM OutfitClothing o WHERE o.outfit = :outfitId")
	List<Long> findPrendaIdsByOutfit(@Param("outfitId") Long outfitId);
	
	
	@Query("SELECT p.id, p.tipoPrenda FROM Prenda p WHERE p.id IN :ids")
	public List<Object[]> findTiposByIds(@Param("ids") List<Long> ids);
	
	
	List<OutfitClothing> findByOutfit(Long idOutfit);
	

}
