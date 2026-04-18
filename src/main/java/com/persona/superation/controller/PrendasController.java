package com.persona.superation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.persona.superation.constants.SPConstants;
import com.persona.superation.dto.DeleteGenericRequest;
import com.persona.superation.dto.FiltersRequest;
import com.persona.superation.dto.PageResponse;
import com.persona.superation.dto.PrendaCreateEditRequest;
import com.persona.superation.dto.PrendaDTO;
import com.persona.superation.dto.PrendaFilters;
import com.persona.superation.dto.ResponseSP;
import com.persona.superation.exception.ExceptionCustomSP;
import com.persona.superation.service.PrendaServiceInt;
import com.persona.superation.util.SPUtils;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/prendas")
public class PrendasController {

	@Autowired
	private PrendaServiceInt prendasService;

	@PostMapping(value = "/obtenerPrendas")
	public ResponseEntity<ResponseSP<PageResponse<List<PrendaDTO>>>> obtenerPrendas(
			@RequestBody FiltersRequest<PrendaFilters> request) throws ExceptionCustomSP {
		ResponseSP<PageResponse<List<PrendaDTO>>> response = this.prendasService.getAllData(request);
		return SPUtils.generateResponseController(response, response.getStatus());
	}

	@PostMapping(value = "/crear", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ResponseSP<Long>> crear(@ModelAttribute @Valid PrendaCreateEditRequest request)
			throws ExceptionCustomSP {
		ResponseSP<Long> response = this.prendasService.crearActualizarPrenda(request, SPConstants.ACTION_CREATE);
		return SPUtils.generateResponseController(response, response.getStatus());
	}

	@PostMapping(value = "/actualizar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ResponseSP<Long>> actualizar(@ModelAttribute @Valid PrendaCreateEditRequest request)
			throws ExceptionCustomSP {
		ResponseSP<Long> response = this.prendasService.crearActualizarPrenda(request, SPConstants.ACTION_UPDATE);
		return SPUtils.generateResponseController(response, response.getStatus());
	}
	
	@PostMapping(value = "/eliminar")
	public ResponseEntity<ResponseSP<Long>> eliminar(@Valid @RequestBody DeleteGenericRequest request)
			throws ExceptionCustomSP {
		ResponseSP<Long> response = this.prendasService.eliminarPrenda(request);
		return SPUtils.generateResponseController(response, response.getStatus());
	}
}
