package jp.chang.myclinic.db;

import org.springframework.stereotype.Component;

import jp.chang.myclinic.dto.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.ArrayList;

import java.util.stream.Stream;
import java.util.stream.Collectors;

@Component
public class DbGateway {
	@Autowired
	private DTOMapper mapper;
	@Autowired
	private PatientRepository patientRepository;
	@Autowired
	private WqueueRepository wqueueRepository;

	public PatientDTO getPatient(int patientId){
		Patient patient = patientRepository.findOne(patientId);
		return mapper.toPatientDTO(patient);
	}

	public List<WqueueFullDTO> listWqueueFull(){
		return wqueueRepository.findAllAsStream()
			.map(wqueue -> {
				WqueueFullDTO wqueueFullDTO = new WqueueFullDTO();
				wqueueFullDTO.wqueue = mapper.toWqueueDTO(wqueue);
				return wqueueFullDTO;
			})
			.collect(Collectors.toList());
	}
}