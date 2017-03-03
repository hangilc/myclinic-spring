package jp.chang.myclinic.web.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;

import jp.chang.myclinic.web.service.json.JsonPatient;
import jp.chang.myclinic.web.HandlerException;

import jp.chang.myclinic.model.Patient;
import jp.chang.myclinic.model.PatientRepository;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value="/service", params="_q")
public class PatientController {
	@Autowired PatientRepository patientRepository;

	@RequestMapping(value="", method=RequestMethod.GET, params={"_q=get_patient", "patient_id"})
	public JsonPatient getPatient(@RequestParam(value="patient_id") int patientId) {
		if( patientId <= 0 ){
			throw new HandlerException("invalid patient_id");
		}
		Patient patient = patientRepository.findOne(patientId);
		JsonPatient json = JsonPatient.fromPatient(patient);
		return json;
	}

	@RequestMapping(value="", method=RequestMethod.GET, params={"_q=search_patient", "text"})
	public List<JsonPatient> searchPatient(@RequestParam("text") String text){
		text = text.trim();
		text = text.replace("\u3000", " "); // zenkaku space
		String[] words = text.split("\\s", 2);
		Pageable pageable = new PageRequest(0, 100, new Sort(Sort.Direction.ASC, "lastNameYomi", "firstNameYomi"));
		List<Patient> patients;
		if( words.length == 1 ){
			patients = patientRepository.searchPatientByName(text, pageable);
		} else {
			patients = patientRepository.searchPatientByName(words[0], words[1], pageable);
		}
		return patients.stream()
				.map(JsonPatient::fromPatient)
				.collect(Collectors.toList());
	}

	// testing master-map
	@Autowired MasterMap masterMap;

}
