package com.persona.superation.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import com.persona.superation.dto.OutfitCreateEditRequest;
import com.persona.superation.dto.OutfitDTO;
import com.persona.superation.dto.OutfitFilters;
import com.persona.superation.dto.PageResponse;
import com.persona.superation.dto.ResponseSP;
import com.persona.superation.entity.Outfit;
import com.persona.superation.entity.OutfitClothing;
import com.persona.superation.exception.ExceptionCustomSP;
import com.persona.superation.repository.OutfitClothingRepository;
import com.persona.superation.repository.OutfitRepository;
import com.persona.superation.repository.PrendaRepository;
import com.persona.superation.util.SPUtils;

@Service
public class OutfitServiceImp implements OutfitServiceInt {

	@Autowired
	private OutfitRepository outfitRepository;

	@Autowired
	private PrendaRepository prendaRepository;

	@Autowired
	private OutfitClothingRepository outfitClothingRepository;

	@Autowired
	private FileStorageService fileStorageService;

	@Override
	public ResponseSP<PageResponse<List<OutfitDTO>>> getAllData(FiltersRequest<OutfitFilters> filters)
			throws ExceptionCustomSP {
		Sort sort = Sort.by(Direction.DESC, "fechaUltimoUso");
		Pageable pageable = PageRequest.of(filters.getPage(), filters.getSize(), sort);

		OutfitFilters f = filters.getFilters();
		Page<OutfitDTO> list = this.generateFilterAndGetData(f, pageable);

		PageResponse<List<OutfitDTO>> response = new PageResponse<>();

		response.setContent(list.getContent());
		response.setCurrentPage(list.getPageable().getPageNumber());
		response.setElementsPage(list.getNumberOfElements());
		response.setTotalElements(list.getTotalElements());
		response.setTotalPages(list.getTotalPages());

		return SPUtils.generateResponseService(response, SPConstants.MESSAGE_SUCCESS, SPConstants.SHORT_CODE_SUCCESS,
				HttpStatus.OK.value());
	}

	private Page<OutfitDTO> generateFilterAndGetData(OutfitFilters f, Pageable pageable) {
		LocalDateTime fechaCreacionInicio = null;
		LocalDateTime fechaCreacionFin = null;
		LocalDateTime fechaUltimoUsoInicio = null;
		LocalDateTime fechaUltimoUsoFin = null;

		if (f.getFechaCreacion() != null) {
			Instant instant = f.getFechaCreacion().toInstant();
			ZoneId zoneId = ZoneId.systemDefault();
			LocalDate fecha = instant.atZone(zoneId).toLocalDate();
			fechaCreacionInicio = fecha.atStartOfDay();
			fechaCreacionFin = fecha.plusDays(1).atStartOfDay().minusNanos(1);
		}

		if (f.getFechaUltimoUso() != null) {
			Instant instant = f.getFechaUltimoUso().toInstant();
			ZoneId zoneId = ZoneId.systemDefault();
			LocalDate fecha = instant.atZone(zoneId).toLocalDate();
			fechaUltimoUsoInicio = fecha.atStartOfDay();
			fechaUltimoUsoFin = fecha.plusDays(1).atStartOfDay().minusNanos(1);
		}

		Page<OutfitDTO> list = this.outfitRepository.findAllByFilters(f.getNombre(),
				f.getCategorias().isEmpty() ? null : f.getCategorias(),
				f.getTiempoDia().isEmpty() ? null : f.getTiempoDia(), fechaCreacionInicio, fechaCreacionFin,
				fechaUltimoUsoInicio, fechaUltimoUsoFin, f.getColores().isEmpty() ? null : f.getColores(), pageable);

		list.forEach(o -> {
			List<OutfitClothing> ocList = this.outfitClothingRepository.findByOutfit(o.getId());

			List<OutfitClothing> ocListSuperiores = ocList.stream()
					.filter(s -> s.getTipoPrendaClothingId().equalsIgnoreCase(SPConstants.TYPE_CLOTHING_SUPERIOR))
					.collect(Collectors.toList());

			List<OutfitClothing> ocListInferiores = ocList.stream()
					.filter(s -> s.getTipoPrendaClothingId().equalsIgnoreCase(SPConstants.TYPE_CLOTHING_INFERIOR))
					.collect(Collectors.toList());

			List<OutfitClothing> ocListAccesorios = ocList.stream()
					.filter(s -> s.getTipoPrendaClothingId().equalsIgnoreCase(SPConstants.TYPE_CLOTHING_ACCESORIOS))
					.collect(Collectors.toList());

			List<OutfitClothing> ocListCalzado = ocList.stream()
					.filter(s -> s.getTipoPrendaClothingId().equalsIgnoreCase(SPConstants.TYPE_CLOTHING_CALZADO))
					.collect(Collectors.toList());

			o.setPrendasAccesorios(this.prendaRepository
					.findByIdIn(ocListAccesorios.stream().map(OutfitClothing::getPrenda).collect(Collectors.toList())));
			o.setPrendasCalzado(this.prendaRepository
					.findByIdIn(ocListCalzado.stream().map(OutfitClothing::getPrenda).collect(Collectors.toList())));
			o.setPrendasInferiores(this.prendaRepository
					.findByIdIn(ocListInferiores.stream().map(OutfitClothing::getPrenda).collect(Collectors.toList())));
			o.setPrendasSuperiores(this.prendaRepository
					.findByIdIn(ocListSuperiores.stream().map(OutfitClothing::getPrenda).collect(Collectors.toList())));
		});

		return list;
	}

