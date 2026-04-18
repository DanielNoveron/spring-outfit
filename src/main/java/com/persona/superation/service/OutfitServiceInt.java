package com.persona.superation.service;

import java.util.List;

import com.persona.superation.dto.DeleteGenericRequest;
import com.persona.superation.dto.FiltersRequest;
import com.persona.superation.dto.OutfitCreateEditRequest;
import com.persona.superation.dto.OutfitDTO;
import com.persona.superation.dto.OutfitFilters;
import com.persona.superation.dto.PageResponse;
import com.persona.superation.dto.ResponseSP;
import com.persona.superation.exception.ExceptionCustomSP;

public interface OutfitServiceInt {
	
	public ResponseSP<PageResponse<List<OutfitDTO>>> getAllData(FiltersRequest<OutfitFilters> filters) throws ExceptionCustomSP;
	public ResponseSP<Long> crearActualizarOutfit(OutfitCreateEditRequest request, String action) throws ExceptionCustomSP;
	public ResponseSP<Long> eliminarOutfit(DeleteGenericRequest request) throws ExceptionCustomSP;
	public ResponseSP<Long> establecerOutfitFavorito(DeleteGenericRequest request) throws ExceptionCustomSP;
}
