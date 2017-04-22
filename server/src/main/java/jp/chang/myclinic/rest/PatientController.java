package jp.chang.myclinic.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.beans.factory.annotation.Autowired;

import jp.chang.myclinic.db.Patient;
import jp.chang.myclinic.db.PatientRepository;

@RestController
@RequestMapping("/patient")
public class PatientController {

	@Autowired
	private PatientRepository patientRepository;

	@RequestMapping("{patientId}")
	public PatientJS getPatient(@PathVariable Integer patientId){
		Patient patient = patientRepository.findOne(patientId);
		return JsonMapper.toPatientJS(patient);
	}

}