package com.persona.superation.service;

import java.util.List;

import com.persona.superation.dto.DeleteGenericRequest;
import com.persona.superation.dto.FiltersRequest;
import com.persona.superation.dto.PageResponse;
import com.persona.superation.dto.PrendaCreateEditRequest;
import com.persona.superation.dto.PrendaDTO;
import com.persona.superation.dto.PrendaFilters;
import com.persona.superation.dto.ResponseSP;
import com.persona.superation.exception.ExceptionCustomSP;

public interface PrendaServiceInt {
	
	public ResponseSP<PageResponse<List<PrendaDTO>>> getAllData(FiltersRequest<PrendaFilters> filters) throws ExceptionCustomSP;
	public ResponseSP<Long> crearActualizarPrenda(PrendaCreateEditRequest request, String action) throws ExceptionCustomSP;
	public ResponseSP<Long> eliminarPrenda(DeleteGenericRequest request) throws ExceptionCustomSP;
}