	@Override
	public ResponseSP<Long> crearActualizarOutfit(OutfitCreateEditRequest request, String action)
			throws ExceptionCustomSP {
		Outfit outfitEntity = new Outfit();
		String fileName = "";
		
		String prendasOrdenadas = Stream
				.of(request.getPrendasSuperiores(), request.getPrendasInferiores(), request.getPrendasCalzado(),
						request.getPrendasAccesorios())
				.flatMap(List::stream).sorted().map(String::valueOf).collect(Collectors.joining(","));

		List<Long> existOutfitWithSameClothings = this.outfitClothingRepository
				.findOutfitsWithSamePrendas(prendasOrdenadas);

		if (action == SPConstants.ACTION_CREATE) {
			Optional<Outfit> existWithSameName = this.outfitRepository.findByNombreOutfit(request.getNombre());

			if (existWithSameName.isPresent()) {
				throw new ExceptionCustomSP(String.format("Outfit con nombre %s ya existe", request.getNombre()),
						HttpStatus.BAD_REQUEST, SPConstants.SHORT_CODE_ERROR);
			}

			boolean exist = !existOutfitWithSameClothings.isEmpty();

			if (exist) {
				throw new ExceptionCustomSP("Ya existe un outfit con las mismas prendas", HttpStatus.BAD_REQUEST,
						SPConstants.SHORT_CODE_ERROR);
			}

			outfitEntity.setFechaUltimoUso(null);
			outfitEntity.setEsFavorito(null);
			outfitEntity.setFechaCreacion(LocalDateTime.now());
		}

		Optional<Outfit> outfit = null;
		if (action == SPConstants.ACTION_UPDATE) {
			
			outfit = this.outfitRepository.findById(request.getId());

			if (!outfit.isPresent()) {
				throw new ExceptionCustomSP(String.format("Outfit con id %s no encontrado", request.getId().toString()),
						HttpStatus.NOT_FOUND, SPConstants.SHORT_CODE_ERROR);
			}
			
			
			boolean exist = !existOutfitWithSameClothings.isEmpty();

			if (exist && existOutfitWithSameClothings.get(0) != outfit.get().getId()) {
				throw new ExceptionCustomSP("Ya existe un outfit con las mismas prendas", HttpStatus.BAD_REQUEST,
						SPConstants.SHORT_CODE_ERROR);
			}

			outfitEntity.setId(outfit.get().getId());
			outfitEntity.setFechaUltimoUso(outfit.get().getFechaUltimoUso());
			outfitEntity.setEsFavorito(outfit.get().getEsFavorito());
			outfitEntity.setFechaCreacion(outfit.get().getFechaCreacion());
		}

		outfitEntity.setColorPrincipal(request.getColor());
		outfitEntity.setNombreOutfit(request.getNombre());
		outfitEntity.setCategoria(request.getCategoria());
		outfitEntity.setTiempoDia(request.getTiempoDia());

		if (request.getImagen() != null) {
			if (action == SPConstants.ACTION_UPDATE) {
				// ELIMINAMOS EL ARCHIVO ANTERIOR
				this.fileStorageService.eliminarArchivoAnterior(outfit.get().getImagen());
			}
			fileName = this.fileStorageService.guardarArchivo(request.getImagen(), null);
		} else {
			fileName = outfit.get().getImagen();
		}

		outfitEntity.setImagen(fileName);

		this.outfitRepository.save(outfitEntity);

		this.guardarRelacionOutfitPrendas(request, outfitEntity, action);

		return SPUtils.generateResponseService(outfitEntity.getId(),
				String.format(SPConstants.MESSAGE_SUCCESS_OPT, "Outfit guardado"), SPConstants.SHORT_CODE_SUCCESS,
				HttpStatus.OK.value());
	}

