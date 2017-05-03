package jp.chang.myclinic.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import jp.chang.myclinic.db.DbGateway;
import jp.chang.myclinic.dto.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

@RestController
@RequestMapping("/json")
@Transactional
public class PatientController {

	@Autowired
	private DbGateway dbGateway;

	@RequestMapping(value="/get-patient", method=RequestMethod.GET)
	public PatientDTO getPatient(@RequestParam("patient-id") int patientId){
		return dbGateway.getPatient(patientId);
	}

	@RequestMapping(value="/enter-patient", method=RequestMethod.POST)
	public int enterPatient(@RequestBody PatientDTO patient){
		return dbGateway.enterPatient(patient);
	}

	@RequestMapping(value="/enter-patient-with-hoken", method=RequestMethod.POST)
	public PatientHokenListDTO enterPatientWithHoken(@RequestBody PatientHokenListDTO patientHokenList){
		int patientId = dbGateway.enterPatient(patientHokenList.patientDTO);
		patientHokenList.patientDTO.patientId = patientId;
		HokenListDTO hokenListDTO = patientHokenList.hokenListDTO;
		hokenListDTO.shahokokuhoListDTO.forEach(shahokokuhoDTO -> {
			shahokokuhoDTO.patientId = patientId;
			shahokokuhoDTO.shahokokuhoId = dbGateway.enterShahokokuho(shahokokuhoDTO);
		});
		hokenListDTO.koukikoureiListDTO.forEach(koukikoureiDTO -> {
			koukikoureiDTO.patientId = patientId;
			koukikoureiDTO.koukikoureiId = dbGateway.enterKoukikourei(koukikoureiDTO);
		});
		hokenListDTO.roujinListDTO.forEach(roujinDTO -> {
			roujinDTO.patientId = patientId;
			roujinDTO.roujinId = dbGateway.enterRoujin(roujinDTO);
		});
		hokenListDTO.kouhiListDTO.forEach(kouhiDTO -> {
			kouhiDTO.patientId = patientId;
			kouhiDTO.kouhiId = dbGateway.enterKouhi(kouhiDTO);
		});
		return patientHokenList;
	}

	@RequestMapping(value="/search-patient-by-name", method=RequestMethod.GET)
	public List<PatientDTO> searchPatientByName(@RequestParam(name="last-name", defaultValue="") String lastName,
			@RequestParam(name="first-name", defaultValue="") String firstName){
		if( !lastName.isEmpty() && !firstName.isEmpty() ){
			return dbGateway.searchPatientByName(lastName, firstName);
		} else if( !lastName.isEmpty() ){
			return dbGateway.searchPatientByLastName(lastName);
		} else if( !firstName.isEmpty() ){
			return dbGateway.searchPatientByFirstName(firstName);
		} else {
			return Collections.emptyList();
		}
	}

	@RequestMapping(value="/search-patient-by-yomi", method=RequestMethod.GET)
	public List<PatientDTO> searchPatientByYomi(@RequestParam(name="last-name-yomi", defaultValue="") String lastNameYomi,
			@RequestParam(name="first-name-yomi", defaultValue="") String firstNameYomi){
		if( !lastNameYomi.isEmpty() && !firstNameYomi.isEmpty() ){
			return dbGateway.searchPatientByYomi(lastNameYomi, firstNameYomi);
		} else if( !lastNameYomi.isEmpty() ){
			return dbGateway.searchPatientByLastNameYomi(lastNameYomi);
		} else if( !firstNameYomi.isEmpty() ){
			return dbGateway.searchPatientByFirstNameYomi(firstNameYomi);
		} else {
			return Collections.emptyList();
		}
	}

	@RequestMapping(value="/list-recently-registered-patients", method=RequestMethod.GET)
	public List<PatientDTO> listRecentlyRegisteredPatients(@RequestParam(name="n", defaultValue="20") int n){
		return dbGateway.listRecentlyRegisteredPatients(n);
	}



}