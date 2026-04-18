package com.persona.superation.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.persona.superation.constants.SPConstants;
import com.persona.superation.dto.DeleteGenericRequest;
import com.persona.superation.dto.FiltersRequest;
import com.persona.superation.dto.PageResponse;
import com.persona.superation.dto.PrendaCreateEditRequest;
import com.persona.superation.dto.PrendaDTO;
import com.persona.superation.dto.PrendaFilters;
import com.persona.superation.dto.ResponseSP;
import com.persona.superation.entity.Prenda;
import com.persona.superation.exception.ExceptionCustomSP;
import com.persona.superation.repository.PrendaRepository;
import com.persona.superation.util.SPUtils;

@Service
public class PrendaServiceImp implements PrendaServiceInt {

	@Autowired
	private PrendaRepository prendaRepository;

	@Autowired
	private FileStorageService fileStorageService;

	@Override
	public ResponseSP<PageResponse<List<PrendaDTO>>> getAllData(FiltersRequest<PrendaFilters> filters)
			throws ExceptionCustomSP {
		Sort sort = Sort.by(Direction.DESC, "fechaActualizacion");
		Pageable pageable = PageRequest.of(filters.getPage(), filters.getSize(), sort);

		PrendaFilters f = filters.getFilters();
		Page<PrendaDTO> list = this.generateFilterAndGetData(f, pageable);

		PageResponse<List<PrendaDTO>> response = new PageResponse<>();

		response.setContent(list.getContent());
		response.setCurrentPage(list.getPageable().getPageNumber());
		response.setElementsPage(list.getNumberOfElements());
		response.setTotalElements(list.getTotalElements());
		response.setTotalPages(list.getTotalPages());

		return SPUtils.generateResponseService(response, SPConstants.MESSAGE_SUCCESS, SPConstants.SHORT_CODE_SUCCESS,
				HttpStatus.OK.value());
	}
	
	private Page<PrendaDTO> generateFilterAndGetData(PrendaFilters f, Pageable pageable) {
		LocalDateTime  fechaCreacionInicio = null;
		LocalDateTime  fechaCreacionFin = null;
		LocalDateTime  fechaActualizacionInicio = null;
		LocalDateTime  fechaActualizacionFin = null;

		if (f.getFechaCreacion() != null) {
			 Instant instant = f.getFechaCreacion().toInstant();
			    ZoneId zoneId = ZoneId.systemDefault();
			    LocalDate fecha = instant.atZone(zoneId).toLocalDate();
		    fechaCreacionInicio = fecha.atStartOfDay();
		    fechaCreacionFin = fecha.plusDays(1).atStartOfDay().minusNanos(1);
		}

		if (f.getFechaActualizacion() != null) {
			Instant instant = f.getFechaActualizacion().toInstant();
		    ZoneId zoneId = ZoneId.systemDefault();
		    LocalDate fecha = instant.atZone(zoneId).toLocalDate();
		    fechaActualizacionInicio = fecha.atStartOfDay();
		    fechaActualizacionFin = fecha.plusDays(1).atStartOfDay().minusNanos(1);
		}

		Page<PrendaDTO> list = this.prendaRepository.findAllByFilters(
		    f.getNombre(),
		    f.getTipoPrenda().isEmpty() ? null : f.getTipoPrenda(),
		    fechaCreacionInicio,
		    fechaCreacionFin,
		    fechaActualizacionInicio,
		    fechaActualizacionFin,
		    f.getColores().isEmpty() ? null : f.getColores(),
		    pageable
		);
		
		return list;
	}

	@Override
	public ResponseSP<Long> crearActualizarPrenda(PrendaCreateEditRequest request, String action)
			throws ExceptionCustomSP {
		Prenda prendaEntity = new Prenda();
		String fileName = "";

		if (action == SPConstants.ACTION_CREATE) {
			Optional<Prenda> existWithSameName = this.prendaRepository.findByNombre(request.getNombre());

			if (existWithSameName.isPresent()) {
				throw new ExceptionCustomSP(String.format("Prenda con nombre %s ya existe", request.getNombre()),
						HttpStatus.BAD_REQUEST, SPConstants.SHORT_CODE_ERROR);
			}
		}
		Optional<Prenda> prenda = null;
		if (action == SPConstants.ACTION_UPDATE) {
			prenda = this.prendaRepository.findById(request.getId());

			if (!prenda.isPresent()) {
				throw new ExceptionCustomSP(String.format("Prenda con id %s no encontrada", request.getId().toString()),
						HttpStatus.NOT_FOUND, SPConstants.SHORT_CODE_ERROR);
			}

			fileName = prenda.get().getImagen();

			prendaEntity.setId(request.getId());
			prendaEntity.setFechaCreacion(prenda.get().getFechaCreacion());
		} else {
			prendaEntity.setFechaCreacion(LocalDateTime.now());
		}

		if (request.getImagen() != null) {
			if (action == SPConstants.ACTION_UPDATE) {
				// ELIMINAMOS EL ARCHIVO ANTERIOR
				this.fileStorageService.eliminarArchivoAnterior(prenda.get().getImagen());
			}
			fileName = this.fileStorageService.guardarArchivo(request.getImagen(), null);
		}

		prendaEntity.setColor(request.getColor());
		prendaEntity.setImagen(fileName);
		prendaEntity.setNombre(request.getNombre());
		prendaEntity.setTipoPrenda(request.getTipoPrenda());
		prendaEntity.setFechaActualizacion(LocalDateTime.now());

		this.prendaRepository.save(prendaEntity);

		return SPUtils.generateResponseService(prendaEntity.getId(),
				String.format(SPConstants.MESSAGE_SUCCESS_OPT, "Prenda guardada"), SPConstants.SHORT_CODE_SUCCESS,
				HttpStatus.OK.value());
	}

	@Override
	public ResponseSP<Long> eliminarPrenda(DeleteGenericRequest request) throws ExceptionCustomSP {
		Optional<Prenda> prenda = this.prendaRepository.findById(request.getId());

		if (!prenda.isPresent()) {
			throw new ExceptionCustomSP(String.format("Prenda con id %s no encontrada", request.getId().toString()),
					HttpStatus.NOT_FOUND, SPConstants.SHORT_CODE_ERROR);
		}

		this.prendaRepository.delete(prenda.get());

		return SPUtils.generateResponseService(null, String.format(SPConstants.MESSAGE_SUCCESS_OPT, "Prenda eliminada"),
				SPConstants.SHORT_CODE_SUCCESS, HttpStatus.OK.value());
	}

}