	private void guardarRelacionOutfitPrendas(OutfitCreateEditRequest request, Outfit outfitEntity, String action) {

	    // Unificar todas las listas de prendas en una sola y quitar duplicados
	    List<Long> todasLasPrendas = Stream
	            .of(request.getPrendasSuperiores(), request.getPrendasInferiores(),
	                request.getPrendasAccesorios(), request.getPrendasCalzado())
	            .flatMap(List::stream)
	            .distinct()
	            .toList();

	    if (todasLasPrendas.isEmpty()) {
	        return;
	    }

	    // 🔹 Si es UPDATE: borrar todas las relaciones previas del outfit
	    if (SPConstants.ACTION_UPDATE.equals(action)) {
	        List<OutfitClothing> relacionesPrevias = outfitClothingRepository.findByOutfit(outfitEntity.getId());
	        if (!relacionesPrevias.isEmpty()) {
	            outfitClothingRepository.deleteAll(relacionesPrevias);
	        }
	    }

	    // 🔹 Traer todos los tipos de prenda en una sola consulta
	    Map<Long, String> tiposPorPrenda = outfitClothingRepository.findTiposByIds(todasLasPrendas).stream()
	            .collect(Collectors.toMap(
	                row -> (Long) row[0],     // ID prenda
	                row -> (String) row[1]   // tipoPrendaClothingId
	            ));

	    // 🔹 Crear nuevas relaciones
	    List<OutfitClothing> nuevasRelaciones = todasLasPrendas.stream()
	            .map(prendaId -> {
	                OutfitClothing oc = new OutfitClothing();
	                oc.setId(null);
	                oc.setOutfit(outfitEntity.getId());
	                oc.setPrenda(prendaId);
	                oc.setTipoPrendaClothingId(tiposPorPrenda.get(prendaId));
	                return oc;
	            })
	            .toList();

	    // 🔹 Guardar en lote
	    if (!nuevasRelaciones.isEmpty()) {
	        outfitClothingRepository.saveAll(nuevasRelaciones);
	    }
	}

	@Override
	public ResponseSP<Long> eliminarOutfit(DeleteGenericRequest request) throws ExceptionCustomSP {
		Outfit outfit = this.outfitRepository.findById(request.getId()).orElse(null);

		if (outfit == null) {
			throw new ExceptionCustomSP(String.format("Outfit con id %s no encontrado", request.getId().toString()),
					HttpStatus.NOT_FOUND, SPConstants.SHORT_CODE_ERROR);
		}
		
		List<OutfitClothing> outfitClothingList = this.outfitClothingRepository.findByOutfit(outfit.getId());
		
		// ELIMINAMOS PRIMERO LA RELACIÓN
		this.outfitClothingRepository.deleteAll(outfitClothingList);

		this.outfitRepository.delete(outfit);

		return SPUtils.generateResponseService(outfit.getId(),
				String.format(SPConstants.MESSAGE_SUCCESS_OPT, "Outfit eliminado"),
				SPConstants.SHORT_CODE_SUCCESS, HttpStatus.OK.value());
	}

	@Override
	public ResponseSP<Long> establecerOutfitFavorito(DeleteGenericRequest request) throws ExceptionCustomSP {
		Outfit outfit = this.outfitRepository.findById(request.getId()).orElse(null);

		if (outfit == null) {
			throw new ExceptionCustomSP(String.format("Outfit con id %s no encontrado", request.getId().toString()),
					HttpStatus.NOT_FOUND, SPConstants.SHORT_CODE_ERROR);
		}

		outfit.setEsFavorito(outfit.getEsFavorito() != null ? !outfit.getEsFavorito() : true);

		this.outfitRepository.save(outfit);

		return SPUtils.generateResponseService(outfit.getId(),
				String.format(SPConstants.MESSAGE_SUCCESS_OPT, "Outfit guardado como favorito"),
				SPConstants.SHORT_CODE_SUCCESS, HttpStatus.OK.value());
	}

}
