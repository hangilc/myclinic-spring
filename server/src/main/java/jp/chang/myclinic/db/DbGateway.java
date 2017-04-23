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
	@Autowired
	private ShahokokuhoRepository shahokokuhoRepository;

	public PatientDTO getPatient(int patientId){
		Patient patient = patientRepository.findOne(patientId);
		return mapper.toPatientDTO(patient);
	}

	public List<WqueueFullDTO> listWqueueFull(){
		return wqueueRepository.findAllAsStream()
			.map(wqueue -> {
				WqueueFullDTO wqueueFullDTO = new WqueueFullDTO();
				wqueueFullDTO.wqueue = mapper.toWqueueDTO(wqueue);
				wqueueFullDTO.visit = mapper.toVisitDTO(wqueue.getVisit());
				wqueueFullDTO.patient = mapper.toPatientDTO(wqueue.getVisit().getPatient());
				return wqueueFullDTO;
			})
			.collect(Collectors.toList());
	}

	public int enterPatient(PatientDTO patientDTO){
		Patient patient = mapper.fromPatientDTO(patientDTO);
		patient.setPatientId(0);
		patient = patientRepository.save(patient);
		return patient.getPatientId();
	}

	public int enterShahokokuho(ShahokokuhoDTO shahokokuhoDTO){
		Shahokokuho shahokokuho = mapper.fromShahokokuhoDTO(shahokokuhoDTO);
		shahokokuho.setShahokokuhoId(0);
		shahokokuho = shahokokuhoRepository.save(shahokokuho);
		return shahokokuho.getShahokokuhoId();
	}
}