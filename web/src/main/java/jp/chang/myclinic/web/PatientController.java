package jp.chang.myclinic.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;

import jp.chang.myclinic.web.json.JsonPatient;

import jp.chang.myclinic.model.Patient;
import jp.chang.myclinic.model.PatientRepository;


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

}
